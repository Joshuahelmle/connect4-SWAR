package de.htwg.se.connect4.model.fileIoComponent.fileIoXmlImpl

import de.htwg.se.connect4.controller.controllerComponent.controllerBaseImpl.{Controller, State}
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.{Board, Color}
import de.htwg.se.connect4.model.playerComponent.Player
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class FileIoSpec extends AnyWordSpec with Matchers {

  "A File IO" when {

    "loaded" should {
      var board = new Board(2, 4, false)
      val players: List[Player] = Player("test1", Color.RED, 1) :: Player("test2", Color.YELLOW, 0) :: Nil
      val controller = new Controller(board, players)

      "get correct board and state" in {

        val fileIo = new FileIO
        fileIo.save(board, new State(controller.currentPlayerIndex, players, controller.state.toString()))
        val oldBoard = board.copy()
        val (newBoard, state) = fileIo.load

        newBoard.sizeOfCols shouldBe 7
      }
    }
  }
}
