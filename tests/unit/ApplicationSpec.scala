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

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boom")) must beNone
    }

    "render the index page" in new WithApplication{
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/plain")
      contentAsString(home) must contain ("METAPI: Needs Version")
    }

    "render swagger-UI" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/swagger-ui")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "text/html")
    }

    "render 'Hello World'" in new WithApplication{
      val home = route(FakeRequest(GET, "/v0/helloWorld")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/plain")
      contentAsString(home) must contain ("Hello World")
    }

    "return 'bad request' if no source is defined in points API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/points")).get

      status(ret) must equalTo(BAD_REQUEST)
    }

    "return 'not found' when no data can be returned points API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/points?sources=abcdef")).get

      status(ret) must equalTo(NOT_FOUND)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid json when a valid source is defined in points API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/points?sources=KS18700")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid json when a valid list of sources is defined in points API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/points?sources=KS18700,KS18800")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid json with many parameters defined in points API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/points?sources=KS18700&places=oslo&reftime=2015-01-01T00:00")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid json if no source is defined in sourceStations API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/sourceStations")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return 'not found' when no data can be returned by sourceStations API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/sourceStations?sources=abcdef")).get

      status(ret) must equalTo(NOT_FOUND)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid json when a valid source is defined in sourceStations API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/sourceStations?sources=KS18700")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid json when a valid list of sources is defined in sourceStations API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/sourceStations?sources=KS18700,KS18800")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid json with many parameters defined in sourceStations API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/sourceStations?sources=KS18700&places=oslo&parameters=2015-01-01T00:00")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid 'json' when fetching a specific sourceStation by ID" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/sourceStations/KS18700")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")

    }

    "return 'not found' if specified sourceStation by ID does not exist" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/sourceStations/AB1234")).get

      status(ret) must equalTo(NOT_FOUND)
      contentType(ret) must beSome.which(_ == "application/json")

    }

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

    "return valid json if no source is defined in places API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/places")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return 'not found' when no data can be returned places API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/places?places=abcdef")).get

      status(ret) must equalTo(NOT_FOUND)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid json when a valid place is defined in places API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/places?places=norway_oslo_oslo_blindern")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid json when a valid list of sources is defined in places API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/places?places=norway_oslo_oslo_blindern,norway_sør-trøndelag_trondheim_trondheim")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return valid 'json' when fetching a specific place by ID" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/places/norway_oslo_oslo_blindern")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return 'not found' if specified place by ID does not exist" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/places/AB1234")).get

      status(ret) must equalTo(NOT_FOUND)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "return 'ok' when adding a new place" in new WithApplication{
      val json : JsValue = Json.parse("""{"id":"norway_oslo_oslo_ciens","name":"Ciens","place":"POINT(10.72307 59.94062)","municipalityId":"Oslo","countyId":"Oslo","countryId":"NO","featureId":"Part of a city"}""")
      val Some(ret) = route(FakeRequest(POST, "/v0/places").withJsonBody(json))

      status(ret) must equalTo(OK)
      contentType(ret) must beSome("application/json")
    }

    "return 'bad request' when adding a place that already exists" in new WithApplication{
      val json : JsValue = Json.parse("""{"id":"norway_oslo_oslo_blindern","name":"Blindern","place":"POINT(10.72307 59.94063)","municipalityId":"Oslo","countyId":"Oslo","countryId":"NO","featureId":"Part of a city"}""")
      val Some(ret) = route(FakeRequest(POST, "/v0/places").withJsonBody(json))

      status(ret) must equalTo(BAD_REQUEST)
      contentType(ret) must beSome("application/json")
    }

    "return 'bad request' when adding a place without a body" in new WithApplication{
      val Some(ret) = route(FakeRequest(POST, "/v0/places"))

      status(ret) must equalTo(BAD_REQUEST)
      contentType(ret) must beSome("application/json")
    }

    "return 'ok' when updating a place" in new WithApplication{
      val json : JsValue = Json.parse("""{"id":"norway_oslo_oslo_blindern","name":"Oslo - Blindern","place":"POINT(10.72307 59.94063)","municipalityId":"Oslo","countyId":"Oslo","countryId":"NO","featureId":"Part of a city"}""")
      val Some(ret) = route(FakeRequest(PUT, "/v0/places").withJsonBody(json))

      status(ret) must equalTo(OK)
      contentType(ret) must beSome("application/json")
    }

    "return 'bad_request' when updating a place that does not exist" in new WithApplication{
      val json : JsValue = Json.parse("""{"id":"norway_oslo_oslo_oslo-blindern","name":"Blindern","place":"POINT(10.72307 59.94063)","municipalityId":"Oslo","countyId":"Oslo","countryId":"NO","featureId":"Part of a city"}""")
      val Some(ret) = route(FakeRequest(PUT, "/v0/places").withJsonBody(json))

      status(ret) must equalTo(BAD_REQUEST)
      contentType(ret) must beSome("application/json")
    }

    "return 'bad request' when updating a place without a body" in new WithApplication{
      val Some(ret) = route(FakeRequest(PUT, "/v0/places"))

      status(ret) must equalTo(BAD_REQUEST)
      contentType(ret) must beSome("application/json")
    }




  }
}
