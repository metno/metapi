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
class PlaceSpec extends Specification {

  "Application" should {

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
