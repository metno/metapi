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

import com.wordnik.swagger.core.util.ScalaJsonUtil
import com.fasterxml.jackson.annotation.JsonInclude
import play.api.mvc._
import java.io.StringWriter
import value._
import util.HttpStatus

class BaseApiController extends Controller {

  // APIs
  protected def jsonResponse(data: Object) = {
    val jsonValue: String = toJsonString(data)
    new Result(header = ResponseHeader(HttpStatus.OK), body = play.api.libs.iteratee.Enumerator(jsonValue.getBytes())).as("application/json")
      .withHeaders(
        ("Access-Control-Allow-Origin", "*"),
        ("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT"),
        ("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization"))
  }

  protected def jsonResponse(data: Object, code: Int) = {
    val jsonValue: String = toJsonString(data)
    new Result(header = ResponseHeader(code), body = play.api.libs.iteratee.Enumerator(jsonValue.getBytes())).as("application/json")
      .withHeaders(
        ("Access-Control-Allow-Origin", "*"),
        ("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT"),
        ("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization"))
  }

  def toJsonString(data: Any): String = {
    if (data.getClass.equals(classOf[String])) {
      data.asInstanceOf[String]
    } else {
      //ScalaJsonUtil.mapper.writeValueAsString(data)
      val m = ScalaJsonUtil.mapper
      m.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
      m.writeValueAsString(data)
    }
  }
}
