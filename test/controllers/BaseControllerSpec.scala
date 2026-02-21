package controllers

import org.scalatest._
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.{Play, Application}
import play.api.inject.guice._

class BaseControllerSpec extends PlaySpec with GuiceOneAppPerSuite {
    override def fakeApplication() = 
        new GuiceApplicationBuilder().configure(
            "play.http.secret.key"  -> "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef",
            "db.default.url"        -> "jdbc:postgresql://localhost:5432/test",
            "db.default.username"   -> "test",
            "db.default.password"   -> "testpass"
        )
        .build()
}
