package de.htwg.se.connect4.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.connect4.model.boardComponent.BoardInterface

abstract class ControllerState {

  def handle(input: String, board: BoardInterface): String

  def nextState(): ControllerState

  def stateString(): String

  def toString(): String

}
