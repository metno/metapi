/*
    MET-API

    Copyright (C) 2014 met.no
    Contact information:
    Norwegian Meteorological Institute
    Box 43 Blindern
    0313 OSLO
    NORWAY
    E-mail: met-api@met.no

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
    MA 02110-1301, USA
*/

package influxdb

import com.paulgoldbaum.influxdbclient._
import com.paulgoldbaum.influxdbclient.Parameter._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Try, Failure, Success}
import scala.collection.mutable.{HashMap, ArrayBuffer, ListBuffer}
import play.api.inject.ApplicationLifecycle
import play.api.Logger
import play.api.Play.current
import javax.inject.{Singleton, Inject}


// This class manages communication with an InfluxDB database server.
// The behavior of the database connection is defined through a set of configuration parameters (matching "influxdb.*" in the config file).
// Values to be reported to the database are passed in the addValues() function.
@Singleton
class IDB @Inject() (applicationLifecycle: ApplicationLifecycle) {

  // --- BEGIN config parameters -------------------------
  private val idbActive: Boolean = current.configuration.getBoolean("influxdb.active").getOrElse(false)

  // Location.
  private val idbHost: String = current.configuration.getString("influxdb.host").getOrElse("localhost")
  // scalastyle:off magic.number
  private val idbPort: Int = current.configuration.getInt("influxdb.port").getOrElse(8086)
  // scalastyle:on magic.number

  // Credentials.
  private val idbUsername: Option[String] = current.configuration.getString("influxdb.username")
  private val idbPassword: Option[String] = current.configuration.getString("influxdb.password")

  // Database name and measurement context.
  private val idbName: String = current.configuration.getString("influxdb.dbname").getOrElse("metapi")
  private val idbMeasurement: String = current.configuration.getString("influxdb.measurement").getOrElse("main")

  // Retention policy duration. Data older than this period will be automatically deleted.
  private val idbRPDuration: String = current.configuration.getString("influxdb.rpduration").getOrElse("52w")

  // Reset pause in seconds. A new reset attempt after a failed one will not be made until after this pause.
  // This way, the program will not become too busy resetting the database connection.
  // scalastyle:off magic.number
  private val idbResetPause: Long = current.configuration.getLong("influxdb.resetpause").getOrElse(5)
  // scalastyle:on magic.number

  // Update pause in seconds. After sending values to the database, the next set of values will not be sent until after this pause
  // (in the meantime, values are accumulated in a stats structure).
  // This way, traffic on the database is limited.
  // scalastyle:off magic.number
  private val idbUpdatePause: Long = current.configuration.getLong("influxdb.updatepause").getOrElse(5)
  // scalastyle:on magic.number

  // --- END config parameters -------------------------


  private val retPolicy = "rpmetapi" // retention policy (assuming this doesn't need to be configurable)


  // --- BEGIN variable state --------------
  private var conn: Option[InfluxDB] = None // DB connection
  private var database: Option[Database] = None
  private var resetstart: Long = -1 // time (secs since 1970) at which the current reset attempt phase started

  // value stats
  type NumberArray = ArrayBuffer[Double] // values
  type String2NumberArray = HashMap[String, NumberArray] // values per field
  private var gbmap: String2NumberArray = new String2NumberArray() // global fields (i.e. not belonging to a specific endpoint)
  private var epmap: HashMap[String, String2NumberArray] = HashMap[String, String2NumberArray]() // per-endpoint fields

  private var lastDBUpdate: Long = 0 // time (secs since 1970) at which value stats were last sent to the database

  // --- END variable state --------------


  // Attempts to reset the connection to InfluxDB. Upon success conn and database are set to Some(x).
  // Otherwise, conn and database are set to None and an exception is thrown.
  private def reset() = {
    // get connection
    // scalastyle:off null
    conn = Some(InfluxDB.connect(idbHost, idbPort, idbUsername.getOrElse(null), idbPassword.getOrElse(null)))
    // scalastyle:on null

    // get database
    database = Some(conn.get.selectDatabase(idbName))

    val timeout = 10000

    // check that database exists
    Try(Await.result(database.get.exists(), Duration(timeout, MILLISECONDS))) match {
      case Success(exists) => {
        if (!exists) {
          conn = None
          database = None
          throw new Exception(s"database not found: ${idbName}@${idbHost}:${idbPort}")
        }
      }
      case Failure(error) => {
        conn = None
        database = None
        throw new Exception("checking for database existence failed (is the server down?): " + error)
      }
    }

    // set retention policy by first attempting to create it ...
    Try(Await.result(database.get.createRetentionPolicy(retPolicy, idbRPDuration, 1, true), Duration(timeout, MILLISECONDS))) match {
      case Failure(createError) => {
        // ... and then attempting to alter it (assuming it already exists)
        Try(Await.result(database.get.alterRetentionPolicy(retPolicy, idbRPDuration, 1, true), Duration(timeout, MILLISECONDS))) match {
          case Failure(alterError) => {
            // give up
            conn = None
            database = None
            throw new Exception("failed to set retention policy: " + alterError)
          }
          case Success(_) => // successfully altered an existing RP
        }
      }
      case Success(_) => // successfully created a new RP
    }
  }

  // Attempts to close the client. Logs outcome but does not throw an exception.
  private def close() = {
    if (database != None) {
      Try(database.get.close()) match {
        case Success(()) => {
          Logger.info("InfluxDB: successfully closed client")
        }
        case Failure(error) => {
          Logger.info("InfluxDB: failed to close client: " + error)
        }
      }
    } else {
      Logger.info("InfluxDB: no client to close")
    }
  }


  // Function to be called upon shutting down the application.
  private def cleanup() = {
    Logger.info("InfluxDB: cleaning up ...")
    close()
  }


  // Returns the current time since 1970 in milliseconds.
  private def nowMillis = System.currentTimeMillis


  // Returns the current time since 1970 in seconds.
  private def nowSecs: Long = nowMillis / 1000


  // Updates the value stats with more values.
  private def updateStats(gbvalues: Map[String, Double], endpoint: String, epvalues: Map[String, Double]) = {

    // update global-only stats
    for ((key, value) <- gbvalues) {
      if (!gbmap.contains(key)) { gbmap(key) = new NumberArray() }
      gbmap(key) += value
    }

    // update per-endpoint stats
    if (!epmap.contains(endpoint)) { epmap(endpoint) = new String2NumberArray() }

    for ((key, value) <- epvalues) {
      // add both to the global stats ...
      if (!gbmap.contains(key)) { gbmap(key) = new NumberArray() }
      gbmap(key) += value

      // ... and to the per-endpoint stats
      if (!epmap(endpoint).contains(key)) { epmap(endpoint)(key) = new NumberArray() }
      epmap(endpoint)(key) += value
    }
  }


  // Clears the value stats.
  private def clearStats() = {
    gbmap.clear()
    epmap.clear()
  }


  // Returns true iff the value stats is empty.
  private def statsIsEmpty() = {
    gbmap.isEmpty && epmap.isEmpty
  }


  // Returns a Point object with stats for a set of fields.
  private def pointFromStats(initPoint: Point, vmap: String2NumberArray): Point = {

    var point: Point = initPoint

    // loop over fields
    for (fkey <- vmap.keys) {

      // extract stats from the values registered for this field
      val values = vmap(fkey)
      assert(values.size > 0) // at least one value must exist, namely the one passed upon initial registration of the field key
      val min = values.min
      val max = values.max
      val mean = values.sum / values.size
      val median = {
        val svalues = values.sorted
        val mid = values.size / 2
        (values.size % 2) match {
          case 1 => svalues.apply(mid)
          case _ => 0.5 * (svalues.apply(mid) + svalues.apply(mid - 1))
        }
      }

      // add the stats to the point object
      point = point.addField(s"${fkey}:min", min)
      point = point.addField(s"${fkey}:max", max)
      point = point.addField(s"${fkey}:mean", mean)
      point = point.addField(s"${fkey}:median", median)
    }

    point = point.addField(s"maxCount", vmap.mapValues(na => na.size).values.max)

    point
  }


  // Returns a list of Point objects (one for global stats and one for each endpoint) constructed from the current value stats.
  private def pointsFromStats(): List[Point] = {
    var points = ListBuffer[Point]()
    val currMillis = nowMillis // Note: if the currMillis arg is left out below, the timestamp at the DB server is used instead

    // add global stats, assuming "all" is never used as an endpoint
    points += pointFromStats(Point(idbMeasurement, currMillis).addTag("endpoint", "all"), gbmap)

    // add per-endpoint stats
    for (endpoint <- epmap.keys) {
      points += pointFromStats(Point(idbMeasurement, currMillis).addTag("endpoint", endpoint), epmap(endpoint))
    }

    points.toList
  }


  // Attempts to send the current value stats to the database.
  // scalastyle:off return
  private def sendStats(): Unit = {

    if (statsIsEmpty()) { return } // nothing to send

    // check if the database connection is ok
    val currSecs = nowSecs
    if (database == None) {
      // database connection is down
      if ((currSecs - resetstart) > idbResetPause) {
        // if it's not too early ...
        // ... attempt to reset database connection
        Try(reset()) match {
          case Failure(error) => {
            if (resetstart == -1) {
              Logger.warn("InfluxDB: reset attempt failed: " + error + "; trying again every " + idbResetPause + " secs")
            }
            resetstart = currSecs // start a new reconnection attempt phase
            return
          }
          case Success(_) => {
            Logger.info("InfluxDB: reset attempt succeeded")
            resetstart = -1 // ensure that reset() is called immediately if the need arises (i.e. if the write() call below fails)
          }
        }
      } else {
        return // too early to make a new reset attempt
      }
    }

    // the database connection is apparently ok, so attempt to write the current stats to the database
    val points = pointsFromStats()
    if (!points.isEmpty) {
      database.get.bulkWrite(points, precision = Precision.MILLISECONDS, consistency = Consistency.ALL, retentionPolicy = retPolicy) onComplete {
        case Success(_) => // all's well; points were successfully written
        case Failure(error) => {
          Logger.warn(s"InfluxDB: failed to write points (does the database exist?): " + error)
          database = None // indicate that the database connection is not ok
        }
      }
    }
  }
  // scalastyle:on return


  // Returns true iff the IDB is active, i.e. enabled in the configuration.
  def active(): Boolean = idbActive


  // Adds a new set of values.
  // gbvalues: Global values. epvalues: Values specific to the endpoint.
  def addValues(gbvalues: Map[String, Double], endpoint: String, epvalues: Map[String, Double]): Unit = {
    if (idbActive) {
      this.synchronized {
        // make thread-safe
        updateStats(gbvalues, endpoint, epvalues)
        if ((nowSecs - lastDBUpdate) > idbUpdatePause) {
          sendStats()
          clearStats()
          lastDBUpdate = nowSecs
        }
      }
    }
  }


  // clean up when application shuts down
  applicationLifecycle.addStopHook { () =>
    Future.successful(cleanup())
  }
}
