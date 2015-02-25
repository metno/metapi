import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

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

    "returns 'bad request' if no source is defined in points API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/points")).get

      status(ret) must equalTo(BAD_REQUEST)
    }

    "returns 'not found' when an unavailable numerical source is defined in points API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/points?sources=999")).get

      status(ret) must equalTo(NOT_FOUND)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "returns 'not found' when an invalid string source is defined in points API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/points?sources=mock")).get

      status(ret) must equalTo(NOT_FOUND)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "returns valid json when a valid source is defined in points API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/points?sources=1")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

    "returns valid json with many parameters defined in points API" in new WithApplication{
      val ret = route(FakeRequest(GET, "/v0/points?sources=1&places=oslo&reftime=2015-01-01T00:00")).get

      status(ret) must equalTo(OK)
      contentType(ret) must beSome.which(_ == "application/json")
    }

  }
}
