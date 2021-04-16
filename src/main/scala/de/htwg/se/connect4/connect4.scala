package de.htwg.se.connect4

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.client.RequestBuilding.Get
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.google.inject.Guice
import com.sun.xml.internal.ws.util.Pool.Unmarshaller
import de.htwg.se.connect4.aview.Tui
import de.htwg.se.connect4.aview.gui.SwingGui
import de.htwg.se.connect4.controller.controllerComponent.ControllerInterface
import de.htwg.se.connect4.model.boardComponent.BoardInterface
import play.api.libs.json.Json

import scala.concurrent.Future
import scala.io.StdIn.readLine
import scala.util.{Failure, Success}

object connect4 {

  val injector = Guice.createInjector(new Connect4Module)
  var board = injector.getInstance(classOf[BoardInterface])

  val controller = injector.getInstance(classOf[ControllerInterface])
  val tui = new Tui(controller)
  //val gui = new SwingGui(controller)

  controller.notifyObservers


  def main(args: Array[String]): Unit = {/*
    implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.executionContext
    val responseFuture: Future[HttpResponse] = Http().singleRequest(Get("http://localhost:9002/game"))
    responseFuture.onComplete{
      case Success(res) => {
        val entityAsText : Future[String] = Unmarshal(res.entity).to[String]
        entityAsText.onComplete{
          case Success(body) => println(body)
          case Failure(_) => println("something Wrong")
        }
      }
      case Failure(_) => sys.error("something wrong")
    }*/
    var input: String = ""
    do {

      input = readLine()
      println(tui.processInputLine(input, board))
    } while (input != "q")

  }
}
