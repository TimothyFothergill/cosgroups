package models

import models.*
import java.time.LocalDate

case class User(
    id: Long,
    username: String,
    email: String,
    password: Password,
    isAdmin: Boolean = false,
    biography: Option[String] = None,
    dateOfBirth: Option[LocalDate] = None,
    registrationDate: LocalDate = LocalDate.now(),
    cosplays: Seq[Cosplay] = Seq()
)

object User {
    def checkCredentials(username: String, passwordText: String): Option[User] = {
        val maybeStoredUserAccount: Option[User] = User.lookupUserByUsername(username)
        if(maybeStoredUserAccount.nonEmpty) {
            if(Password.checkPassword(passwordText, maybeStoredUserAccount.get.password)) {
                maybeStoredUserAccount
            } else {
                None
            }
        } else {
            None
        }
    }

    def lookupUserByUsername(username: String): Option[User] = {
        if(username == "timlah") {
            val storedPassword: Password = Password.createPassword("Password123!")
            Some(User(1,"timlah","timlah@timlah.com",storedPassword,true,None,None,LocalDate.now(),Seq()))
        } else {
            None
        }
    }
}
