package repositories

import java.time.LocalDate
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api.*

import models.{Cosplay, CosplayComponent, User}
import services.UsersRepositoryService
import utility.JsonColumnTypes.*
import java.util.Currency
import play.api.libs.json.JsValue

case class CosplaysRepositoryModel(
    id                  : Long,
    characterName       : String,
    cosplayer           : Long,
    seriesName          : Option[String],
    started             : LocalDate,
    completed           : Option[LocalDate] = None,
    description         : Option[String] = None,
    budget              : Option[Currency],
    cosplayComponents   : Seq[CosplayComponent]
)

case class CosplaysRepositoryInsertModel(
    characterName       : String,
    cosplayer           : Long,
    seriesName          : Option[String],
    started             : LocalDate,
    completed           : Option[LocalDate] = None,
    description         : Option[String] = None,
    budget              : Option[Currency],
    cosplayComponents   : Seq[CosplayComponent]
)

class CosplaysTable(tag: Tag) extends Table[CosplaysRepositoryModel](tag, "cosplays") {
import utility.CurrencyColumnTypes.*
    def id                  = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def characterName       = column[String]("characterName")
    def cosplayer           = column[Long]("cosplayer")
    def seriesName          = column[Option[String]]("seriesName")
    def started             = column[LocalDate]("started")
    def completed           = column[Option[LocalDate]]("completed")
    def description         = column[Option[String]]("description")
    def budget              = column[Option[Currency]]("budget")
    def cosplayComponents   = column[Seq[CosplayComponent]]("cosplayComponents")

    def * = (
        id,
        characterName,
        cosplayer,
        seriesName,
        started,
        completed,
        description,
        budget,
        cosplayComponents
    ).mapTo[CosplaysRepositoryModel]
}
