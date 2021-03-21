package de.htwg.se.connect4.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.connect4.controller.controllerComponent.ControllerInterface
import de.htwg.se.connect4.model.boardComponent.BoardInterface

case class PlayerWinState(controller: ControllerInterface, name: String) extends ControllerState {

  override def handle(input: String, board: BoardInterface): String = ""

  override def nextState(): ControllerState = InGameState(controller)

  override def stateString(): String = "Congratulations! Player " + name + " You win."

  override def toString: String = "PlayerWinState"
}
