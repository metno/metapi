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

package statsd

import java.net.{DatagramPacket, DatagramSocket, InetAddress}
import scala.util.{Failure, Success, Try}
import play.api.Play.current

import play.api.Logger

// $COVERAGE-OFF$ Tricky to coverage test
object StatsD {
  private val socket = new DatagramSocket()
  private val host = InetAddress.getLocalHost() // for now, assume that the consumer (like Telegraf) runs on the local host
  // scalastyle:off magic.number
  private val port: Int = current.configuration.getInt("statsd.port").getOrElse(8125)
  // scalastyle:on magic.number
  private val active: Boolean = current.configuration.getBoolean("statsd.active").getOrElse(false)
  private var failing: Boolean = false

  /**
    * Sends a string on a UDP socket.
    */
  def send(s: String): Unit = {
    if (active) {
      Try({
        val buffer = s.map(_.toByte).toArray
        val packet = new DatagramPacket(buffer, buffer.length, host, port)
        socket.send(packet)
      }) match {
        case Success(_) => {
          if (failing) { // log that we've stopped failing
            Logger.warn("StatsD: successfully sent datagram (not logging subsequent successful sends)")
            failing = false
          }
        }
        case Failure(e) => {
          if (!failing) { // log that we've started failing
            Logger.warn("StatsD: failed to send datagram: " + e + " (not logging subsequent failed sends)")
            failing = true
          }
        }
      }
    }
  }
}
// $COVERAGE-ON$
