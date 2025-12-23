package controllers

import javax.inject.{Inject, Singleton}
import scala.concurrent.{Await, ExecutionContext}
import play.api.*
import play.api.mvc.*

import actions.*
import models.{Password,User}
import models.forms.*
import repositories.*
import services.{CosgroupsRepositoryService, CosplaysRepositoryService, UsersService, UsersRepositoryService}

import java.time.LocalDate
import scala.concurrent.Future
import play.api.i18n.{Messages, MessagesProvider}

import utility.DateFormats.*
import services.UsersService

@Singleton
class MainController @Inject(
    cc: MessagesControllerComponents,
    userAction: UserAction,
    usersService: UsersService,
    usersRepositoryService: UsersRepositoryService,
    cosgroupsRepositoryService: CosgroupsRepositoryService,
    cosplaysRepositoryService: CosplaysRepositoryService,
)(implicit executionContext: ExecutionContext) 
  extends MessagesAbstractController(cc)
    with play.api.i18n.I18nSupport {

  def index() = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.pages.Index())
  }

  def about() = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.pages.About())
  }

  def login() = Action { implicit request: MessagesRequest[AnyContent] =>
    val boundForm = LoginForm.loginForm
    Ok(views.html.pages.Login(boundForm))
  }

  def loginPost() = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val boundForm = LoginForm.loginForm.bindFromRequest()
    boundForm.fold(
      formWithErrors => Future.successful(BadRequest(views.html.pages.Login(formWithErrors))),
      loginData => {
        val maybeUser = usersService.checkCredentials(loginData.username, loginData.password)
        maybeUser.map {
          case Some(_) =>
            Redirect(routes.MainController.dashboard())
              .withSession(request.session + ("username" -> loginData.username))
          case None =>
            Redirect(routes.MainController.login())
        }
      }
    )
  }

  def dashboard() = Action.async { implicit request: MessagesRequest[AnyContent] =>
    request.session.get("username") match {
      case Some(name) => {
        val repositoryUser = usersRepositoryService.returnUserByUsername(name)
        repositoryUser.map { currentUser =>
          val userObject: User = User(
            currentUser.get.id,
            currentUser.get.username,
            currentUser.get.email,
            Password(currentUser.get.password),
            currentUser.get.isAdmin,
            currentUser.get.biography,
            currentUser.get.dateOfBirth,
            currentUser.get.registrationDate
          )
          Ok(views.html.pages.Dashboard(userObject))()
        }
      }
      case None => {
        Future.successful(Redirect(routes.MainController.login()))
      }
    }
  }

  def loggedInPage() = userAction.async { implicit request: UserRequest[AnyContent] => 
    implicit val messages: Messages = messagesApi.preferred(request)
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
    implicit val messages: Messages = messagesApi.preferred(request)
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
    implicit val messages: Messages = messagesApi.preferred(request)
    request.username match {
      case Some(name) => {
        usersRepositoryService.returnUserByUsername(name).map { user =>
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
    implicit val messages: Messages = messagesApi.preferred(request)
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
        usersService.lookupUserByUsername(registrationData.username).flatMap {
          case Some(_) =>
            Future.successful(Redirect(routes.MainController.register()))
          case None =>
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
            usersRepositoryService.addNewUser(newUser).map { _ =>
              Redirect(routes.MainController.index())
            }
        }
      }
    )
  }

  def newCosplay() = Action { implicit request: MessagesRequest[AnyContent] =>
    val boundForm = CosplayForm.cosplayForm
    Ok(views.html.pages.NewCosplay(boundForm))
  }

  def newCosplayPost() = userAction.async { implicit request: UserRequest[AnyContent] => 
    request.username match {
      case Some(name) => {
        val boundForm = CosplayForm.cosplayForm.bindFromRequest()
        boundForm.fold(
          formWithErrors => {
            Future.successful(BadRequest(views.html.pages.NewCosplay(formWithErrors)))
          },
          newCosplayData => {
            usersRepositoryService.returnUserByUsername(name).map { user =>
              val currentUser = user.get
                val newCosplayInsertModel = CosplaysRepositoryInsertModel(
                  characterName = newCosplayData.characterName,
                  cosplayer = currentUser.id,
                  seriesName = Some(newCosplayData.seriesName),
                  started = newCosplayData.started,
                  completed = newCosplayData.completed,
                  description = newCosplayData.description,
                  budget = newCosplayData.budget,
                  cosplayComponents = newCosplayData.cosplayComponents.map(_.toCosplayComponent)
                )
                cosplaysRepositoryService.addNewCosplay(newCosplayInsertModel)
                Redirect(routes.MainController.dashboard())
              }
            }
        )
      }
      case None => {
        Future.successful(Redirect(routes.MainController.login()))
      }
    }
  }

  def newCosgroup() = Action { implicit request: MessagesRequest[AnyContent] =>
    val boundForm = CosgroupForm.cosgroupForm
    Ok(views.html.pages.NewCosgroup(boundForm))
  }

  def newCosgroupPost() = userAction.async { implicit request: UserRequest[AnyContent] => 
    request.username match {
      case Some(name) => {
        val boundForm = CosgroupForm.cosgroupForm.bindFromRequest()
        println(boundForm)
        boundForm.fold(
          formWithErrors => {
            println("Bad request.")
            Future.successful(BadRequest(views.html.pages.NewCosgroup(formWithErrors)))
          },
          newCosgroupData => {
            println("Successfully submitted cosgroup data...")
            usersRepositoryService.returnUserByUsername(name).map { user =>
              val currentUser = user.get
                val newCosgroupInsertModel = CosgroupsRepositoryInsertModel(
                  name             = newCosgroupData.name,
                  created          = newCosgroupData.created,
                  archived         = newCosgroupData.archived,
                  description      = newCosgroupData.description,
                  admin            = currentUser.id,
                  members          = Seq(currentUser.id),
                  nextEvents       = Seq(),
                  previousEvents   = Seq()
                )
                println(newCosgroupInsertModel)
                cosgroupsRepositoryService.addNewCosgroup(newCosgroupInsertModel)
                Redirect(routes.MainController.dashboard())
              }
            }
        )
      }
      case None => {
        Future.successful(Redirect(routes.MainController.login()))
      }
    }
  }

  def logout() = Action {
    Redirect(routes.MainController.login()).withNewSession
  }
}
