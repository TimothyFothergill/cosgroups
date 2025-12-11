package utility

import play.api.libs.json._
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object LocalDateFormat {
  private val fmt = DateTimeFormatter.ISO_LOCAL_DATE

  implicit val localDateFormat: Format[LocalDate] = Format(
    Reads.localDateReads(fmt),
    Writes[LocalDate] { d => JsString(d.format(fmt)) }
  )
}
