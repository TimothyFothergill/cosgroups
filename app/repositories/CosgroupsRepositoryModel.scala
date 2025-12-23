package repositories

import java.time.LocalDate
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api.*

import utility.JsonColumnTypes.*
import java.util.Currency
import play.api.libs.json.JsValue

case class CosgroupsRepositoryModel(
    id              : Long,
    name            : String,
    created         : LocalDate,
    archived        : Option[LocalDate],
    description     : Option[String],
    admin           : Long,
    members         : Seq[Long],
    nextEvents      : Seq[Long],
    previousEvents  : Seq[Long]
)

case class CosgroupsRepositoryInsertModel(
    name            : String,
    created         : LocalDate,
    archived        : Option[LocalDate],
    description     : Option[String],
    admin           : Long,
    members         : Seq[Long],
    nextEvents      : Seq[Long],
    previousEvents  : Seq[Long]
)

class CosgroupsTable(tag: Tag) extends Table[CosgroupsRepositoryModel](tag, "cosgroups") {
import utility.JsonColumnTypes.*
    def id              = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name            = column[String]("name")
    def created         = column[LocalDate]("created")
    def archived        = column[Option[LocalDate]]("archived")
    def description     = column[Option[String]]("description")
    def admin           = column[Long]("admin")
    def members         = column[Seq[Long]]("members")
    def nextEvents      = column[Seq[Long]]("nextEvents")
    def previousEvents  = column[Seq[Long]]("previousEvents")

    def * = (
        id,
        name,
        created,
        archived,
        description,
        admin,
        members,
        nextEvents,
        previousEvents
    ).mapTo[CosgroupsRepositoryModel]
}
