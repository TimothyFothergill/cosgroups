package services

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api.*
import repositories.*
import services.*
import models.{Cosplay, Cosgroup, User}

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
    cosplaysRepositoryService: CosplaysRepositoryService,
    cosgroupsRepositoryService: CosgroupsRepositoryService
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

    private def resolveCosgroupsMembers(cosgroupRepos: Seq[CosgroupsRepositoryModel]): Future[Seq[Cosgroup]] = {
        Future.traverse(cosgroupRepos) { cosgroupRepo =>
            Future.traverse(cosgroupRepo.members) { memberId =>
                returnUserById(memberId).map(_.map(u => User.fromRepo(u, Seq(), Seq())))
            }.map(_.flatten).map { members =>
                Cosgroup.fromRepo(cosgroupRepo, members)
            }
        }
    }

    def returnUserWithCosplaysById(id: Long): Future[Option[User]] = {
        returnUserById(id).flatMap {
            case None => Future.successful(None)
            case Some(userRepo) =>
                for {
                    repoCosplays  <- cosplaysRepositoryService.returnCosplaysByUserId(id)
                    repoCosgroups <- cosgroupsRepositoryService.returnCosgroupsByUserId(id)
                    cosgroups     <- resolveCosgroupsMembers(repoCosgroups)
                } yield {
                    val cosplays = repoCosplays.map(Cosplay.fromRepo)
                    Some(User.fromRepo(userRepo, cosplays, cosgroups))
                }
        }
    }

    def returnUserWithCosplaysByUsername(username: String): Future[Option[User]] = {
        returnUserByUsername(username).flatMap {
            case None => Future.successful(None)
            case Some(userRepo) =>
                for {
                    repoCosplays  <- cosplaysRepositoryService.returnCosplaysByUserId(userRepo.id)
                    repoCosgroups <- cosgroupsRepositoryService.returnCosgroupsByUserId(userRepo.id)
                    cosgroups     <- resolveCosgroupsMembers(repoCosgroups)
                } yield {
                    val cosplays = repoCosplays.map(Cosplay.fromRepo)
                    Some(User.fromRepo(userRepo, cosplays, cosgroups))
                }
        }
    }

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
