package models

import repositories.{CosplaysRepositoryModel, CosgroupsRepositoryModel, UsersRepositoryModel}
import models.Password
import utility.{Mapping, ManyMappingWithSeq}

import java.time.LocalDate

case class User(
    id                    : Long,
    username              : String,
    email                 : String,
    password              : Password,
    isAdmin               : Boolean = false,
    biography             : Option[String] = None,
    dateOfBirth           : Option[LocalDate] = None,
    registrationDate      : LocalDate = LocalDate.now(),
    cosplays              : Seq[Cosplay] = Seq(),
    cosgroups             : Seq[Cosgroup] = Seq()
)

object User {
  def fromRepo(
    repoUserModel: UsersRepositoryModel,
    cosplays: Seq[Cosplay],
    cosgroups: Seq[Cosgroup]
  ): User = {
        User(
          id                = repoUserModel.id,
          username          = repoUserModel.username,
          email             = repoUserModel.email,
          password          = Password(repoUserModel.password),
          isAdmin           = repoUserModel.isAdmin,
          biography         = repoUserModel.biography,
          dateOfBirth       = repoUserModel.dateOfBirth,
          registrationDate  = repoUserModel.registrationDate,
          cosplays          = cosplays,
          cosgroups         = cosgroups
        )
    }

    def toRepo(user: User): UsersRepositoryModel = {
        UsersRepositoryModel(
            id                = user.id,
            username          = user.username,
            email             = user.email,
            password          = user.password.hashedPassword,
            isAdmin           = user.isAdmin,
            biography         = user.biography,
            dateOfBirth       = user.dateOfBirth,
            registrationDate  = user.registrationDate,
            lastActive        = None
        )
    }

  implicit val repoToUser: ManyMappingWithSeq[UsersRepositoryModel, CosplaysRepositoryModel, CosgroupsRepositoryModel, User] =
    new ManyMappingWithSeq[UsersRepositoryModel, CosplaysRepositoryModel, CosgroupsRepositoryModel, User] {
      def map(
        repoUserModel: UsersRepositoryModel, 
        cosplays: Seq[CosplaysRepositoryModel], 
        cosgroups: Seq[CosgroupsRepositoryModel]
      ): User =
        fromRepo(
          repoUserModel, 
          Cosplay.fromRepo(cosplays), 
          Cosgroup.fromRepo(cosgroups, Seq()) // this isn't right yet... how are we getting these Cosgroups filled?
        )
    }

  implicit val userToRepo: Mapping[User, UsersRepositoryModel] =
    new Mapping[User, UsersRepositoryModel] {
      def map(user: User): UsersRepositoryModel = toRepo(user)
    }
}
