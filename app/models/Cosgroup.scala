package models

case class Cosgroup(
    groupName: String,
    users: Seq[User]
)
