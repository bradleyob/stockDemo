package models
import java.math.BigDecimal

case class StockData(symbol: String = "", name: String = "", price: BigDecimal = new BigDecimal(0.0))

