package models

case class User(
    id: Int,
    username: String,
    firstName: String,
    lastName: String,
    emailAddress: String,
    password: Password,
    cosplays: Seq[Cosplay] = Seq(),
    friends: Seq[User] = Seq()
)

object User {
    def isUsernameAvailable: Boolean = {
        true
    }

    def isEmailRegistered: Boolean = {
        false
    }

    def isPasswordValid: Boolean = {
        true
    }
}