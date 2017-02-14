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
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import javax.inject.Inject
import play.api.http.HttpFilters
import play.api.routing.Router.Tags._
import statsd.StatsD


// Filter applied to each request on each route. This is useful e.g. for logging general traffic to Telegraf/InfluxDB via StatsD.
class RequestFilter extends Filter {

  def apply(nextFilter: RequestHeader => Future[Result]) (requestHeader: RequestHeader): Future[Result] = {
    val startTime = System.currentTimeMillis

    nextFilter(requestHeader).map { result =>

      // get request processing time
      val responseTime = System.currentTimeMillis - startTime

      // log to StatsD
      val endpoint: String = requestHeader.tags.getOrElse(RouteController, "UNDEF") + "/" + requestHeader.tags.getOrElse(RouteActionMethod, "UNDEF")
      StatsD.send(s"response.time,endpoint=${endpoint}:${responseTime}|ms")

      //result.withHeaders("foo1" -> "bar1", "foo2" -> "bar2")
      result
    }
  }
}


class Filters @Inject() (requestFilter: RequestFilter) extends HttpFilters {
  val filters = Seq(requestFilter)
}
