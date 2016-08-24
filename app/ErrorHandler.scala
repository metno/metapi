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
import no.met.json._

// $COVERAGE-OFF$ Legacy: Global object seems to not be available in tests
//scalastyle:off public.methods.have.type

@Singleton
class ErrorHandler @Inject() (env: Environment, config: Configuration, sourceMapper: OptionalSourceMapper, router: Provider[Router] )
    extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  private def formatOf(request: RequestHeader): String = {
    val elements = request.path.split('.')
    elements(elements.length - 1)
  }

  private def showError(code: Int, msg: String, help: Option[String], request: RequestHeader): Result = {
    val output = formatOf(request) match {
      case "jsonld" => new ErrorJsonFormat().error(DateTime.now(DateTimeZone.UTC), code, Some(msg), help)(request)
      case "csv" => msg
      case _ => msg
    }
    val status = new Results.Status(code)
    status(output)
  }

  /**
   * Invoked when the client makes a bad request
   */
  override def onBadRequest(request: RequestHeader, error: String) = {
    Future.successful(
      showError(BAD_REQUEST, error, None, request)
    )
  }

  override def onDevServerError(request: RequestHeader, exception: UsefulException) = {
    Future.successful(
      exception.getCause match {
        case x: BadRequestException => {
          val response = showError(x code, x getLocalizedMessage, x help, request)
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

  override def onForbidden(request: RequestHeader, error: String) = {
    Future.successful(
      showError(FORBIDDEN, error, None, request) // scalastyle:ignore magic.number
    )
  }

  override def onProdServerError(request: RequestHeader, exception: UsefulException) = {
    Future.successful(
      exception.getCause match {
        case x: BadRequestException => {
          val response = showError(x code, x getLocalizedMessage, x help, request)
          if (x.headers.isEmpty) {
            response
          } else {
            response withHeaders (x.headers: _*)
          }
        }
        case x => {
          val userKey = request.headers.get("Authorization").getOrElse("none")
          Logger.error(s"Error on request ${request.uri} from user with credentials <$userKey>", x)
          showError(INTERNAL_SERVER_ERROR,
              "The server encountered an internal error",
              Some("Try again later, or report this to met.no"),
              request)
        }
      }
    )
  }


}

//scalastyle:on
// $COVERAGE-ON$
