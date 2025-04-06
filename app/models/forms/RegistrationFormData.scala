package models.forms

import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formatter
import play.api.mvc.QueryStringBindable

import models.Password

case class RegistrationFormData(
    username        : String,
    firstName       : String,
    surname         : String,
    email           : String,
    password        : String,
    confirmPassword : String
) 

object RegistrationFormData {
    // val registrationForm: Form[RegistrationFormData] = Form(
    //     mapping(
    //         "username"          -> nonEmptyText ,
    //         "firstName"         -> nonEmptyText ,
    //         "surname"           -> nonEmptyText ,
    //         "email"             -> nonEmptyText ,
    //         "password"          -> nonEmptyText ,
    //         "confirmPassword"   -> nonEmptyText
    //     )(RegistrationFormData.apply)(RegistrationFormData.unapply)
    // )
}
