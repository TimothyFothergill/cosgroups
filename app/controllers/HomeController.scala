package controllers

import javax.inject._
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import models.{Cosplay, Cosgroup, User, Password}
import java.util.Date

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  val fakeCosplay = Cosplay(
    characterName = "Captain James Hook",
    username = "Timlah",
    startDate = Some(new Date(2025, 3, 6)),
    endDate = Some(new Date(2025, 10, 1)),
    seriesName = "Hook (1991)",
    budget = 250.00
  )

  val fakeUser = User(
    id = 1,
    username = "Timlah",
    firstName = "Tim",
    lastName = "Fothergill",
    emailAddress = "timlah@timlah.com",
    password= Password("Password123!"),
    cosplays = Seq(
      fakeCosplay
    )
  )

  val fakeCosgroup = Cosgroup(
    groupName = "Butlins Krew 2025",
    users = Seq(fakeUser),
    nextEvent = "Butlins 2025"
  )

  def index() = Action { implicit request: Request[AnyContent] =>
    request.session.get("username") match {
      case Some(username) => {
        println("Logged in?")
        Ok(views.html.partials.dashboard(fakeUser))
      }
      case None => {
        Ok(views.html.index())        
      }
    }
  }

  def register() = Action { implicit request: Request[AnyContent] =>
    request.session.get("username") match {
      case Some(username) => {
        Ok(views.html.partials.dashboard(fakeUser))
      }
      case None => {
        Ok(views.html.partials.register())
      }
    }
  }

  def login() = Action { implicit request: Request[AnyContent] =>
    request.session.get("username") match {
      case Some(username) => {
        Ok(views.html.partials.dashboard(fakeUser))
      }
      case None => {
        Ok(views.html.partials.login())
      }
    }
  }

  def about () = Action { implicit request: Request[AnyContent] => 
    Ok(views.html.partials.about())
  }

  def dashboard() = Action { implicit request: Request[AnyContent] => {
    // request.session.get("username") match {
      // case Some(username) => {
        Ok(views.html.partials.dashboard(fakeUser))
      // }
      // case None => {
        // Ok(views.html.index())
      // }
      // }
    }
  }

  def cosgroup() = Action { implicit request: Request[AnyContent] => {
    // request.session.get("username") match {
      // case Some(username) => {
        Ok(views.html.partials.cosgroup(fakeCosgroup))
      // }
      // case None => {
        // Ok(views.html.index())
      // }
      // }
    }  
  }

  def cosplay(cosplay: String) = Action { implicit request: Request[AnyContent] => {
    // request.session.get("username") match {
      // case Some(username) => {
        Ok(views.html.partials.cosplay(fakeCosplay))
      // }
      // case None => {
        // Ok(views.html.index())
      // }
      // }
    }  
  }



  def profile(user: String) = Action { implicit request: Request[AnyContent] => {
    // request.session.get("username") match {
      // case Some(username) => {
        Ok(views.html.partials.profile(fakeUser))
      // }
      // case None => {
        // Ok(views.html.index())
      // }
      // }
    }  
  }

}
