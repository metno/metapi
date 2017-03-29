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
import play.api.mvc.AnyContentAsFormUrlEncoded
import com.google.common.io.BaseEncoding
import no.met.security.Authorization

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

    def getAccessToken(user: String = "someone@met.no"): String = {
      val client = Authorization.newClient(user)
      val body = AnyContentAsFormUrlEncoded(
        Map("grant_type" -> List("client_credentials"),
          "client_id" -> List(client.id),
          "client_secret" -> List(client.secret)))

      val result = route(FakeRequest(POST, "/auth/requestAccessToken").withBody(body)).get
      status(result) must equalTo(OK)
      val contents = contentAsJson(result)
      val accessToken = contents \ "access_token"
      accessToken.as[String]
    }


  "Application" should {

    //"send 404 on a bad request" in new WithApplication {
    //  route(FakeRequest(GET, "/boom")) must beNone
    //}

    "reroute to index.html from root" in running(TestUtil.app) {
      val ret = route(FakeRequest(GET, "/")).get

      status(ret) must equalTo(SEE_OTHER)
      redirectLocation(ret) must beSome.which(_ == "/index.html")
    }

    "render the index page" in running(TestUtil.app) {
      val ret = route(FakeRequest(GET, "/index.html")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "text/html")
      contentAsString(ret) must contain("data.met.no")
    }

    "render concepts" in running(TestUtil.app) {
      val ret = route(FakeRequest(GET, "/concepts")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "text/html")
    }

    "render concepts/index.html" in running(TestUtil.app) {
      val ret = route(FakeRequest(GET, "/concepts/index.html")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "text/html")
    }

    "render swagger JSON" in running(TestUtil.app) {
      val ret = route(FakeRequest(GET, "/swagger.json")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
      contentAsString(ret) must contain("swagger")
    }

    "render swagger-UI" in running(TestUtil.app) {
      val ret = route(FakeRequest(GET, "/reference")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "text/html")
    }

    "render reference" in running(TestUtil.app) {
      val ret = route(FakeRequest(GET, "/reference")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "text/html")
    }

    "render reference/index.html" in running(TestUtil.app) {
      val ret = route(FakeRequest(GET, "/reference/index.html")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "text/html")
    }

    "render schema" in running(TestUtil.app) {
      val ret = route(FakeRequest(GET, "/schema")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "text/html")
    }

    "render schema/index.html" in running(TestUtil.app) {
      val ret = route(FakeRequest(GET, "/schema/index.html")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "text/html")
    }

    "render termsofuse" in running(TestUtil.app) {
      val ret = route(FakeRequest(GET, "/termsofuse")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "text/html")
    }

    "render termsofuse/index.html" in running(TestUtil.app) {
      val ret = route(FakeRequest(GET, "/termsofuse/index.html")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "text/html")
    }

    "render examples" in running(TestUtil.app) {
      val ret = route(FakeRequest(GET, "/examples")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "text/html")
    }

    "render examples/index.html" in running(TestUtil.app) {
      val ret = route(FakeRequest(GET, "/examples/index.html")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "text/html")
    }

    "return 'hello' response" in running(TestUtil.app) {
      val home = route(FakeRequest(GET, "/tests/hello")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/plain")
      contentAsString(home) must contain("Hello")
    }

    "return 'secureHello' response for basic auth" in running(TestUtil.app) {
      val credentials = Authorization.newClient("someone@met.no")
      val clientId = credentials.id
      val encoded = BaseEncoding.base64Url().encode(s"$clientId:".getBytes("UTF-8"))
      val headers = FakeHeaders(List("Authorization" -> s"Basic $encoded"))
      val secret = route(FakeRequest(GET, "/tests/secureHello", headers, "")).get
      status(secret) must equalTo(OK)
      contentType(secret) must beSome.which(_ == "text/plain")
      contentAsString(secret) must contain("Hello to you too, securely!")
    }

    "return 'secureHello' response for oauth2" in running(TestUtil.app) {
      val userToken = getAccessToken()
      val headers = FakeHeaders(List("Authorization" -> s"Bearer $userToken"))
      val secret = route(FakeRequest(GET, "/tests/secureHello", headers, "")).get
      status(secret) must equalTo(OK)
      contentType(secret) must beSome.which(_ == "text/plain")
      contentAsString(secret) must contain("Hello to you too, very securely!")
    }

  }

}
