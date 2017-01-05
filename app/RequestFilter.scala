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

import play.api.mvc.{Result, RequestHeader, Filter}
import play.api.libs.iteratee._
import play.api.Logger
import play.api.routing.Router.Tags._
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import com.kenshoo.play.metrics.Metrics
import com.codahale.metrics.Gauge
import influxdb.IDB
import javax.inject.Inject
import play.api.http.HttpFilters
import scala.collection.mutable.HashMap
import scala.util.{Failure, Success, Try}
import java.io.File


// Filter applied to each request on each route. This is useful e.g. for logging general traffic to InfluxDB.
class RequestFilter @Inject() (metrics: Metrics, idb: IDB) extends Filter {

  private def gaugeToDouble[T](x: Gauge[T]): Option[Double] = x match {
    // scalastyle:off null
    case null => None
    // scalastyle:on null
    case _ => x.getValue() match {
      case y: Int => Some(y.toDouble)
      case y: Long=> Some(y.toDouble)
      case y: Float => Some(y.toDouble)
      case y: Double => Some(y)
      case _ => None
    }
  }


  // Returns disk usage in current directory as percentage of available space, or -1 if an error occurred.
  private def diskUsage(): Double = {
    val f = new File(".")
    Try(f.getFreeSpace() / f.getTotalSpace().toDouble) match {
      case Success(x) => if (x >= 0 && x <= 1) x * 100d else -1D
      case Failure(e) => -1D
    }
  }


  // Returns memory usage as percentage of available memory, or -1 if an error occurred.
  // scalastyle:off cyclomatic.complexity
  private def memoryUsage(): Double = {

    // Returns a value from /proc/meminfo output if key is found, otherwise -1.
    def procMeminfoVal(lines: String, key: String): Long = {
      val pattern = s".*${key}:[^\\d]*([\\d]+).*".r
      val valStr: String = lines.filter(_ >= ' ') match { case pattern(x) => x; case _ => "-1" }
      Try(valStr.toLong) match { case Success(x) => x; case Failure(e) => -1L }
    }

    Try(scala.io.Source.fromFile("/proc/meminfo").mkString) match {
      case Success(lines) => {
        val totMem = procMeminfoVal(lines, "MemTotal")
        val freeMem = procMeminfoVal(lines, "MemFree")

        if ((totMem >= 0) && (freeMem >= 0)) {
          Try((totMem - freeMem) / totMem.toDouble) match {
            case Success(x) => if (x >= 0 && x <= 1) x * 100d else -1D
            case Failure(e) => -1D
          }
        } else {
          -1D
        }
      }
      case Failure(e) => -1D
    }
  }
  // scalastyle:on cyclomatic.complexity


  // Reports values to InfluxDB.
  private def reportToInfluxDB(requestHeader: RequestHeader, result: Result, startTime: Long) = {
    val endTime = System.currentTimeMillis

    // --- BEGIN global values -------------------------
    var gbvalues: HashMap[String, Double] = HashMap[String, Double]()
    val gauges = metrics.defaultRegistry.getGauges()
    gaugeToDouble(gauges.get("jvm.memory.total.used")) match {
      case Some(x) => gbvalues("jvm.memory.total.used") = x;
      case _ =>
    }
    gbvalues("disk_usage") = diskUsage()
    gbvalues("memory_usage") = memoryUsage()
    // add more global values ... TBD
    // --- END global values -------------------------

    // --- BEGIN per-endpoint values -------------------------
    val endpoint: String = requestHeader.tags.getOrElse(RouteController, "UNDEF") + "/" + requestHeader.tags.getOrElse(RouteActionMethod, "UNDEF")
    var epvalues: HashMap[String, Double] = HashMap[String, Double]()
    epvalues("response_time") = endTime - startTime

    val futureBodySize: Future[Long] = {
      result.body |>>> Iteratee.fold(0L) { (total, bytes) =>
        total + bytes.length
      }
    }
    // scalastyle:off magic.number
    val bodySize: Long = Try(Await.result(futureBodySize, Duration(1000, MILLISECONDS))) match {
      // scalastyle:on magic.number
      case Success(size) => size
      case Failure(error) => -1
    }

    epvalues("response_size") = bodySize
    // --- END per-endpoint values -------------------------

    idb.addValues(gbvalues.toMap, endpoint, epvalues.toMap)
  }


  def apply(nextFilter: RequestHeader => Future[Result]) (requestHeader: RequestHeader): Future[Result] = {
    val startTime = System.currentTimeMillis
    nextFilter(requestHeader).map { result =>
      if (idb.active()) { reportToInfluxDB(requestHeader, result, startTime) }
      //result.withHeaders("foo1" -> "bar1", "foo2" -> "bar2")
      result
    }
  }
}


class Filters @Inject() (requestFilter: RequestFilter) extends HttpFilters {
  val filters = Seq(requestFilter)
}
