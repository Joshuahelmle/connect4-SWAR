package de.htwg.se.connect4.model.boardComponent.boardBaseImpl

import de.htwg.se.connect4.util.EnumRead
import play.api.libs.json.{Json, Writes}

object Color extends Enumeration {

  type Color = Value

  val RED = Value("red")
  val YELLOW = Value("yellow")
  val EMPTY = Value("empty")


  implicit val colorWrites = new Writes[Color] {
    def writes(color: Color) = Json.obj("color" -> color)
  }


  implicit val colorReads = EnumRead.enumReads(Color)

  def toEnum(string: String): Color = {
    string match {
      case "red" => Color.RED
      case "yellow" => Color.YELLOW
      case "empty" => Color.EMPTY
    }
  }


}


