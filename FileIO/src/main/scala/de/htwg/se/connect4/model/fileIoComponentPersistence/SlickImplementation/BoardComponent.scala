package de.htwg.se.connect4.model.fileIoComponentPersistence.SlickImplementation

import slick.jdbc.PostgresProfile.api._



class BoardComponent(tag: Tag) extends Table[(Option[Int], Int, Int, Option[String], Boolean)](tag, "boards") {

  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def xValue = column[Int]("X_Value")
  def yValue = column[Int]("Y_Value")
  def color = column[String]("Color")
  def isSet = column[Boolean]("Is_Set")

  def * = (id.?, xValue, yValue, color.?, isSet)

}
