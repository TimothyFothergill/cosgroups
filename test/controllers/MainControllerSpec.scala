package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class MainControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "MainController GET" should {

    "render the index page from a new instance of controller" in {
      val controller = new MainController(stubControllerComponents())
      val home = controller.index().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Cosgroups")
    }

    "render the index page from the application" in {
      val controller = inject[MainController]
      val home = controller.index().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Cosgroups")
    }

    "render the index page from the router" in {
      val request = FakeRequest(GET, "/")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Cosgroups")
    }

    "render the about page from the application" in {
      val controller = inject[MainController]
      val home = controller.about().apply(FakeRequest(GET, "/about"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("About Cosgroups")
    }

    "render the about page from the router" in {
      val request = FakeRequest(GET, "/about")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("About Cosgroups")
    }

    "render the login page from the application" in {
      val controller = inject[MainController]
      val home = controller.login().apply(FakeRequest(GET, "/login"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Login to Cosgroups")
    }

    "render the login page from the router" in {
      val request = FakeRequest(GET, "/login")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Login to Cosgroups")
    }
  }
}
