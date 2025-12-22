package services

import models.{Password, User}
import java.time.LocalDate
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UsersService @Inject(
    usersRepositoryService: UsersRepositoryService
)(implicit ec: ExecutionContext) {

    def checkCredentials(username: String, passwordText: String) = {
        val maybeUser: Future[Option[User]] = lookupUserByUsername(username)
        maybeUser.map { user =>
            user match {
                case Some(value) => {
                    if(Password.checkPassword(passwordText, user.get.password)) {
                        user
                    } else {
                        None
                    }
                }
                case _ => { None }
            }
        }
    }

    def lookupUserByUsername(username: String): Future[Option[User]] = {
        usersRepositoryService.returnUserByUsername(username).map { maybeUser =>
            if(maybeUser.nonEmpty) {
                Some(User(
                    id              = maybeUser.get.id, 
                    username        = maybeUser.get.username, 
                    email           = maybeUser.get.email, 
                    password        = Password(maybeUser.get.password),
                    isAdmin         = maybeUser.get.isAdmin, 
                    biography       = maybeUser.get.biography,
                    dateOfBirth     = maybeUser.get.dateOfBirth, 
                    registrationDate= maybeUser.get.registrationDate
                ))
            } else {
                None
            }
        }
    }
}
