package de.htwg.se.connect4.model.DBIoComponentPersistence.SlickImplementation
import slick.jdbc.PostgresProfile.api._

class GameComponent(tag: Tag) extends Table[(Option[Int], Int, Int)](tag, "games") {

  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def sizeOfRows = column[Int]("SizeOfRows")
  def sizeOfCols = column[Int]("SizeOfCols")
  def * = (id.?, sizeOfRows, sizeOfCols)
}
