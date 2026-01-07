package models

import repositories.CosplaysRepositoryModel
import utility.Mapping

import java.time.LocalDate
import java.util.Currency

case class Cosplay(
    id                  : Long,
    characterName       : String,
    cosplayerId         : Long,
    seriesName          : String,
    started             : LocalDate,
    status              : CosplayStatus = CosplayStatus.Started,
    completed           : Option[LocalDate] = None,
    archived            : Option[LocalDate] = None,
    description         : Option[String] = None,
    budget              : Option[Currency],
    cosplayComponents   : Seq[CosplayComponent]
)

object Cosplay {
    def fromRepo(
        cosplaysRepositoryModel: CosplaysRepositoryModel
    ): Cosplay = {
        Cosplay(
            id                  =   cosplaysRepositoryModel.id,
            characterName       =   cosplaysRepositoryModel.characterName,
            cosplayerId         =   cosplaysRepositoryModel.cosplayer,
            seriesName          =   cosplaysRepositoryModel.seriesName.get,
            started             =   cosplaysRepositoryModel.started,
            completed           =   cosplaysRepositoryModel.completed,
            description         =   cosplaysRepositoryModel.description,
            budget              =   cosplaysRepositoryModel.budget,
            cosplayComponents   =   cosplaysRepositoryModel.cosplayComponents
        )
    }

    def fromRepo(
        cosplaysRepositoryModels: Seq[CosplaysRepositoryModel]
    ): Seq[Cosplay] = {
        cosplaysRepositoryModels.map(fromRepo)
    }

    def toRepo(cosplay: Cosplay): CosplaysRepositoryModel = {
        CosplaysRepositoryModel(
            id                  =   cosplay.id,
            characterName       =   cosplay.characterName,
            cosplayer           =   cosplay.cosplayerId,
            seriesName          =   Some(cosplay.seriesName),
            started             =   cosplay.started,
            completed           =   cosplay.completed,
            description         =   cosplay.description,
            budget              =   cosplay.budget,
            cosplayComponents   =   cosplay.cosplayComponents
        )
    }

    implicit val repoToCosplay: Mapping[CosplaysRepositoryModel, Cosplay] =
        (repo: CosplaysRepositoryModel) => fromRepo(repo)

    implicit val userToRepo: Mapping[Cosplay, CosplaysRepositoryModel] =
        (cosplay: Cosplay) => toRepo(cosplay)
}

sealed trait CosplayStatus {
    def asString: String
}
object CosplayStatus {
    case object Started     extends CosplayStatus { override val asString = "Started"   }
    case object Completed   extends CosplayStatus { override val asString = "Completed" }
    case object OnHold      extends CosplayStatus { override val asString = "OnHold"    }
    case object Archived    extends CosplayStatus { override val asString = "Archived"  }
}