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
import play.api.Logger
import javax.ws.rs.{QueryParam, PathParam}
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core.util.ScalaJsonUtil
import javax.ws.rs.{QueryParam, PathParam}
import models.Observation
import play.api.Logger
import javax.ws.rs.core.Response.Status
import util.HttpStatus
import api.ObservationData

@Api(value = "/observations", description = "API for observations")
class ObservationController extends BaseApiController {
  var observationData = new ObservationData

  @ApiOperation(
    nickname = "getObservationById",
    value = "Find observation by ID",
    notes = "Returns an observation",
    response = classOf[models.Observation],
    httpMethod = "GET" )
  @ApiResponses(Array(
    new ApiResponse(code = HttpStatus.BAD_REQUEST, message = "Invalid ID supplied"),
    new ApiResponse(code = HttpStatus.NOT_FOUND, message = "Observation not found")))
  def getObservationById(
    @ApiParam(value = "ID of the observation to fetch")@PathParam("id") id: String)  = Action {

      request =>
      //Todo handle id is not an int.
      observationData.getObservationbyId(id.toLong) match {
        case Some(observation) => jsonResponse(observation)
        case _ => jsonResponse(new value.ApiResponse(HttpStatus.NOT_FOUND, "Observation not found"))
      }
  }

  @ApiOperation(nickname = "addObservation",
    value = "Add a new observation",
    response = classOf[Void],
    httpMethod = "POST",
    authorizations = Array(new Authorization(value = "oauth2",
      scopes = Array(
        new AuthorizationScope(scope = "test:anything", description = "anything"),
        new AuthorizationScope(scope = "test:nothing", description = "nothing")))))
  @ApiResponses(Array(
    new ApiResponse(code = HttpStatus.METHOD_NOT_ALLOWED, message = "Invalid input")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Observation object that needs to be added to the store",
      required = true, dataType = "Observation", paramType = "body")))
  def addObservation() = Action {
      request =>
      request.body.asJson match {
        case Some(e) => {
          val observation = ScalaJsonUtil.mapper.readValue(e.toString, classOf[Observation]).asInstanceOf[Observation]
          observationData.addObservation(observation)
          Ok
        }
        case None => jsonResponse(new value.ApiResponse(HttpStatus.BAD_REQUEST, "Invalid input"))
      }
  }
}
