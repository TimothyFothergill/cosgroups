package utility

import play.api.libs.json.*
import java.util.Currency
import play.api.data.format.Formatter
import play.api.data.{FormError}


object CurrencyFormat {
    private val reads: Reads[Currency] = Reads[Currency] {
        case JsString(value) => JsSuccess(Currency.getInstance(value))
        case _               => JsError("Incorrect currency format")
    }

    private val writes: Writes[Currency] = Writes[Currency] { c =>
        JsString(c.toString())
    }

    implicit val format: Format[Currency] = Format[Currency](reads, writes)

    implicit val formatter: Formatter[Currency] = new Formatter[Currency] {
      override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], Currency] =
        data.get(key).map(_.trim) match {
          case Some(code) if code.nonEmpty =>
            try Right(Currency.getInstance(code))
            catch {
              case _: IllegalArgumentException =>
                Left(Seq(FormError(key, s"Invalid currency code: $code")))
            }

          case _ => Right(null.asInstanceOf[Currency])
        }

      override def unbind(key: String, value: Currency): Map[String, String] =
        if (value == null) Map.empty
        else Map(key -> value.getCurrencyCode)
    }
}

// single implicit `formatter` lives inside `CurrencyFormat` object above
