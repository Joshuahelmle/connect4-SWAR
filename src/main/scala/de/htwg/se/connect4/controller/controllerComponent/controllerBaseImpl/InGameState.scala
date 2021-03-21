package de.htwg.se.connect4.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.connect4.controller.controllerComponent.ControllerInterface
import de.htwg.se.connect4.model.boardComponent.BoardInterface

import scala.util.{Failure, Success, Try}

case class InGameState(controller: ControllerInterface) extends ControllerState {

  override def handle(input: String, board: BoardInterface): String = evaluateInput(input, board)

  def evaluateInput(input: String, board: BoardInterface): String = {
    val list: Try[List[Int]] = Try(input.toList.filter(c => c != ' ').map(c => c.toString.toInt))

    list match {
      case Success(v) =>

        if (list.get.size == 2) {
          val row: Int = list.get.head
          val column: Int = list.get(1)
          if (row < board.sizeOfRows && column < board.sizeOfCols) controller.setCol(column)
          else controller.getIncorrectInputMessage
        }

        else controller.getIncorrectInputMessage

      case Failure(e) => controller.getIncorrectInputMessage
    }
  }

  override def nextState(): ControllerState =
    if (controller.playersHaveNoPiecesLeft) GameOverState(controller)
    else PlayerWinState(controller, controller.getPlayers(controller.getCurrentPlayerIndex).playerName)

  override def stateString(): String = controller.boardToString + "\n" + controller.getPlayerDemandString

  override def toString: String = "InGameState"
}
