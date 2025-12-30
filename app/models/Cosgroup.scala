package models

import repositories.CosgroupsRepositoryModel
import utility.Mapping

import java.time.LocalDate

case class Cosgroup(
    id              : Long                      ,
    name            : String                    ,
    created         : LocalDate                 ,
    archived        : Option[LocalDate]         ,
    description     : Option[String]    = None  ,
    members         : Seq[User]         = Seq() ,
    nextEvents      : Seq[Event]        = Seq() ,
    previousEvents  : Seq[Event]        = Seq() ,
)

object Cosgroup {
    def fromRepo(
        cosgroupsRepositoryModel: CosgroupsRepositoryModel,
        members: Seq[User] = Seq()
    ): Cosgroup = {
        Cosgroup(
            id              =   cosgroupsRepositoryModel.id,
            name            =   cosgroupsRepositoryModel.name,
            created         =   cosgroupsRepositoryModel.created,
            archived        =   cosgroupsRepositoryModel.archived,
            description     =   cosgroupsRepositoryModel.description,
            members         =   members,
            nextEvents      =   Seq(), // Same here, we'll need to get the ids of events and convert them into event objects..?
            previousEvents  =   Seq() // Same here, we'll need to get the ids of events and convert them into event objects..?
        )
    }

    def fromRepo(
        cosgroupsRepositoryModels: Seq[CosgroupsRepositoryModel]
    ): Seq[Cosgroup] = {
        cosgroupsRepositoryModels.map(fromRepo)
    }

    def toRepo(cosgroup: Cosgroup): CosgroupsRepositoryModel = {
        CosgroupsRepositoryModel(
            id              =   cosgroup.id,
            name            =   cosgroup.name,
            created         =   cosgroup.created,
            archived        =   cosgroup.archived,
            description     =   cosgroup.description,
            admin           =   cosgroup.members.head.id,
            members         =   cosgroup.members.map(_.id), // Interesting one... We'll need to get the id for members and convert them into user objects?
            nextEvents      =   Seq(), // Same here, we'll need to get the ids of events and convert them into event objects..?
            previousEvents  =   Seq() // Same here, we'll need to get the ids of events and convert them into event objects..?
        )
    }

    implicit val repoToCosgroup: Mapping[CosgroupsRepositoryModel, Cosgroup] =
        (repo: CosgroupsRepositoryModel) => fromRepo(repo)

    implicit val cosgroupToRepo: Mapping[Cosgroup, CosgroupsRepositoryModel] =
        (cosgroup: Cosgroup) => toRepo(cosgroup)
}
