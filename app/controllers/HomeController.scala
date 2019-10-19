package controllers

import actors._
import akka.NotUsed
import akka.pattern.ask
import javax.inject._
import play.api.libs.json.JsValue
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request, RequestHeader, WebSocket}
import akka.actor._
import akka.stream.Materializer
import akka.stream.scaladsl.Flow
import akka.util.Timeout
import play.api.libs.streams.ActorFlow

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents)(implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {

  //val stockActor: ActorRef = system.actorOf(Props[StockActor], "stock-actor")

  def index: Action[AnyContent] = Assets.versioned("index.html")
  //Action { implicit request: Request[AnyContent] =>
    //Ok(views.html.index())
  //}

  def ws: WebSocket = WebSocket.accept[JsValue, JsValue] { request =>
    implicit val timeout: Timeout = Timeout(1.second)
    ActorFlow.actorRef { out =>
      StockActor.props(out)
    }
    //(stockActor ? Stocks(List("AAPL", "GOOGL"))).mapTo[String].map { message => Ok(message) }
  }

  /*private def wsFutureFlow(request: RequestHeader): Future[Flow[JsValue, JsValue, NotUsed]] = {
    implicit val timeout: Timeout = Timeout(1.second)
    // Do our processing on the request, take the response, and send a message back
  }*/

}
