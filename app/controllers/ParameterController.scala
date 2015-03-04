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
import models.Parameter
import play.api.Logger
import javax.ws.rs.core.Response.Status
import util.HttpStatus
import api.ParameterData
import scala.concurrent.impl.Future

// scalastyle:off public.methods.have.type

@Api(value = "/parameters", description = "MET API parameters")
class ParameterController extends BaseApiController {
  var parameterData = new ParameterData

  @ApiOperation(
    nickname = "getParameters",
    value = "Retrieve parameter metadata",
    notes = "Retrieves metadata about MET API parameters in JSON format.",
    response = classOf[models.Parameter],
    responseContainer = "List",
    httpMethod = "GET" )
  @ApiResponses(Array(
    new ApiResponse(code = HttpStatus.OK, message = "OK: The request was successful."),
    new ApiResponse(code = HttpStatus.BAD_REQUEST, message = "Bad Request: The request could not be understood or was missing required parameters."),
    new ApiResponse(code = HttpStatus.UNAUTHORIZED, message = "Unauthorized:  authentication failed or user doesn't have permissions for requested operation."),
    new ApiResponse(code = HttpStatus.NOT_FOUND, message = "Not Found: The data that the user requested could not be found."),
    new ApiResponse(code = HttpStatus.SERVICE_UNAVAILABLE, message = "Service Unavailable: The service is temporarily unavailable (e.g., due to scheduled platform maintenance). Try again later.")))
  def getParameters(
    @ApiParam(value = "ID(s) of the MET API parameters. Use station IDs 'air_temperature' and 'precipitation_amount' to return data", allowMultiple = true)@QueryParam("parameters")
      parameters: Option[String],
    @ApiParam(value = "Specify attribute fields in the response (if you don't want the full result)", allowMultiple = true)@QueryParam("fields")
      fields: Option[String],
    @ApiParam(value = "Limit the number of points returned", defaultValue = "1000")@QueryParam("limit")
      limit: Option[String],
    @ApiParam(value = "Offset in the response set", defaultValue = "0")@QueryParam("offset")
      offset: Option[String],
    @ApiParam(value = "Namespace of the response", defaultValue = "en")@QueryParam("namespace")
      namespace:  Option[String]) = Action {
    implicit request => var params = parameterData.getParameters(parameters)
    if (params.size > 0) jsonResponse(params) else jsonResponse(new value.ApiResponse(HttpStatus.NOT_FOUND, "Source/station not found"), HttpStatus.NOT_FOUND)
  }

  @ApiOperation(
    nickname = "getParameterById",
    value = "Retrieve parameter metadata by ID",
    notes = "Retrieves metadata about a single MET API parameter in JSON format.",
    response = classOf[models.Parameter],
    httpMethod = "GET" )
  @ApiResponses(Array(
    new ApiResponse(code = HttpStatus.OK, message = "OK: The request was successful."),
    new ApiResponse(code = HttpStatus.BAD_REQUEST, message = "Bad Request: Invalid parameter ID."),
    new ApiResponse(code = HttpStatus.UNAUTHORIZED, message = "Unauthorized:  authentication failed or user doesn't have permissions for requested operation."),
    new ApiResponse(code = HttpStatus.NOT_FOUND, message = "Not Found: The parameter could not be found."),
    new ApiResponse(code = HttpStatus.SERVICE_UNAVAILABLE, message = "Service Unavailable: The service is temporarily unavailable (e.g., due to scheduled platform maintenance). Try again later.")))
  def getParameterById(
    @ApiParam(value = "ID of the MET parameter to fetch") @PathParam("id")
      id: String) = Action {
  implicit request =>
    parameterData.getParameterById(id) match {
      case Some(parameter) => jsonResponse(parameter)
      case _ => jsonResponse(new value.ApiResponse(HttpStatus.NOT_FOUND, "Parameter not found"), HttpStatus.NOT_FOUND)
    }
  }

  @ApiOperation(nickname = "addParameter",
    value = "Create a new MET API parameter",
    response = classOf[Void],
    httpMethod = "POST")
  @ApiResponses(Array(
    new ApiResponse(code = HttpStatus.OK, message = "OK: The request was successful."),
    new ApiResponse(code = HttpStatus.BAD_REQUEST, message = "Bad Request: Invalid input."),
    new ApiResponse(code = HttpStatus.UNAUTHORIZED, message = "Unauthorized:  authentication failed or user doesn't have permissions for requested operation."),
    new ApiResponse(code = HttpStatus.NOT_FOUND, message = "Not Found: The station could not be found."),
    new ApiResponse(code = HttpStatus.SERVICE_UNAVAILABLE, message = "Service Unavailable: The service is temporarily unavailable (e.g., due to scheduled platform maintenance). Try again later.")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Parameter object that needs to be added to the system", required = true, dataType = "Parameter", paramType = "body")
  ))
  def addParameter() = Action {
    implicit request =>
      request.body.asJson match {
        case Some(e) => {
          val parameter = ScalaJsonUtil.mapper.readValue(e.toString, classOf[Parameter]).asInstanceOf[Parameter]
          parameterData.addParameter(parameter) match {
            case Some(param) => jsonResponse(param)
            case None => jsonResponse(new value.ApiResponse(HttpStatus.BAD_REQUEST, "Duplicate input not allowed on POST"), HttpStatus.BAD_REQUEST)
          }
        }
        case None => jsonResponse(new value.ApiResponse(HttpStatus.BAD_REQUEST, "Invalid input"), HttpStatus.BAD_REQUEST)
      }
  }

  @ApiOperation(nickname = "updateParameter",
    value = "Update an existing MET API parameter",
    response = classOf[Void],
    httpMethod = "PUT")
  @ApiResponses(Array(
    new ApiResponse(code = HttpStatus.OK, message = "OK: The request was successful."),
    new ApiResponse(code = HttpStatus.BAD_REQUEST, message = "Bad Request: Invalid input."),
    new ApiResponse(code = HttpStatus.UNAUTHORIZED, message = "Unauthorized:  authentication failed or user doesn't have permissions for requested operation."),
    new ApiResponse(code = HttpStatus.NOT_FOUND, message = "Not Found: The station could not be found."),
    new ApiResponse(code = HttpStatus.SERVICE_UNAVAILABLE, message = "Service Unavailable: The service is temporarily unavailable (e.g., due to scheduled platform maintenance). Try again later.")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Parameter object that needs to be updated in the system", required = true, dataType = "Parameter", paramType = "body")))
  def updateParameter() = Action {
    implicit request =>
      request.body.asJson match {
        case Some(e) => {
          val parameter = ScalaJsonUtil.mapper.readValue(e.toString, classOf[Parameter]).asInstanceOf[Parameter]
          parameterData.updateParameter(parameter) match {
            case Some(param) => jsonResponse(param)
            case None => jsonResponse(new value.ApiResponse(HttpStatus.BAD_REQUEST, "No existing parameter to update"), HttpStatus.BAD_REQUEST)
          }
        }
        case None => jsonResponse(new value.ApiResponse(HttpStatus.BAD_REQUEST, "Invalid input"), HttpStatus.BAD_REQUEST)
      }
  }
}
