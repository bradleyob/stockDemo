package models

import java.io.FileNotFoundException

import yahoofinance.YahooFinance

import scala.collection.mutable
case class StockSymbolCollection(stocks: mutable.Map[String, StockData])

object StockSymbolCollection {
 def getData(symbol: String): StockData = {
   try {
     val symbolData = YahooFinance.get(symbol)
     StockData(symbol, symbolData.getName, symbolData.getQuote().getPrice)
   } catch {
     // Return a dummy that indicates the symbol was invalid
     case e: FileNotFoundException => StockData(symbol, "Invalid Ticker Symbol")
  }
 }
}
