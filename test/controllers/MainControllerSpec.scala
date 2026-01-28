package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.inject.bind
import play.api.test._
import play.api.test.CSRFTokenHelper._
import play.api.test.Helpers._
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito.when
import scala.concurrent.Future

import services.{CosgroupsRepositoryService, CosplaysRepositoryService, UsersService, UsersRepositoryService}
import actions.UserAction
import models.{Password, User}

class MainControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar with BaseControllerSpec {

  private val mockUsersService: UsersService = {
    val mocked = mock[UsersService]
    when(mocked.lookupUserByUsername("cosplayer"))
      .thenReturn(Future.successful(Some(User(1, "cosplayer", "cosplayer@timlahs.tests.com", Password("~testPassword123~")))))
    mocked
  }

  "MainController GET" should {
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
      val home = controller.login().apply(FakeRequest(GET, "/login").withCSRFToken)

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Login to Cosgroups")
    }

    "render the login page from the router" in {
      val request = FakeRequest(GET, "/login").withCSRFToken
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Login to Cosgroups")
    }
  }
}
