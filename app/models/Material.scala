package models

import java.util.Currency
import java.time.LocalDate
import java.util.Date

import utility.CurrencyFormat.*

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
)