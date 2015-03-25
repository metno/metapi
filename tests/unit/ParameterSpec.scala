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
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._

@RunWith(classOf[JUnitRunner])
class ParameterSpec extends Specification {

  "Application" should {

    "return valid json if no source is defined in parameters API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/parameters")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return 'not found' when no data can be returned parameters API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/parameters?parameters=abcdef")).get

      status(ret) must equalTo(NOT_FOUND)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid json when a valid parameter is defined in parameters API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/parameters?parameters=air_temperature")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid json when a valid list of sources is defined in parameters API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/parameters?parameters=air_temperature,precipitation_amount")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid 'json' when fetching a specific parameter by ID" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/parameters/air_temperature")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return 'not found' if specified parameter by ID does not exist" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/parameters/AB1234")).get

      status(ret) must equalTo(NOT_FOUND)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return 'ok' when adding a new parameter" in new WithApplication{
      val json : JsValue = Json.parse("""{"id": "air_temperature_max", "name": "Max Air Temperature", "unit": "K", "description": "A description"}""")
      val Some(ret) = route(FakeRequest(POST, "/v0/parameters").withJsonBody(json))

      status(ret) must equalTo(OK)
      contentType(ret) must beSome("application/json")
    }

    "return 'bad request' when adding a parameter that already exists" in new WithApplication{
      val json : JsValue = Json.parse("""{"id": "air_temperature", "name": "Air Temperature", "unit": "K", "description": "A description"}""")
      val Some(ret) = route(FakeRequest(POST, "/v0/parameters").withJsonBody(json))

      status(ret) must equalTo(BAD_REQUEST)
      contentType(ret) must beSome("application/json")
    }

    "return 'bad request' when adding a parameter without a body" in new WithApplication{
      val Some(ret) = route(FakeRequest(POST, "/v0/parameters"))

      status(ret) must equalTo(BAD_REQUEST)
      contentType(ret) must beSome("application/json")
    }

    "return 'ok' when updating a parameter" in new WithApplication{
      val json : JsValue = Json.parse("""{"id": "air_temperature", "name": "Air Temperature", "unit": "K", "description": "A description"}""")
      val Some(ret) = route(FakeRequest(PUT, "/v0/parameters").withJsonBody(json))

      status(ret) must equalTo(OK)
      contentType(ret) must beSome("application/json")
    }

    "return 'bad_request' when updating a parameter that does not exist" in new WithApplication{
      val json : JsValue = Json.parse("""{"id": "air_temperature_min", "name": "Min Air Temperature", "unit": "K", "description": "A description"}""")
      val Some(ret) = route(FakeRequest(PUT, "/v0/parameters").withJsonBody(json))

      status(ret) must equalTo(BAD_REQUEST)
      contentType(ret) must beSome("application/json")
    }

    "return 'bad request' when updating a parameter without a body" in new WithApplication{
      val Some(ret) = route(FakeRequest(PUT, "/v0/parameters"))

      status(ret) must equalTo(BAD_REQUEST)
      contentType(ret) must beSome("application/json")
    }

  }

}