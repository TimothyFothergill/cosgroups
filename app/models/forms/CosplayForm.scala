package models.forms

import java.util.Currency
import java.util.Date

import models.{CosplayComponent, CosplayComponentForm, CosplayStatus, MaterialForm}
import utility.CurrencyFormat._

import play.api.data.*
import play.api.data.Forms.*
import play.api.data.validation.Constraints.*

import play.api.data.format.Formats.{dateFormat, intFormat}

case class CosplayForm(
    characterName       : String,
    seriesName          : String,
    started             : Date,
    status              : String,
    completed           : Option[Date] = None,
    archived            : Option[Date] = None,
    description         : Option[String] = None,
    budget              : Option[Currency],
    cosplayComponents   : Seq[CosplayComponentForm]
)

object CosplayForm {
    def unapply(cosplayForm: CosplayForm): Option[(String, String, Date, String, Option[Date], Option[Date], Option[String], Option[Currency], Seq[CosplayComponentForm])] = Some(
        cosplayForm.characterName,
        cosplayForm.seriesName,
        cosplayForm.started,
        cosplayForm.status,
        cosplayForm.completed,
        cosplayForm.archived,
        cosplayForm.description,
        cosplayForm.budget,
        cosplayForm.cosplayComponents
    )

    val materialFormMapping = mapping(
        "materialName"          -> nonEmptyText,
        "quantityNeeded"        -> optional(of[Int]),
        "expectedMaterialCost"  -> optional(of[Currency]),
        "isPurchased"           -> boolean,
        "purchaseCost"          -> optional(of[Currency]),
        "purchaseDate"          -> optional(date)
    )(MaterialForm.apply)(mf => Some((mf.materialName, mf.quantityNeeded, mf.expectedMaterialCost, mf.isPurchased, mf.purchaseCost, mf.purchaseDate)))

    val componentFormMapping = mapping(
        "componentName"         -> nonEmptyText,
        "budget"                -> optional(of[Currency]),
        "cost"                  -> optional(of[Currency]),
        "expectedMaterials"     -> seq(materialFormMapping),
        "actualMaterials"       -> seq(materialFormMapping),
        "started"               -> date,
        "completed"             -> optional(date),
        "archived"              -> optional(date)
    )(CosplayComponentForm.apply)(ccf => Some((ccf.componentName, ccf.budget, ccf.cost, ccf.expectedMaterials, ccf.actualMaterials, ccf.started, ccf.completed, ccf.archived)))

    val cosplayForm = Form(
        mapping(
            "characterName"     -> nonEmptyText,
            "seriesName"        -> nonEmptyText,
            "started"           -> date,
            "status"            -> nonEmptyText,
            "completed"         -> optional(date),
            "archived"          -> optional(date),
            "description"       -> optional(text),
            "budget"            -> optional(of[Currency]),
            "cosplayComponents" -> seq(componentFormMapping)
        )(CosplayForm.apply)(CosplayForm.unapply)
    )
}
