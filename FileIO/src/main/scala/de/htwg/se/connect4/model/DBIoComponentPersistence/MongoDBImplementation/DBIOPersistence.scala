package de.htwg.se.connect4.model.DBIoComponentPersistence.MongoDBImplementation




import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Post
import de.htwg.se.connect4.model.DBIoComponentPersistence.DBIoPersistenceInterface
import de.htwg.se.connect4.model.boardComponent.BoardInterface
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.{Board, Color}
import de.htwg.se.connect4.model.fileIoComponent.State
import de.htwg.se.connect4.model.playerComponent
import de.htwg.se.connect4.model.playerComponent.Player
import org.bson.codecs.configuration.CodecRegistries._
import org.bson.codecs.configuration.CodecRegistry
import org.bson.types.ObjectId
import org.mongodb.scala._
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.model.Filters.equal
import play.api.libs.json.{JsNumber, JsString, Json}

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.util.{Failure, Success}


class DBIOPersistence extends DBIoPersistenceInterface {

  val mongoClient: MongoClient = MongoClient("mongodb://root:example@localhost/admin");
  val customCodecs = fromProviders(classOf[PlayerComponent], classOf[BoardComponent], classOf[GameComponent])
  val codecRegistry: CodecRegistry = fromRegistries(customCodecs, DEFAULT_CODEC_REGISTRY)
  val db: MongoDatabase = mongoClient.getDatabase("games").withCodecRegistry(codecRegistry)
  //val playersT : MongoCollection[PlayerComponent] = db.getCollection("players")
  val gamesT: MongoCollection[GameComponent] = db.getCollection("games")
  val gamesD : MongoCollection[Document] = db.getCollection("games")


  //playersT.drop().toFuture()

  // playersT.insertOne(josh).toFuture()

  override def create(players: List[playerComponent.Player], rows: Int, cols: Int): String = {
    val b = new Board(rows, cols, false)
    val cells = new ListBuffer[BoardComponent]

    for (row <- 0 until b.sizeOfRows) {
      for (col <- 0 until b.sizeOfCols) {
        val isSet = b.cell(row, col).isSet
        println(isSet)
        val cell = BoardComponent(row, col, isSet, Color.EMPTY.toString)
        cells += cell
      }
    }

    cells.foreach(c => println(s"x: ${c.xValue}\t y: ${c.yValue}\t isSet: ${c.isSet}"))
    val game = GameComponent(
      Seq(
        PlayerComponent("Josh", Color.RED.toString, 21),
        PlayerComponent("Test", Color.YELLOW.toString, 21)
      ), cells.toList,
      b.sizeOfRows, b.sizeOfCols
    )

    //game.board.rows.foreach(v => v.foreach(c => println(c.isSet)))
    Await.result(gamesT.insertOne(game).toFuture(), Duration("10s"))

    val allGames = Await.result(gamesT.find().toFuture(), Duration("10s"))

    val payload = gridToJson(b, new State(0, players, ""))
    implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.executionContext
    Http().singleRequest(Post("http://localhost:9003/newGame", payload.toString()))

    allGames.foreach(println)

    game._id.toString
  }

  override def update(id: String, players: List[playerComponent.Player], board: Board): Boolean = {
    val cells = for {
      row <- 0 until board.sizeOfRows
      col <- 0 until board.sizeOfCols
      current = board.cell(row, col)
      cell = BoardComponent(row, col, current.isSet, current.color.toString)
    } yield cell

    val game = GameComponent(new ObjectId(id),
      Seq(
        PlayerComponent(players(0).playerName, players(0).color.toString, players(0).piecesLeft),
        PlayerComponent(players(1).playerName, players(1).color.toString, players(1).piecesLeft)
      ), cells.toList,
      board.sizeOfRows, board.sizeOfCols)


    Await.result(gamesT.replaceOne(equal("_id", new ObjectId(id)), game).toFuture(), Duration("10s"))


    true
  }

  override def delete(id: String): Boolean = {
    Await.result(gamesT.findOneAndDelete(equal("_id", new ObjectId(id))).toFuture(), Duration("10s"))
    true
  }

  override def read(id: String): Option[(List[playerComponent.Player], Board)] = {
    val game = Await.ready(gamesT.find(equal("_id", new ObjectId(id))).toFuture(), Duration("10s"))
    var ret: Option[(List[playerComponent.Player], Board)] = None
    game.onComplete {
      case Success(value) => {
        val players = List(
          new Player(value(0).players(0).name, Color.toEnum(value(0).players(0).color), value(0).players(0).piecesLeft),
          new Player(value(0).players(1).name, Color.toEnum(value(0).players(1).color), value(0).players(1).piecesLeft))
        val b = new Board(value(0).sizeOfRows, value(0).sizeOfCols, false)
        value(0).board.foreach(c => {
          b.set(c.xValue, c.yValue, Color.toEnum(c.color), c.isSet)
        })
        ret = Some((players, b))
      }
      case Failure(exception) => ret = None
    }
    ret
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

  def test() : String = {
    val t = Await.result(gamesD.find().toFuture(), Duration("10s"))
    t.head.toString()
  }

}