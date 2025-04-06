package controllers

import javax.inject._
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import models.{User, Password}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  val fakeUser = User(
    id = 1,
    username = "Timlah",
    firstName = "Tim",
    lastName = "Fothergill",
    emailAddress = "timlah@timlah.com",
    password= Password("Password123!"),
    cosplays = Seq()
  )

  def index() = Action { implicit request: Request[AnyContent] =>
    request.session.get("username") match {
      case Some(username) => {
        println("Logged in?")
        Ok(views.html.index())
      }
      case None => {
        Ok(views.html.index())        
      }
    }
  }

  def register() = Action { implicit request: Request[AnyContent] =>
    request.session.get("username") match {
      case Some(username) => {
        Ok(views.html.partials.register())
      }
      case None => {
        Ok(views.html.partials.register())
      }
    }
  }

  def login() = Action { implicit request: Request[AnyContent] =>
    request.session.get("username") match {
      case Some(username) => {
        Ok(views.html.partials.login())
      }
      case None => {
        Ok(views.html.partials.login())
      }
    }
  }

  def about () = Action { implicit request: Request[AnyContent] => 
    request.session.get("username") match {
      case Some(username) => {
        Ok(views.html.about())
      }
      case None => {
        Ok(views.html.about())
      }
    }
  }

  def dashboard() = Action { implicit request: Request[AnyContent] => {
    request.session.get("username") match {
      case Some(username) => {
        Ok(views.html.partials.dashboard(fakeUser))
      }
      case None => {
        Ok(views.html.partials.dashboard(fakeUser))
      }
      }
    }
  }

}
