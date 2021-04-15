package de.htwg.se.connect4.model.playerComponent

import play.api.libs.json.Json
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.Color

case class Player(playerName: String, color: Color.Value, piecesLeft: Int = 21) {

  override def toString: String = playerName

  def setPiece(): Player = copy(piecesLeft = piecesLeft - 1)
}

object Player {
  implicit val playerWrites = Json.writes[Player]
  implicit val playerReads = Json.reads[Player]


}
