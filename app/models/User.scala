package models

import java.time.LocalDate

case class User(
    id: Long,
    username: String,
    email: String,
    password: Password,
    isAdmin: Boolean = false,
    biography: Option[String] = None,
    dateOfBirth: Option[LocalDate] = None,
    registrationDate: LocalDate = LocalDate.now()
)
