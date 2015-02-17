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

    "render 'Hello World'" in new WithApplication{
      val home = route(FakeRequest(GET, "/v0/helloWorld")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/plain")
      contentAsString(home) must contain ("Hello World")
    }

    "Detect output format 'json'" in new WithApplication{
      val home = route(FakeRequest(GET, "/v0/observations/1")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "application/json")

    }

  }
}
