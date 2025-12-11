package models

import java.time.LocalDate
import java.util.Currency

case class Cosplay(
    id                  : Long,
    characterName       : String,
    cosplayer           : User,
    seriesName          : String,
    started             : LocalDate,
    status              : CosplayStatus = CosplayStatus.Started,
    completed           : Option[LocalDate] = None,
    archived            : Option[LocalDate] = None,
    description         : Option[String] = None,
    budget              : Option[Currency],
    cosplayComponent    : Seq[CosplayComponent]
)

sealed trait CosplayStatus {
    def asString: String
}
object CosplayStatus {
    case object Started     extends CosplayStatus { override val asString = "Started"   }
    case object Completed   extends CosplayStatus { override val asString = "Completed" }
    case object OnHold      extends CosplayStatus { override val asString = "OnHold"    }
    case object Archived    extends CosplayStatus { override val asString = "Archived"  }
}