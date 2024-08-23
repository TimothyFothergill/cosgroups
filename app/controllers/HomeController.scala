package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models.User

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
  def register() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.register())
  }
  def login() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.partials.login())
  }
  def dashboard(user: User) = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.register())
  }
}
