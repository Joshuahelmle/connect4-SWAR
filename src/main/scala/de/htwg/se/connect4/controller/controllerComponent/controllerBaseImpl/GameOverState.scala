package de.htwg.se.connect4.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.connect4.controller.controllerComponent.ControllerInterface
import de.htwg.se.connect4.model.boardComponent.BoardInterface

case class GameOverState(controller: ControllerInterface) extends ControllerState {

  override def handle(input: String, board: BoardInterface): String = ""

  override def nextState(): ControllerState = InGameState(controller)

  override def stateString(): String = "Game over. No pieces left. Press 'n' to start a new game."

  override def toString: String = "GameOverState"
}
