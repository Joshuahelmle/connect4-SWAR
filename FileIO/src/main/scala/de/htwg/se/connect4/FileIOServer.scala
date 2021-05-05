package de.htwg.se.connect4

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.PathMatchers
import akka.stream
import akka.stream.{ActorMaterializer, Materializer}
import de.htwg.se.connect4.controller.FileIOController
import de.htwg.se.connect4.model.boardComponent.BoardInterface
import de.htwg.se.connect4.model.fileIoComponent.State
import play.api.libs.json.{JsObject, JsValue, Json}

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn


object FileIOServer {

  val connectionInterface = "0.0.0.0"
  val connectionPort: Int = sys.env.getOrElse("PORT", 9002).toString.toInt
  def main(args: Array[String]): Unit = {


    implicit val actorSystem: ActorSystem = ActorSystem("actorSystem")
    implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

    val controller: FileIOController = new FileIOController()


    val route = concat(
      path("games") {
        get {
          complete(HttpEntity(ContentTypes.`application/json`, controller.getAllGames().toString()))
        }
      },
      path("game") {
        concat(
        get {
          complete(HttpEntity(ContentTypes.`application/json`, controller.getAsJson(0).toString))
        },
        post {
          entity(as[String]){ payload =>
            controller.save(payload)
            complete(HttpEntity(ContentTypes.`text/plain(UTF-8)` ,"Game Saved!"))
          }
        })
      },
      path("newGame") {
        concat(
          get {
            complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, controller.create().toString))
          }
        )
      }
    )
    val bindingFuture = Http().bindAndHandle(route, connectionInterface, connectionPort)

    println(s"Server online at http://$connectionInterface:$connectionPort/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => actorSystem.terminate()) // and shutdown when done
  }
}
