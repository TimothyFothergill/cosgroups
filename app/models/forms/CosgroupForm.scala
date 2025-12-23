package models.forms

import java.util.Date

import play.api.data.*
import play.api.data.Forms.*
import play.api.data.validation.Constraints.*

import play.api.data.format.Formats.{dateFormat, longFormat}

case class CosgroupForm(
    name            : String                    ,
    created         : Date                      ,
    archived        : Option[Date]              ,
    description     : Option[String]    = None  ,
    members         : Seq[Long]         = Seq() ,
    nextEvents      : Seq[Long]         = Seq() ,
    previousEvents  : Seq[Long]         = Seq() ,
)

object CosgroupForm {
    def unapply(cosgroupForm: CosgroupForm): Option[(String, Date, Option[Date], Option[String], Seq[Long], Seq[Long], Seq[Long])] = Some(
        cosgroupForm.name           ,
        cosgroupForm.created        ,
        cosgroupForm.archived       ,
        cosgroupForm.description    ,
        cosgroupForm.nextEvents     ,
        cosgroupForm.members        ,
        cosgroupForm.previousEvents
    )

    val cosgroupForm = Form(
        mapping(
            "name"              -> nonEmptyText     ,
            "created"           -> date             ,
            "archived"          -> optional(date)   ,
            "description"       -> optional(text)   ,
            "members"           -> seq(of[Long])    ,
            "nextEvents"        -> seq(of[Long])    ,
            "previousEvents"    -> seq(of[Long])    ,
        )(CosgroupForm.apply)(CosgroupForm.unapply)
    )
}
