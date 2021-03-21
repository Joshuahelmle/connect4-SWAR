package de.htwg.se.connect4.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.Color
import de.htwg.se.connect4.model.playerComponent.Player
import de.htwg.se.connect4.util.Command

class SetCommand(row: Int, col: Int, currentPlayer: Player, controller: Controller, isSet: Boolean) extends Command {

  override def doStep: Unit = controller.board = controller.board.set(row, col, currentPlayer.color, isSet)

  override def undoStep: Unit = controller.board = controller.board.set(row, col, Color.EMPTY, false)

  override def redoStep: Unit = controller.board = controller.board.set(row, col, currentPlayer.color, isSet)
}
