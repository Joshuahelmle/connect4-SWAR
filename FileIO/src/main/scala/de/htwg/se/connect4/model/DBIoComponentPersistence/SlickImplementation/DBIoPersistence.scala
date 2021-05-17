package de.htwg.se.connect4.model.DBIoComponentPersistence.SlickImplementation

import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.{Board, Cell, Color, Matrix}
import de.htwg.se.connect4.model.DBIoComponentPersistence.DBIoPersistenceInterface
import de.htwg.se.connect4.model.playerComponent.Player
import javax.xml.datatype.DatatypeConstants
import org.mongodb.scala.bson.ObjectId
import slick.jdbc.JdbcBackend.Database
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._

import scala.::
import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class DBIoPersistence extends DBIoPersistenceInterface {


  val db = Database.forURL(
    "jdbc:postgresql://localhost:5432/postgres",
    "root",
    "1234",
    null,
    "org.postgresql.Driver"
  )
  val games = TableQuery[GameComponent]
  val boards = TableQuery[BoardComponent]
  val playersT = TableQuery[PlayerComponent]


  val setup = DBIO.seq(
    boards.schema.createIfNotExists,
    playersT.schema.createIfNotExists,
    games.schema.createIfNotExists
  )
  db.run(setup)

  val setupFuture = db.run(setup);

  /*
  def setupDB(): Unit ={
    db.run(setup)
    println("Setup done.")
    create(List(Player("Josh", Color.RED, 21), Player("Test", Color.YELLOW, 21)), 5, 5)
  }
  */

  override def create(players: List[Player], rows: Int, cols: Int): String = {
    //returns inserted row with auto increment id
    val gameIdQuery = (games returning games.map(_.id)) += ((None, rows, cols))
    val gameId = Await.result(db.run(gameIdQuery), Duration("10s"))

    val playerIDQuery = (playersT returning playersT.map(_.id)) += ((None, players(0).playerName, players(0).color.toString, players(0).piecesLeft, gameId))
    val player2IDQuery = (playersT returning playersT.map(_.id)) += ((None, players(1).playerName, players(1).color.toString(), players(1).piecesLeft, gameId))
    val player1ID = Await.result(db.run(playerIDQuery), Duration("10s"))
    val player2ID = Await.result(db.run(player2IDQuery), Duration("10s"))

    for (row <- 0 until rows) {
      for (col <- 0 until cols) {
        val cellQuery = (boards returning boards.map(_.id)) += ((None, row, col, gameId, Some(Color.EMPTY.toString()), false))
        Await.result(db.run(cellQuery), Duration("10s"))
      }
    }
    gameId.toString
  }

  override def update(id: String, players: List[Player], board: Board): Boolean = {

    val actions = DBIO.sequence(players.map(player => {
      playersT.filter(_.name === player.playerName).update((None, player.playerName, player.color.toString(), player.piecesLeft, id.toInt))
    }))
    Await.result(db.run(actions), Duration("10s"))

    var cells = new ListBuffer[(Option[Int], Int, Int, Int, Option[String], Boolean)]()
    for (row <- 0 until board.sizeOfRows) {
      for (col <- 0 until board.sizeOfCols) {
        val current = board.cell(row, col)
        cells.addOne((None, row, col, id.toInt, Some(current.color.toString()), current.isSet))
        //cells += boards.filter(_.gameID === id).filter(_.xValue === row).filter(_.yValue === col).update()

      }
    }

    val boardActions = DBIO.sequence(cells.toList.map(cell => {
      boards.filter(_.gameID === id.toInt).filter(_.xValue === cell._2).filter(_.yValue === cell._3).update(cell)
    }))

    Await.result(db.run(boardActions), Duration("10s"))


    true

  }

  override def delete(id: String): Boolean = {
    Await.result(db.run(boards.filter(_.gameID === id.toInt).delete), Duration("10s"))
    true
  }

  override def read(id: String): Option[(List[Player], Board)] = {
    val q = playersT.filter(_.boardID === id.toInt).result
    val playerList = Await.result(db.run(q), Duration("10s"))
    val players = List[Player](new Player(playerList(0)._2, Color.toEnum(playerList(0)._3), playerList(0)._4), new Player(playerList(1)._2, Color.toEnum(playerList(1)._3), playerList(1)._4))
    val gameq = games.filter(_.id === id.toInt).result
    val game = Await.result(db.run(gameq), Duration("10s"))
    val newBoard = new Board(game.head._2, game.head._3, false)
    val cellq = boards.filter(_.gameID === id.toInt).result
    val cellList = Await.result(db.run(cellq), Duration("10s"))
    cellList.foreach(cell => {
      newBoard.set(cell._2, cell._3, Color.toEnum(cell._5.get), cell._6)
    })

    Some(players, newBoard)
  }

  override def test(): String = ???

}
