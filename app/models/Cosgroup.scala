package models

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
    def addMember(newMember: User) = {
        // val newMembersList: Seq[user] = members.copy(newMember)
    }
}