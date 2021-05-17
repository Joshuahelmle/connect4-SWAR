package de.htwg.se.connect4.controller



import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Get
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.unmarshalling.Unmarshal
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.Color.Color
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.{Board, BoardSizeStrategy}
import play.api.libs.json
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

class Controller {

  var board = BoardSizeStrategy.execute(6, 7)
  loadBoard()
  println(board.getBoardAsString(board.cells))

  def loadBoard() {
    implicit val actorSystem: ActorSystem = ActorSystem("actorSystem")
    implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

    val responseFuture: Future[HttpResponse] = Http().singleRequest(Get("http://localhost:9002/game"))
    responseFuture.onComplete {
      case Success(res) => {
        println(res)
        val entityAsText: Future[String] = Unmarshal(res.entity).to[String]
        entityAsText.onComplete {
          case Success(body) => load(body)
          case Failure(_) => println("something Wrong")
        }
      }
      case Failure(_) => sys.error("something wrong")
    }
  }

  def loadBoard(id : String): Unit ={
    implicit val actorSystem: ActorSystem = ActorSystem("actorSystem")
    implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher

    val responseFuture: Future[HttpResponse] = Http().singleRequest(Get("http://localhost:9002/game"))
    responseFuture.onComplete {
      case Success(res) => {
        println(res)
        val entityAsText: Future[String] = Unmarshal(res.entity).to[String]
        entityAsText.onComplete {
          case Success(body) => load(body)
          case Failure(_) => println("something Wrong")
        }
      }
      case Failure(_) => sys.error("something wrong")
    }
  }
 def load(payload : String) : Board = {
    println(payload)
    val json: JsValue = Json.parse(payload)
    val sizeOfRows = (json \ "board" \ "row").get.toString.toInt
    val sizeOfCols = (json \ "board" \ "col").get.toString.toInt

    var board = BoardSizeStrategy.execute(sizeOfRows, sizeOfCols)



    for (index <- 0 until sizeOfRows * sizeOfCols) {
    val row = (json \ "board" \ "cells" \\ "row") (index).as[Int]
    val col = (json \ "board" \ "cells" \\ "col") (index).as[Int]
    val cell = (json \\ "cell") (index)
    val isSet = (cell \ "isSet").as[Boolean]
    val color = (cell \ "color" \ "color").as[Color]

    board = board.set(row, col, color, isSet)

  }
  board
  }

  def set(col : Int, color : Color)  : (Boolean, Int) =  {

    var idx = board.sizeOfRows-1
    println(idx)
    var found = false;
    while(!found && idx >= 0){
      found = !board.cell(idx,col).isSet
      println(idx, col, found)
      idx -= 1
    }

    if(found){
      board = board.set(idx+1, col,color,true)
      return (true, idx+1)
    }
    (false, -1)
  }
}
