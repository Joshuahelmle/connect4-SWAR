package de.htwg.se.connect4.controller

import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Post
import com.google.inject.Guice
import de.htwg.se.connect4.FileIOServerModule
import de.htwg.se.connect4.model.boardComponent.BoardInterface
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.{Board, BoardSizeStrategy, Color}
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.Color.Color
import de.htwg.se.connect4.model.fileIoComponent.{FileIoInterface, State}
import de.htwg.se.connect4.model.DBIoComponentPersistence.DBIoPersistenceInterface
import de.htwg.se.connect4.model.DBIoComponentPersistence.MongoDBImplementation.BoardComponent
import de.htwg.se.connect4.model.playerComponent.Player
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import play.api.libs.json._

import scala.collection.mutable.ListBuffer

class FileIOController {

  val injector = Guice.createInjector(new FileIOServerModule)
  val fileIO = injector.instance[FileIoInterface]
  val databaseIO = injector.instance[DBIoPersistenceInterface]
  val games = Seq("board.json")

  def getAsJson(id : Int): JsValue = {
    val (board, state) = fileIO.load
    gridToJson(board, state)
  }

  def getAllGames() : JsValue = {
  Json.obj("games" -> Json.toJson(for {game <- games } yield {
      Json.obj(
        "game" -> game
      )
    })

    )
  }

  def save(payload : String): Unit ={
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

    val currentPlayerIndex = (json \ "currentPlayerIndex").get.toString().toInt
    var players: List[Player] = List()

    for (index <- 0 until 2) {
      val name = (json \ "players" \\ "name") (index).as[String]
      val piecesLeft = (json \ "players" \\ "piecesLeft") (index).as[Int]
      val playerColor = (json \ "players") (index)("color")
      val color = (playerColor \ "color").as[Color]


      val player = new Player(name, color, piecesLeft)
      players = players ::: List(player)

    }

    val state = (json \ "state").as[String]

    val stateToLoad = new State(currentPlayerIndex, players, state)
    val id = (json \ "id").as[String]
    fileIO.save(board, stateToLoad)
    databaseIO.update(id, players, board)
  }

  def gridToJson(board: BoardInterface, state: State) = {
    Json.obj(
      "currentPlayerIndex" -> JsNumber(state.currentPlayerIndex),
      "state" -> JsString(state.state),
      "players" -> Json.toJson(
        for {
          index <- state.players.indices

        } yield {
          Json.obj(
            "name" -> state.players(index).playerName,
            "color" -> state.players(index).color,
            "piecesLeft" -> state.players(index).piecesLeft,
          )
        }

      ),
      "board" -> Json.obj(
        "row" -> JsNumber(board.sizeOfRows),
        "col" -> JsNumber(board.sizeOfCols),
        "cells" -> Json.toJson(
          for {
            row <- 0 until board.sizeOfRows
            col <- 0 until board.sizeOfCols
          } yield {
            Json.obj(
              "row" -> row,
              "col" -> col,
              "cell" -> Json.toJson(board.cell(row, col))
            )
          }
        )
      )
    )
  }

  /*def save(id : Int) : Unit = {
    fileIO.save()
  }*/

  def create(payload : String) : String = {
    val json: JsValue = Json.parse(payload)
    var players: List[Player] = List()

    for (index <- 0 until 2) {
      val name = (json \ "players" \\ "name") (index).as[String]
      val piecesLeft = (json \ "players" \\ "piecesLeft") (index).as[Int]
      val playerColor = (json \ "players") (index)("color")
      val color = (playerColor \ "color").as[Color]


      val player = new Player(name, color, piecesLeft)
      players = players ::: List(player)

    }
    val id = databaseIO.create(players, 6, 7)

    Json.obj("id" -> id).toString()
  }


  def update(id : String) = {
    val players = List(Player("Vanessa", Color.RED, 2), Player("Test", Color.YELLOW, 21))
    var b = new Board(5,5,false)
    b = b.set(3,3,Color.RED, true)


    databaseIO.update(id, players, b)
  }

  def delete(id : String) = {
    databaseIO.delete(id)
  }

  def test(): String = {
    databaseIO.test()
  }
}
