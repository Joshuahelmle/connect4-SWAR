package de.htwg.se.connect4.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.connect4.controller.controllerComponent.ControllerInterface
import de.htwg.se.connect4.model.boardComponent.BoardInterface

case class InitializationState(controller: ControllerInterface) extends ControllerState {

  override def handle(input: String, board: BoardInterface): String = controller.addPlayer(input)


  override def nextState(): ControllerState = InGameState(controller)

  override def stateString(): String = controller.getWelcomeString

  override def toString: String = "InitializationState"
}
