package de.htwg.se.connect4.model.fileIoComponentPersistence.SlickImplementation

import slick.ast.ColumnOption.PrimaryKey
import slick.jdbc.PostgresProfile.api._


class PlayerComponent(tag: Tag) extends Table[(Option[Int], String, String, Int)](tag, "boards") {

  val boards = TableQuery[BoardComponent]

  def id = column[Int]("id", PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def color = column[String]("color")

  def piecesLeft = column[Int]("pieces left")

  def boardFK = foreignKey("boardFK", id, boards)(
    _.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

  override def * = (id.?, name, color, piecesLeft)
}
