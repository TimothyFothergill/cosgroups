package models

case class Cosgroup(
    id: Long,
    name: String,
    description: Option[String] = None,
    members: Seq[User] = Seq()
)

object Cosgroup {
    def addMember(newMember: User) = {
        // val newMembersList: Seq[user] = members.copy(newMember)
    }
}