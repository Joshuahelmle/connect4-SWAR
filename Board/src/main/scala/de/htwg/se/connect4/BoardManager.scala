package de.htwg.se.connect4

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers
import akka.stream
import akka.stream.{ActorMaterializer, Materializer}
import de.htwg.se.connect4.controller.Controller
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.Color.Color
import play.api.libs.json.Json

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object BoardManager {
  val connectionInterface = "0.0.0.0"
  val connectionPort: Int = sys.env.getOrElse("PORT", 9003).toString.toInt

  def main(args: Array[String]) {
    implicit val actorSystem: ActorSystem = ActorSystem("actorSystem")
    implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

    val controller: Controller = new Controller()

    val route = concat(
      pathPrefix("board") {
        concat(
          get {
            complete(HttpEntity(ContentTypes.`application/json`, controller.board.getBoardAsString(controller.board.cells)))
          }, post {
            entity(as[String]) { jsonString => {
              println(jsonString)
              val json = Json.parse(jsonString)
              val col = (json \ "col").as[Int]
              val color = (json \ "color" \ "color").as[Color]
              val possible = controller.set(col, color)
              println(possible._2)
              if (possible._1) {
                println(possible._2)
                val payload = Json.obj("idx" -> possible._2)
                complete(HttpEntity(ContentTypes.`application/json`, payload.toString()))
              } else {
                complete(StatusCodes.Forbidden, HttpEntity(ContentTypes.`application/json`, ""))
              }
            }
            }
          }

            )
          },
        )

    val bindingFuture = Http().bindAndHandle(route, connectionInterface, connectionPort)

    println(s"Server online at http://$connectionInterface:$connectionPort/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => actorSystem.terminate()) // and shutdown when done
  }
}
