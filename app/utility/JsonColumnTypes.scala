package utility

import models.CosplayComponent
import slick.jdbc.JdbcType

object JsonColumnTypes {
  import slick.jdbc.PostgresProfile.api.*
  import play.api.libs.json.*

  implicit def seqFormat[T : Format]: Format[Seq[T]] =
    Format(
      Reads.seq(Json.fromJson[T](_)),
      Writes.seq(t => Json.toJson(t))
    )

  implicit val cosplayComponentsColumnType: JdbcType[Seq[CosplayComponent]] =
    MappedColumnType.base[Seq[CosplayComponent], String](
      seq => Json.stringify(Json.toJson(seq)),
      str => Json.parse(str).as[Seq[CosplayComponent]]
    )

  implicit val seqLongColumnType: JdbcType[Seq[Long]] =
    MappedColumnType.base[Seq[Long], String](
      seq => Json.stringify(Json.toJson(seq)),
      str => Json.parse(str).as[Seq[Long]]
    )
}
