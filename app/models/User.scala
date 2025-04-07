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
    // implicit val defaultUser: User = User(
    //     id = 0,
    //     username = "Timlah",
    //     firstName = "Tim",
    //     lastName = "Fothergill",
    //     emailAddress = "timlah@timlah.com",
    //     password = Password("Password123!"),
    //     cosplays = Seq(),
    //     friends = Seq()
    // )

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
