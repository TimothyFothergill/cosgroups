package repositories

import java.time.LocalDate
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api.*

case class UsersRepositoryModel(
    id              : Long,
    username        : String,
    email           : String,
    password        : String,
    isAdmin         : Boolean,
    biography       : Option[String],
    dateOfBirth     : Option[LocalDate],
    registrationDate: LocalDate,
    lastActive      : Option[LocalDate]
)

case class UsersRepositoryInsertModel(
    username        : String,
    email           : String,
    password        : String,
    isAdmin         : Boolean,
    biography       : Option[String],
    dateOfBirth     : Option[LocalDate],
    registrationDate: LocalDate
)

class UsersTable(tag: Tag) extends Table[UsersRepositoryModel](tag, "users") {
  def id              = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def username        = column[String]("username")
  def email           = column[String]("email")
  def password        = column[String]("password")
  def isAdmin         = column[Boolean]("isadmin")
  def biography       = column[Option[String]]("biography")
  def dateOfBirth     = column[Option[LocalDate]]("dateofbirth")
  def registrationDate= column[LocalDate]("registrationdate")
  def lastActive      = column[Option[LocalDate]]("lastactive")

  def * = (
    id,
    username,
    email,
    password,
    isAdmin,
    biography,
    dateOfBirth,
    registrationDate,
    lastActive
  ).mapTo[UsersRepositoryModel]
}