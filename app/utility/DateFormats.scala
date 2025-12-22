package utility

import java.time.{LocalDate, ZoneId}
import java.time.format.DateTimeFormatter
import java.util.Date

object DateFormats {
    private val fmt = ZoneId.systemDefault()
    implicit def dateToLocalDateFormat(dateToConvert: Date): LocalDate =
        LocalDate.ofInstant(
            dateToConvert.toInstant(),
            fmt
        )
    
    implicit def localDateToDateFormat(localDateToConvert: LocalDate): Date =
        Date.from(localDateToConvert.atStartOfDay()
            .atZone(fmt)
            .toInstant()
        )

    implicit def optionalDateToOptionalLocalDateFormat(optionalDateToConvert: Option[Date]): Option[LocalDate] =
        optionalDateToConvert match {
            case Some(value) => {
                Some(LocalDate.ofInstant(
                    optionalDateToConvert.get.toInstant(),
                    fmt
                ))
            }
            case _ => None
        }

    implicit def optionalLocalDateToOptionalDateFormat(optionalLocalDateToConvert: Option[LocalDate]): Option[Date] =
        optionalLocalDateToConvert match {
            case Some(value) => {
                Some(Date.from(optionalLocalDateToConvert.get.atStartOfDay()
                    .atZone(fmt)
                    .toInstant()
                ))
            }
            case _ => None
        }
}
