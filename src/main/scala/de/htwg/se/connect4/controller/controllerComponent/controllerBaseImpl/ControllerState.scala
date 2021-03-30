package de.htwg.se.connect4.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.connect4.model.boardComponent.BoardInterface

import scala.util.Try

abstract class ControllerState {

  def handle(input: String, board: BoardInterface): Try[String]

  def nextState(): ControllerState

  def stateString(): String

  def toString(): String

}
