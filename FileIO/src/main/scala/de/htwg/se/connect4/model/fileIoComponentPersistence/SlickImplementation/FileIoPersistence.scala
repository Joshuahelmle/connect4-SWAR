package de.htwg.se.connect4.model.fileIoComponentPersistence.SlickImplementation

import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.connect4.model.fileIoComponentPersistence.FileIoPersistenceInterface
import de.htwg.se.connect4.model.playerComponent.Player
import slick.jdbc.JdbcBackend.Database
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class FileIoPersistence extends FileIoPersistenceInterface {

  val db = Database.forURL(
    "jdbc:postgresql://database:5432/fileIo",
    "root",
    "1234",
    null,
    "org.postgresql.Driver"
  )

  val boards = TableQuery[BoardComponent]
  val players = TableQuery[PlayerComponent]

  val setup = DBIO.seq(
    boards.schema.createIfNotExists,
    players.schema.createIfNotExists
  )
  db.run(setup)


  override def create(players: List[Player], rows: Int, cols: Int): Int = {
    //returns inserted row with auto increment id
    val boardIdQuery = (boards returning boards.map(_.id)) += (None, rows, cols, Option.empty, false)
    val boardId = Await.result(db.run(boardIdQuery), Duration("10s"))
    boardId
  }

  override def update(id: Int, players: List[Player], board: Board): Boolean = ???

  override def delete(id: Int): Boolean = ???

  override def read(id: Int): Option[(List[Player], Board)] = ???
}
