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
import models.Place
import play.api.Logger
import javax.ws.rs.core.Response.Status
import util.HttpStatus
import api.PlaceData
import scala.concurrent.impl.Future

// scalastyle:off public.methods.have.type

@Api(value = "/places", description = "MET API places")
class PlaceController extends BaseApiController {
  var placeData = new PlaceData

  @ApiOperation(
    nickname = "getPlaces",
    value = "Retrieve place metadata",
    notes = "Retrieves metadata about MET API places in JSON format.",
    response = classOf[models.Place],
    responseContainer = "List",
    httpMethod = "GET" )
  @ApiResponses(Array(
    new ApiResponse(code = HttpStatus.OK, message = "OK: The request was successful."),
    new ApiResponse(code = HttpStatus.BAD_REQUEST, message = "Bad Request: The request could not be understood or was missing required parameters."),
    new ApiResponse(code = HttpStatus.UNAUTHORIZED, message = "Unauthorized:  authentication failed or user doesn't have permissions for requested operation."),
    new ApiResponse(code = HttpStatus.NOT_FOUND, message = "Not Found: The data that the user requested could not be found."),
    new ApiResponse(code = HttpStatus.SERVICE_UNAVAILABLE, message = "Service Unavailable: The service is temporarily unavailable (e.g., due to scheduled platform maintenance). Try again later.")))
  def getPlaces(
    @ApiParam(value = "ID(s) or spatial locations of the MET API places you're looking for. Use place ID 'norway_oslo_oslo_blindern' to return data", allowMultiple = true)@QueryParam("places")
      places: Option[String],
    @ApiParam(value = "Specify attribute fields in the response (if you don't want the full result)", allowMultiple = true)@QueryParam("fields")
      fields: Option[String],
    @ApiParam(value = "Limit the number of points returned", defaultValue = "1000")@QueryParam("limit")
      limit: Option[String],
    @ApiParam(value = "Offset in the response set", defaultValue = "0")@QueryParam("offset")
      offset: Option[String],
    @ApiParam(value = "Namespace of the response", defaultValue = "en")@QueryParam("namespace")
      namespace:  Option[String]) = Action {
    implicit request => var pls = placeData.getPlaces(places)
    if (pls.size > 0) jsonResponse(pls) else jsonResponse(new value.ApiResponse(HttpStatus.NOT_FOUND, "MET API place not found"), HttpStatus.NOT_FOUND)
  }

  @ApiOperation(
    nickname = "getPlaceById",
    value = "Retrieve place metadata by ID",
    notes = "Retrieves metadata about a single MET API place in JSON format.",
    response = classOf[models.Place],
    httpMethod = "GET" )
  @ApiResponses(Array(
    new ApiResponse(code = HttpStatus.OK, message = "OK: The request was successful."),
    new ApiResponse(code = HttpStatus.BAD_REQUEST, message = "Bad Request: Invalid parameter ID."),
    new ApiResponse(code = HttpStatus.UNAUTHORIZED, message = "Unauthorized:  authentication failed or user doesn't have permissions for requested operation."),
    new ApiResponse(code = HttpStatus.NOT_FOUND, message = "Not Found: The parameter could not be found."),
    new ApiResponse(code = HttpStatus.SERVICE_UNAVAILABLE, message = "Service Unavailable: The service is temporarily unavailable (e.g., due to scheduled platform maintenance). Try again later.")))
  def getPlaceById(
    @ApiParam(value = "ID of the MET place to fetch") @PathParam("id")
      id: String) = Action {
  implicit request =>
    placeData.getPlaceById(id) match {
      case Some(place) => jsonResponse(place)
      case _ => jsonResponse(new value.ApiResponse(HttpStatus.NOT_FOUND, "Parameter not found"), HttpStatus.NOT_FOUND)
    }
  }

  @ApiOperation(nickname = "addPlace",
    value = "Create a new MET API place",
    response = classOf[Void],
    httpMethod = "POST")
  @ApiResponses(Array(
    new ApiResponse(code = HttpStatus.OK, message = "OK: The request was successful."),
    new ApiResponse(code = HttpStatus.BAD_REQUEST, message = "Bad Request: Invalid input."),
    new ApiResponse(code = HttpStatus.UNAUTHORIZED, message = "Unauthorized:  authentication failed or user doesn't have permissions for requested operation."),
    new ApiResponse(code = HttpStatus.NOT_FOUND, message = "Not Found: The station could not be found."),
    new ApiResponse(code = HttpStatus.SERVICE_UNAVAILABLE, message = "Service Unavailable: The service is temporarily unavailable (e.g., due to scheduled platform maintenance). Try again later.")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Place object that is to be added to the system", required = true, dataType = "Place", paramType = "body")
  ))
  def addPlace() = Action {
    implicit request =>
      request.body.asJson match {
        case Some(e) => {
          val place = ScalaJsonUtil.mapper.readValue(e.toString, classOf[Place]).asInstanceOf[Place]
          placeData.addPlace(place) match {
            case Some(pl) => jsonResponse(pl)
            case None => jsonResponse(new value.ApiResponse(HttpStatus.BAD_REQUEST, "Duplicate input not allowed on POST"), HttpStatus.BAD_REQUEST)
          }
        }
        case None => jsonResponse(new value.ApiResponse(HttpStatus.BAD_REQUEST, "Invalid input"), HttpStatus.BAD_REQUEST)
      }
  }

  @ApiOperation(nickname = "updatePlace",
    value = "Update an existing MET API place",
    response = classOf[Void],
    httpMethod = "PUT")
  @ApiResponses(Array(
    new ApiResponse(code = HttpStatus.OK, message = "OK: The request was successful."),
    new ApiResponse(code = HttpStatus.BAD_REQUEST, message = "Bad Request: Invalid input."),
    new ApiResponse(code = HttpStatus.UNAUTHORIZED, message = "Unauthorized:  authentication failed or user doesn't have permissions for requested operation."),
    new ApiResponse(code = HttpStatus.NOT_FOUND, message = "Not Found: The station could not be found."),
    new ApiResponse(code = HttpStatus.SERVICE_UNAVAILABLE, message = "Service Unavailable: The service is temporarily unavailable (e.g., due to scheduled platform maintenance). Try again later.")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Place object that needs to be updated in the system", required = true, dataType = "Place", paramType = "body")))
  def updatePlace() = Action {
    implicit request =>
      request.body.asJson match {
        case Some(e) => {
          val place = ScalaJsonUtil.mapper.readValue(e.toString, classOf[Place]).asInstanceOf[Place]
          placeData.updatePlace(place) match {
            case Some(pl) => jsonResponse(pl)
            case None => jsonResponse(new value.ApiResponse(HttpStatus.BAD_REQUEST, "No existing parameter to update"), HttpStatus.BAD_REQUEST)
          }
        }
        case None => jsonResponse(new value.ApiResponse(HttpStatus.BAD_REQUEST, "Invalid input"), HttpStatus.BAD_REQUEST)
      }
  }
}
