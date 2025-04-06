package models

import play.api.libs.json.OFormat
import play.api.libs.json.Json

case class Password(
    passwordString: String
)

object Password {
    // password rules:
    // A minimum of -
    // 8 alphanumeric characters
    // 1 special character
    // 1 capital

    implicit val passwordFormat: OFormat[Password] = Json.format[Password]

    def checkPasswordValidity(passwordAttempt: String): Boolean = {
        true
    }
}
