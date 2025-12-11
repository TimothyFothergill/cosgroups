package models

import utility.CurrencyFormat.*
import utility.LocalDateFormat.*

import java.util.Currency
import java.time.LocalDate
import java.util.Date

import play.api.libs.json.*
import play.api.libs.json.Reads.*
import play.api.libs.functional.syntax.*

import scala.language.postfixOps

case class CosplayComponent(
    componentName       : String,
    budget              : Option[Currency],
    cost                : Option[Currency],
    expectedMaterials   : Seq[Material],
    actualMaterials     : Seq[Material],
    started             : LocalDate,
    completed           : Option[LocalDate] = None,
    archived            : Option[LocalDate] = None
)

object CosplayComponent {
    implicit val cosplayComponentJsonFormat: OFormat[CosplayComponent] = Json.format[CosplayComponent]
}

case class CosplayComponentForm(
    componentName       : String,
    budget              : Option[Currency],
    cost                : Option[Currency],
    expectedMaterials   : Seq[MaterialForm],
    actualMaterials     : Seq[MaterialForm],
    started             : Date,
    completed           : Option[Date] = None,
    archived            : Option[Date] = None
)
