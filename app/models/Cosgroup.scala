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
            nextEvents      =   Seq(), // This comes along later
            previousEvents  =   Seq() // This comes along later
        )
    }

    def fromRepo(
        cosgroupsRepositoryModels: Seq[CosgroupsRepositoryModel],
        members: Seq[User]
    ): Seq[Cosgroup] = {
        cosgroupsRepositoryModels.map(cosgroupsRepositoryModel => fromRepo(cosgroupsRepositoryModel, members))
    }

    def toRepo(cosgroup: Cosgroup): CosgroupsRepositoryModel = {
        CosgroupsRepositoryModel(
            id              =   cosgroup.id,
            name            =   cosgroup.name,
            created         =   cosgroup.created,
            archived        =   cosgroup.archived,
            description     =   cosgroup.description,
            admin           =   cosgroup.members.head.id,
            members         =   cosgroup.members.map(_.id),
            nextEvents      =   Seq(), // This comes along later
            previousEvents  =   Seq() // This comes along later
        )
    }

    implicit val repoToCosgroup: Mapping[CosgroupsRepositoryModel, Cosgroup] =
        (repo: CosgroupsRepositoryModel) => fromRepo(repo)

    implicit val cosgroupToRepo: Mapping[Cosgroup, CosgroupsRepositoryModel] =
        (cosgroup: Cosgroup) => toRepo(cosgroup)
}
