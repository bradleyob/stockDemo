package models

import java.math.BigDecimal

import yahoofinance.{Stock, YahooFinance}

import scala.collection.mutable
case class StockSymbolCollection(stocks: mutable.Map[String, StockData])

object StockSymbolCollection {
 def getData(symbol: String): StockData = {
  val symbolData: Stock= YahooFinance.get(symbol)
  StockData(symbol, symbolData.getName, symbolData.getQuote().getPrice)
 }
}
