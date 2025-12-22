package models

import utility.CurrencyFormat.*
import utility.LocalDateFormat.*
import utility.DateFormats.*
import models.Material.*

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
) {
    def toCosplayComponent: CosplayComponent =
        CosplayComponent(
            componentName       = componentName,
            budget              = budget,
            cost                = cost,
            expectedMaterials   = expectedMaterials.map(_.toMaterial),
            actualMaterials     = actualMaterials.map(_.toMaterial),
            started             = started,
            completed           = completed,
            archived            = archived
        )
}

object CosplayComponentForm {
    def cosplayComponentFormToCosplayComponent(cosplayComponentForm: CosplayComponentForm) = 
        CosplayComponent(
            componentName       = cosplayComponentForm.componentName,
            budget              = cosplayComponentForm.budget,
            cost                = cosplayComponentForm.cost,
            expectedMaterials   = cosplayComponentForm.expectedMaterials.map(_.toMaterial),
            actualMaterials     = cosplayComponentForm.actualMaterials.map(_.toMaterial),
            started             = cosplayComponentForm.started,
            completed           = cosplayComponentForm.completed,
            archived            = cosplayComponentForm.archived
        )
}
