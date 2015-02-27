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
package controllers
import play.api.mvc._
import play.mvc.Http
import play.api.Logger
import javax.ws.rs.{ QueryParam, PathParam }
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core.util.ScalaJsonUtil
import javax.ws.rs.{ QueryParam, PathParam }
import models.Observation
import play.api.Logger
import javax.ws.rs.core.Response.Status
import util.HttpStatus
import api.ObservationData
import scala.concurrent.impl.Future

@Api(value = "/points", description = "API for retrieval of point data")
class PointController extends BaseApiController {
  var observationData = new ObservationData

  @ApiOperation(
    nickname = "getPoints",
    value = "Retrieve point data",
    notes = "Retrieves point data from the API in JSON format.",
    response = classOf[models.Observation],
    httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = HttpStatus.OK, message = "OK: The request was successful."),
    new ApiResponse(code = HttpStatus.BAD_REQUEST, message = "Bad Request: The request could not be understood or was missing required parameters."),
    new ApiResponse(code = HttpStatus.UNAUTHORIZED, message = "Unauthorized:  authentication failed or user doesn't have permissions for requested operation."),
    new ApiResponse(code = HttpStatus.NOT_FOUND, message = "Not Found: The data that the user requested could not be found."),
    new ApiResponse(code = HttpStatus.SERVICE_UNAVAILABLE, message = "Service Unavailable: The service is temporarily unavailable (e.g., due to scheduled platform maintenance). Try again later.")))
  def getPoints(
    @ApiParam(value = "ID(s) of the data source. Use source '1' to '4' to return data", allowMultiple = true, required = true)@QueryParam("sources") sources: String,
    @ApiParam(value = "Spatial location in WKT")@QueryParam("places") places: Option[String],
    @ApiParam(value = "<a href='http://data.met.no/reference_time.html'>Reference time</a>")@QueryParam("reftime") reftime: Option[String],
    @ApiParam(value = "Valid time")@QueryParam("validtime") validtime: Option[String],
    @ApiParam(value = "MET Parameters", allowMultiple = true)@QueryParam("parameters") parameters: Option[String],
    @ApiParam(value = "Specify attribute fields in the response", allowMultiple = true)@QueryParam("fields") fields: Option[String],
    @ApiParam(value = "Limit the number of points returned")@QueryParam("limit") limit: Option[String],
    @ApiParam(value = "Offset in the response set")@QueryParam("offset") offset: Option[String],
    @ApiParam(value = "Namespace of the response")@QueryParam("namespace") namespace: Option[String]) = Action {
    request =>
      observationData.getPoints(sources) match {
        case Some(observation) => jsonResponse(observation)
        case _                 => jsonResponse(new value.ApiResponse(HttpStatus.NOT_FOUND, "Observation not found"), HttpStatus.NOT_FOUND)
      }
  }
}
