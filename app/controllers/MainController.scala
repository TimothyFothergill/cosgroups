package controllers

import javax.inject.{Inject, Singleton}
import scala.concurrent.{Await, ExecutionContext}
import play.api.*
import play.api.mvc.*

import actions.*
import models.{Password,User}
import models.forms.*
import repositories.*
import services.UsersRepositoryService

import java.time.LocalDate
import scala.concurrent.Future

@Singleton
class MainController @Inject(
    cc: MessagesControllerComponents,
    userAction: UserAction,
    usersRepositoryService: UsersRepositoryService
)(implicit executionContext: ExecutionContext) extends MessagesAbstractController(cc) {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.pages.Index())
  }

  def about() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.pages.About())
  }

  def login() = Action { implicit request: MessagesRequest[AnyContent] =>
    val boundForm = LoginForm.loginForm
    Ok(views.html.pages.Login(boundForm))
  }

  def loginPost() = Action { implicit request: MessagesRequest[AnyContent] =>
    val boundForm = LoginForm.loginForm.bindFromRequest()
    boundForm.fold(
      formWithErrors => {
        BadRequest(views.html.pages.Login(formWithErrors))
      },
      loginData => {
        val maybeUser = User.checkCredentials(loginData.username, loginData.password)
        if(maybeUser.nonEmpty) {
          Redirect(routes.MainController.index())
            .withSession(
              request.session + ("username" -> loginData.username)
            )
        } else {
          Redirect(routes.MainController.login())
        }
      }
    )
  }

  def loggedInPage() = userAction.async { implicit request: UserRequest[AnyContent] => 
    request.username match {
      case Some(name) => {
        usersRepositoryService.returnAllUsers().map { users =>
          Ok(users.toString())
        }
      }
      case None => {
        Future.successful(Unauthorized("Go log in..."))
      }
    }
  }

  def findUserPage() = userAction.async { implicit request: UserRequest[AnyContent] => 
    request.username match {
      case Some(name) => {
        usersRepositoryService.returnUserByUsername("timlahthesecond").map { user =>
          Ok(user.toString())
        }
      }
      case None => {
        Future.successful(Unauthorized("Not today"))
      }
    }
  }

  def updateUserPage() = userAction.async { implicit request: UserRequest[AnyContent] => 
    request.username match {
      case Some(name) => {
        usersRepositoryService.returnUserByUsername("timlahthesecond").map { user =>
          val currentUser = user.get
          val updatedUser: UsersRepositoryModel = UsersRepositoryModel(
            currentUser.id,
            currentUser.username,
            currentUser.email,
            currentUser.password,
            currentUser.isAdmin,
            Some("This is a biography that has been updated."),
            currentUser.dateOfBirth,
            currentUser.registrationDate,
            currentUser.lastActive
          )
          usersRepositoryService.updateUser(updatedUser)
          Redirect(routes.MainController.loggedInPage())
        }
      }
      case None => {
        Future.successful(Unauthorized("Not today"))
      }
    }
  }

  def dropUserPage() = userAction.async { implicit request: UserRequest[AnyContent] => 
    request.username match {
      case Some(name) => {
        usersRepositoryService.returnUserById(3).map { userToDrop =>
          usersRepositoryService.dropUser(3)
          Redirect(routes.MainController.loggedInPage())
        }
      }
      case None => {
        Future.successful(Unauthorized("Not today"))
      }
    }
  }

  def register() = Action { implicit request: MessagesRequest[AnyContent] =>
    val boundForm = RegistrationForm.registrationForm
    Ok(views.html.pages.Register(boundForm))
  }

  def registerPost() = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val boundForm = RegistrationForm.registrationForm.bindFromRequest()
    boundForm.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.pages.Register(formWithErrors)))
      },
      registrationData => {
        val doesUserExist = User.lookupUserByUsername(registrationData.username)
        if(doesUserExist.nonEmpty) {
          Future.successful(Redirect(routes.MainController.register()))
        } else {
          val passwordObj: Password = Password.createPassword(registrationData.password)
          val newUser = UsersRepositoryInsertModel(
            username          = registrationData.username, 
            email             = registrationData.email, 
            password          = passwordObj.hashedPassword,
            isAdmin           = false,
            biography         = None,
            dateOfBirth       = None,
            registrationDate  = LocalDate.now()
          )
          usersRepositoryService.addNewUser(newUser)
          Future.successful(Redirect(routes.MainController.index()))
        }
      }
    )
  }

  def logout() = Action {
    Redirect(routes.MainController.login()).withNewSession
  }
}
