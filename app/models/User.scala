package models

case class User(
    username: String,
    firstName: String,
    lastName: String,
    emailAddress: String,
    password: String,
    cosplays: Seq[Cosplay]
)
