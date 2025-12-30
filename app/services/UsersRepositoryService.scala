package services

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api.*
import repositories.*
import services.*
import models.{Cosplay, User}

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import play.db.NamedDatabase
import scala.concurrent.ExecutionContext

@Singleton
class UsersRepositoryService @Inject(
    @NamedDatabase("cosgroups") val dbConfigProvider: DatabaseConfigProvider
)(
    cosplaysRepositoryService: CosplaysRepositoryService
)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

    import profile.api._

    private val usersTable = TableQuery[UsersTable]
    private val insertProjection =
    usersTable.map(u =>
        (u.username, u.email, u.password, u.isAdmin, u.biography, u.dateOfBirth, u.registrationDate)
    )

    def addNewUser(newUser: UsersRepositoryInsertModel): Future[Int] = {
        val action =
            insertProjection += (
                newUser.username,
                newUser.email,
                newUser.password,
                newUser.isAdmin,
                newUser.biography,
                newUser.dateOfBirth,
                newUser.registrationDate
            )
        db.run(action)
    }

    def returnAllUsers(): Future[Seq[UsersRepositoryModel]] = {
        db.run(usersTable.result)
    }

    def returnUserWithCosplaysById(id: Long): Future[Option[User]] = {
        for {
            repoUser        <- returnUserById(id)
            repoCosplays    <- cosplaysRepositoryService.returnCosplaysByUserId(id)
        } yield {
            repoUser match {
                case Some(user) => {
                    val cosplays = repoCosplays.map { cosplay => (Cosplay.fromRepo(cosplay)) }
                    Some(User.fromRepo(user, cosplays))
                }
                case None => {
                    None
                }
            }
        }
    }

    def returnUserWithCosplaysByUsername(username: String): Future[Option[User]] = {
        for {
            repoUser        <- returnUserByUsername(username)
            repoCosplays    <- cosplaysRepositoryService.returnCosplaysByUserId(repoUser.get.id)
        } yield {
            repoUser match {
                case Some(user) => {
                    val cosplays = repoCosplays.map { cosplay => (Cosplay.fromRepo(cosplay)) }
                    Some(User.fromRepo(user, cosplays))
                }
                case None => {
                    None
                }
            }
        }
    }

// {
//     def toCosplay: Cosplay = {
//         UsersRepositoryService.returnUserById(cosplayer).map { user => 
            
//         }
//         Cosplay(
//             id                  = id,
//             characterName       = characterName,
//             cosplayer           = ,
//             seriesName          = seriesName,
//             started             = started,
//             completed           = completed,
//             description         = description,
//             budget              = budget,
//             cosplayComponents   = cosplayComponents
//         )
//     }
// } <- can we do an implicit conversion of [T]RepositoryModel -> [T]?


    def returnUserById(id: Long): Future[Option[UsersRepositoryModel]] = {
        db.run(usersTable.filter(_.id === id).result.headOption)
    }

    def returnUserByUsername(username: String): Future[Option[UsersRepositoryModel]] = {
        db.run(usersTable.filter(_.username === username).result.headOption)
    }

    def updateUser(updatedUser: UsersRepositoryModel): Future[Int] = {
        val query = 
            usersTable.filter(_.id === updatedUser.id)
                .map(x => (x.username, x.email, x.password, x.isAdmin, x.biography, x.dateOfBirth))
                .update(
                    (
                        updatedUser.username, 
                        updatedUser.email, 
                        updatedUser.password, 
                        updatedUser.isAdmin, 
                        updatedUser.biography, 
                        updatedUser.dateOfBirth
                    )
                )
        db.run(query)
    }

    def dropUser(id: Long): Future[Int] = {
        val query = 
            usersTable.filter(_.id === id).delete
        db.run(query)
    }
}
