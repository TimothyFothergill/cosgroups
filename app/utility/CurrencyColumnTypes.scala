package utility

import slick.jdbc.PostgresProfile.api.*
import java.util.Currency
import slick.jdbc.JdbcType

object CurrencyColumnTypes {

  implicit val currencyColumnType: JdbcType[Currency] =
    MappedColumnType.base[Currency, String](
      c => c.getCurrencyCode,
      s => Currency.getInstance(s)
    )
}
