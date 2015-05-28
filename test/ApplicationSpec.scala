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
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import com.google.common.io.BaseEncoding
import no.met.security.Authorization

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/boom")) must beNone
    }

    "reroute to index.html from root" in new WithApplication {
      val ret = route(FakeRequest(GET, "/")).get

      status(ret) must equalTo(SEE_OTHER)
      redirectLocation(ret) must beSome.which(_ == "/index.html")
    }

    "render the index page" in new WithApplication {
      val ret = route(FakeRequest(GET, "/index.html")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "text/html")
      contentAsString(ret) must contain("data.met.no")
    }

    "render swagger JSON" in new WithApplication {
      val ret = route(FakeRequest(GET, "/api-docs")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
      contentAsString(ret) must contain("swaggerVersion")
    }

    "render swagger-UI" in new WithApplication {
      val ret = route(FakeRequest(GET, "/docs")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "text/html")
    }

    "return 'hello' response" in new WithApplication {
      val home = route(FakeRequest(GET, "/tests/hello")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/plain")
      contentAsString(home) must contain("Hello")
    }

    "return 'secureHello' response" in running(TestUtil.app) {
        val credentials = Authorization.newClient("someone@met.no")
        val clientId = credentials.id
        val encoded = BaseEncoding.base64Url().encode(s"$clientId:".getBytes("UTF-8"))
        val headers = FakeHeaders(List("Authorization" -> List(s"Basic $encoded")))
        val secret = route(FakeRequest(GET, "/tests/secureHello", headers, "")).get
        status(secret) must equalTo(OK)
        contentType(secret) must beSome.which(_ == "text/plain")
        contentAsString(secret) must contain("Hello to you too, securely!")
      }


  }

}
