package de.htwg.se.connect4.model.boardComponent.boardBaseImpl

import de.htwg.se.connect4.model.boardComponent.CellInterface
import play.api.libs.json.Json

case class Cell(isSet: Boolean, color: Color.Value = Color.EMPTY) extends CellInterface

object Cell {
  implicit val cellWrites = Json.writes[Cell]
  implicit val cellReads = Json.reads[Cell]
}
