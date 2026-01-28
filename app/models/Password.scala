package models

import java.util.UUID
import org.mindrot.jbcrypt.BCrypt

case class Password(
    hashedPassword: String
)

object Password {
    private val costFactor: Int = 12

    private lazy val Pepper: String = sys.env.getOrElse("COSGROUPS_DB_PEPPER",
        throw new RuntimeException("Environment variable COSGROUPS_DB_PEPPER not set.")
    )

    def createPassword(plainText: String): Password = {
        val hash = BCrypt.hashpw(plainText + Pepper, BCrypt.gensalt(costFactor))
        Password(hash)
    }

    def checkPassword(plainText: String, storedPassword: Password): Boolean = {
        BCrypt.checkpw(plainText + Pepper, storedPassword.hashedPassword)
    }
}
