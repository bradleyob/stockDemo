package actors
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import scala.collection.mutable
import play.api.Logger
import java.math.BigDecimal

import models.StockSymbolCollection
import models.StockData

import play.api.libs.json._

object StockActor {
  def props(out: ActorRef) = Props(new StockActor(out))
}

class StockActor(out: ActorRef) extends Actor with ActorLogging {
  // Set up some static data for displaying Stocks when a user has not provided any
  var stocksC: StockSymbolCollection = StockSymbolCollection(mutable.Map("GOOGL" -> StockData(), "AAPL" -> StockData()))

  implicit val stockWrites: Writes[StockData] = new Writes[StockData] {
    override def writes(o: StockData): JsValue = Json.obj(s"symbol"-> o.symbol,  s"name" -> o.name, s"price" -> o.price.toPlainString)
  }

  // Poll for new stock quotes every second
  val tick: Cancellable = {
    context.system.scheduler.schedule(Duration.Zero, 1.second, self, SendStocks)(context.system.dispatcher)
  }

  implicit val timeout: Timeout = Timeout(1.second)

  override def receive: Receive = {
    // We were given a new set of symbols to work with
    case tickerSymbol: String =>
      Logger.info("Fetching with updated symbol" + tickerSymbol)
      // Update and fire to websocket
      stocksC.stocks += (tickerSymbol -> StockData())
      updateStocks()
      out ! Json.toJson(stocksC.stocks)

    // Continue to use old set
    case SendStocks =>
      Logger.info("Sending loaded stocks: " + stocksC.toString)
      updateStocks()
      out ! Json.toJson(stocksC.stocks)
  }
  private def updateStocks(): Unit = {
    stocksC.stocks.keys.foreach {symbol => stocksC.stocks.update(symbol, StockSymbolCollection.getData(symbol))}
  }
}

case object SendStocks
