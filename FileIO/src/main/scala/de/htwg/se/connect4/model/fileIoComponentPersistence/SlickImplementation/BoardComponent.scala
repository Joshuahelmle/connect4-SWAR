package de.htwg.se.connect4.model.fileIoComponentPersistence.SlickImplementation

import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.Color.Color

import slick.jdbc.PostgresProfile.api._
import slick.ast.BaseTypedType
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.JdbcType
import slick.lifted.ProvenShape



class BoardComponent(tag: Tag) extends Table[(Int, Int, Int, Option[String], Boolean)](tag, "boards") {

  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def xValue = column[Int]("X_Value")
  def yValue = column[Int]("Y_Value")
  def color = column[String]("Color")
  def isSet = column[Boolean]("Is_Set")

  def * = (id, xValue, yValue, color.?, isSet)

}
