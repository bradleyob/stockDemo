package controllers

import actors._
import akka.actor._
import akka.stream.Materializer
import akka.util.Timeout
import javax.inject._
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc._

import scala.concurrent.duration._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents)(implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {

  def index: Action[AnyContent] = Assets.versioned("index.html")

  def ws: WebSocket = WebSocket.accept[JsValue, JsValue] { request =>
    implicit val timeout: Timeout = Timeout(1.second)
    ActorFlow.actorRef { out =>
      StockActor.props(out)
    }
  }

}
