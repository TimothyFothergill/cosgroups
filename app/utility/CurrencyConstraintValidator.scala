package utility

import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}


class CurrencyConstraintValidator {
    val allNumbers = """\d*""".r
    val allLetters = """[A-Za-z]*""".r

    def passwordCheckConstraint: Constraint[String] = Constraint("constraints.passwordcheck") { plainText =>
        val errors = plainText match {
            case allNumbers() => Seq(ValidationError("Password is all numbers"))
            case allLetters() => Seq(ValidationError("Password is all letters"))
            case _            => Nil
        }
        if (errors.isEmpty) {
            Valid
        } else {
            Invalid(errors)
        }
    }
}
