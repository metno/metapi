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

import play.api._
import play.api.mvc._
import com.wordnik.swagger.annotations._

//scalastyle:off public.methods.have.type

@Api(value = "/v1/helloWorld", description = "The \"Hello World\" API 1.0")
object HelloWorldController extends Controller {
  @ApiOperation(
    produces = "text/plain",
    nickname = "helloWorld",
    value = "Get a nice greeting",
    notes = "Version 1",
    response = classOf[String],
    httpMethod = "GET")
  def helloWorld = Action {
    req =>
      Ok("Hello World and all you colleagues!")
  }
}

@Api(value = "/v2/helloWorld", description = "The \"Hello World\" API 2.0")
object HelloWorldController_v2 extends Controller {
  @ApiOperation(
    produces = "text/plain",
    nickname = "helloWorld",
    value = "Get a nice greeting",
    notes = "Version 2",
    response = classOf[String],
    httpMethod = "GET")
  def helloWorld = Action {
    req =>
      Ok("Hello World!")
  }
}
