package models

import java.util.Currency
import java.time.LocalDate
import java.util.Date

import utility.CurrencyFormat.*
import utility.DateFormats.*

import play.api.libs.json._

import scala.language.postfixOps

case class Material(
  materialName: String,
  quantityNeeded: Option[Int],
  expectedMaterialCost: Option[Currency],
  isPurchased: Boolean,
  purchaseCost: Option[Currency],
  purchaseDate: Option[LocalDate]
)

object Material {
  implicit val materialJsonFormat: OFormat[Material] = Json.format[Material]
}

case class MaterialForm(
  materialName: String,
  quantityNeeded: Option[Int],
  expectedMaterialCost: Option[Currency],
  isPurchased: Boolean,
  purchaseCost: Option[Currency],
  purchaseDate: Option[Date]
) {
  def toMaterial: Material =
    Material(
      materialName = materialName,
      quantityNeeded = quantityNeeded,
      expectedMaterialCost = expectedMaterialCost,
      isPurchased = isPurchased,
      purchaseCost = purchaseCost,
      purchaseDate = purchaseDate
    )
}

object MaterialForm {
  implicit def materialFormToMaterial(materialForm: MaterialForm): Material =
    Material(
      materialName = materialForm.materialName,
      quantityNeeded = materialForm.quantityNeeded,
      expectedMaterialCost = materialForm.expectedMaterialCost,
      isPurchased = materialForm.isPurchased,
      purchaseCost = materialForm.purchaseCost,
      purchaseDate = materialForm.purchaseDate
    )

    
}