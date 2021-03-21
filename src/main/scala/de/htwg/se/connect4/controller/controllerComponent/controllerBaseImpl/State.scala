package de.htwg.se.connect4.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.connect4.model.playerComponent.Player

case class State(currentPlayerIndex: Int = 0, players: List[Player], state: String)

object State {
  import play.api.libs.json._
  implicit val gridWrites = Json.writes[State]
  implicit val gridReads = Json.reads[State]

}
