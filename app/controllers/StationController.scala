/*
    MET-API

    Copyright (C) 2015 met.no
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
import javax.ws.rs.{QueryParam, PathParam}
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core.util.ScalaJsonUtil
import javax.ws.rs.{QueryParam, PathParam}
import models.Point
import play.api.Logger
import javax.ws.rs.core.Response.Status
import util.HttpStatus
import api.StationData
import scala.concurrent.impl.Future

// scalastyle:off public.methods.have.type

@Api(value = "/sourceStations", description = "MHO Station Source information")
class StationController extends BaseApiController {
  var stationData = new StationData

  @ApiOperation(
    nickname = "getStations",
    value = "Retrieve source station data",
    notes = "Retrieves source data about MHO stations from the API in JSON format.",
    response = classOf[models.Station],
    responseContainer = "List",
    httpMethod = "GET" )
  @ApiResponses(Array(
    new ApiResponse(code = HttpStatus.OK, message = "OK: The request was successful."),
    new ApiResponse(code = HttpStatus.BAD_REQUEST, message = "Bad Request: The request could not be understood or was missing required parameters."),
    new ApiResponse(code = HttpStatus.UNAUTHORIZED, message = "Unauthorized:  authentication failed or user doesn't have permissions for requested operation."),
    new ApiResponse(code = HttpStatus.NOT_FOUND, message = "Not Found: The data that the user requested could not be found."),
    new ApiResponse(code = HttpStatus.SERVICE_UNAVAILABLE, message = "Service Unavailable: The service is temporarily unavailable (e.g., due to scheduled platform maintenance). Try again later.")))
  def getStations(
    @ApiParam(value = "ID(s) of the data source/station. Use station IDs 'KS18700' and 'KS18800' to return data", allowMultiple = true)@QueryParam("sources")
      sources: Option[String],
    @ApiParam(value = "Spatial location in WKT of the data source/station")@QueryParam("places")
      places: Option[String],
    @ApiParam(value = "Valid time of the data source/station")@QueryParam("validtime")
      validtime: Option[String],
    @ApiParam(value = "MET Parameters", allowMultiple = true)@QueryParam("parameters")
      parameters: Option[String],
    @ApiParam(value = "Time Series Type", allowMultiple = true)@QueryParam("timeSeriesTypes")
      timeSeriesTypes: Option[String],
    @ApiParam(value = "Specify attribute fields in the response (if you don't want the full result)", allowMultiple = true)@QueryParam("fields")
      fields: Option[String],
    @ApiParam(value = "Limit the number of points returned", defaultValue = "1000")@QueryParam("limit")
      limit: Option[String],
    @ApiParam(value = "Offset in the response set", defaultValue = "0")@QueryParam("offset")
      offset: Option[String],
    @ApiParam(value = "Namespace of the response", defaultValue = "en")@QueryParam("namespace")
      namespace:  Option[String]) = Action {
    implicit request => var stations = stationData.getStations(sources)
    if (stations.size > 0) jsonResponse(stations) else jsonResponse(new value.ApiResponse(HttpStatus.NOT_FOUND, "Source/station not found"), HttpStatus.NOT_FOUND)
  }

  @ApiOperation(
    nickname = "getStationById",
    value = "Retrieve source station data by ID",
    notes = "Retrieves source data of a single MHO station from the API in JSON format.",
    response = classOf[models.Station],
    httpMethod = "GET" )
  @ApiResponses(Array(
    new ApiResponse(code = HttpStatus.OK, message = "OK: The request was successful."),
    new ApiResponse(code = HttpStatus.BAD_REQUEST, message = "Bad Request: Invalid station ID."),
    new ApiResponse(code = HttpStatus.UNAUTHORIZED, message = "Unauthorized:  authentication failed or user doesn't have permissions for requested operation."),
    new ApiResponse(code = HttpStatus.NOT_FOUND, message = "Not Found: The station could not be found."),
    new ApiResponse(code = HttpStatus.SERVICE_UNAVAILABLE, message = "Service Unavailable: The service is temporarily unavailable (e.g., due to scheduled platform maintenance). Try again later.")))
  def getStationsById(
    @ApiParam(value = "ID of the station to fetch") @PathParam("id")
      id: String) = Action {
  implicit request =>
    stationData.getStationById(id) match {
      case Some(station) => jsonResponse(station)
      case _ => jsonResponse(new value.ApiResponse(HttpStatus.NOT_FOUND, "Source/station not found"), HttpStatus.NOT_FOUND)
    }
  }

}
