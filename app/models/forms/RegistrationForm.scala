package models.forms

import play.api.data.*
import play.api.data.Forms.*
import play.api.data.validation.Constraints.*

case class RegistrationForm(
    username: String,
    email: String,
    password: String,
    confirmPassword: String
)

object RegistrationForm {
    // rules: username min 3 chars, email valid format, password min 8 chars, password and confirmPassword match
    def unapply(registrationForm: RegistrationForm): Option[(String, String, String, String)] = Some(
        (
            registrationForm.username, 
            registrationForm.email,
            registrationForm.password,
            registrationForm.confirmPassword
        )
    )

    val registrationForm = Form(
        mapping(
            "username" -> nonEmptyText,
            "email" -> email,
            "password" -> nonEmptyText,
            "confirmPassword" -> nonEmptyText
        )(RegistrationForm.apply)(RegistrationForm.unapply)
    )    
}
