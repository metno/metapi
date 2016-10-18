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

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.routing.Router
import play.api.http.DefaultHttpErrorHandler
import play.api.http.Status._
import com.github.nscala_time.time.Imports._
import scala.concurrent._
import scala.language.postfixOps
import no.met.data._

@Singleton
class ErrorHandler @Inject() (env: Environment, config: Configuration, sourceMapper: OptionalSourceMapper, router: Provider[Router] )
    extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  override def onBadRequest(request: RequestHeader, msg: String): Future[Result] = {
    Future.successful(
      Error.error(BAD_REQUEST, Some(msg), None)(request)
    )
  }

  override def onDevServerError(request: RequestHeader, exception: UsefulException): Future[Result] = {
    Future.successful(
      exception.getCause match {
        case x: BadRequestException => {
          val response = Error.error(x code, Option(x getLocalizedMessage), x help)(request)
          if (x.headers.isEmpty) {
            response
          } else {
            response withHeaders (x.headers: _*)
          }
        }
        case x => {
          // Default Play Error display
          InternalServerError(views.html.defaultpages.devError(config.getString("play.editor"), exception))
        }
      }
    )
  }

  override def onForbidden(request: RequestHeader, msg: String): Future[Result] = {
    Future.successful(
      Error.error(FORBIDDEN, Option(msg), None)(request)
    )
  }

  override def onProdServerError(request: RequestHeader, exception: UsefulException): Future[Result] = {
    Future.successful(
      exception.getCause match {
        case x: BadRequestException => {
          val response = Error.error(x code, Option(x getLocalizedMessage), x help)(request)
          if (x.headers.isEmpty) {
            response
          } else {
            response withHeaders (x.headers: _*)
          }
        }
        case x => {
          val userKey = request.headers.get("Authorization").getOrElse("none")
          Logger.error(s"Error on request ${request.uri} from user with credentials <$userKey>", x)
          Error.error(INTERNAL_SERVER_ERROR,
              Some("The server encountered an internal error"),
              Some("Try again later, or report this to met.no"))(request)
        }
      }
    )
  }

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future.successful(
      Error.error(statusCode, Some(message), None)(request)
    )
  }

}
