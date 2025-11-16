package models.forms

import play.api.data.*
import play.api.data.Forms.*
import play.api.data.validation.Constraints.*

case class LoginForm(
    username: String,
    password: String
)

object LoginForm {
    // username must not be empty, password must not be empty
    def unapply(login: LoginForm): Option[(String, String)] = Some((login.username, login.password))

    val loginForm = Form(
        mapping(
            "username" -> nonEmptyText,
            "password" -> nonEmptyText
        )(LoginForm.apply)(LoginForm.unapply)
    )
}