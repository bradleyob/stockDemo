package actors
import akka.actor._
import akka.util.Timeout
import models.{SendStocks, StockData, StockSymbol, StockSymbolCollection}
import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json._

import scala.collection.mutable
import scala.concurrent.duration._

object StockActor {
  def props(out: ActorRef) = Props(new StockActor(out))
}

class StockActor(out: ActorRef) extends Actor with ActorLogging {
  // Set up some static data for displaying Stocks when a user has not provided any
  var stocksC: StockSymbolCollection = StockSymbolCollection(mutable.Map("GOOGL" -> StockData(), "AAPL" -> StockData()))

  implicit val stockWrites: Writes[StockData] = (o: StockData) => Json.obj(
    s"symbol" -> o.symbol,
    s"name" -> o.name,
    s"price" -> o.price.toPlainString
  )

  implicit val StockReads: Reads[StockSymbol] = ((JsPath  \ "symbol").read[String] and
    (JsPath \ "action").read[String])(StockSymbol.apply _)


  // Poll for new stock quotes every second
  val tick: Cancellable = {
    context.system.scheduler.schedule(Duration.Zero, 1.second, self, SendStocks)(context.system.dispatcher)
  }

  implicit val timeout: Timeout = Timeout(1.second)

  override def receive: Receive = {
    // We were given a new set of symbols to work with
    case tickerSymbol: JsValue =>
      val symbol = tickerSymbol.as[StockSymbol]
      symbol.action match {
        // An action can be either add or remove
        case "add" =>
          Logger.info("Fetching with updated symbol " + symbol.symbol)
          // Update and fire to websocket
          stocksC.stocks += (symbol.symbol -> StockData())
          updateStocks()
          out ! Json.toJson(stocksC.stocks)
        case "remove" =>
          Logger.info("Removing stock " + symbol.symbol)
          // Mutate our stock collection to remove the marked stock
          stocksC.stocks.retain(((keepStock,_) => keepStock != symbol.symbol))
          out ! Json.toJson(stocksC.stocks)
      }

    // Continue to use old set
    case SendStocks =>
      Logger.info("Sending loaded stocks: " + stocksC.toString)
      updateStocks()
      out ! Json.toJson(stocksC.stocks)
  }

  private def updateStocks(): Unit = {
    stocksC.stocks.keys.foreach { symbol => stocksC.stocks.update(symbol, StockSymbolCollection.getData(symbol)) }
  }
}
