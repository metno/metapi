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
// Using val for "hello" messes up swagger-ui

@Api(value = "/tests", description = "Test Resources")
object TestController extends Controller {
  @ApiOperation(
    produces = "text/plain",
    nickname = "hello",
    value = "Say hello to the API",
    response = classOf[String],
    httpMethod = "GET")
  def hello = Action {
    req =>
      Ok("Hello to you too!\n")
  }

  @ApiOperation(
    produces = "text/plain",
    nickname = "secureHello",
    value = "Say hello to the API, securely",
    response = classOf[String],
    httpMethod = "GET")
  def secureHello = no.met.security.AuthorizedAction {
    req =>
      {
        val authHeader = req.headers.get("Authorization")
        if (authHeader.get.startsWith("Bearer ")) {
          Ok("Hello to you too, very securely!\n")
        } else {
          Ok("Hello to you too, securely!\n")
        }
      }
  }

}
