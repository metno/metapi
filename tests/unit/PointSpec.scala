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
class PointSpec extends Specification {

  "Application" should {

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

  }

}
