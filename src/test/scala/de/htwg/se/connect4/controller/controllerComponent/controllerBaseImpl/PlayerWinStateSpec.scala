package de.htwg.se.connect4.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.connect4.aview.Tui
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.{Board, Color}
import de.htwg.se.connect4.model.playerComponent
import de.htwg.se.connect4.model.playerComponent.Player
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class PlayerWinStateSpec extends AnyWordSpec with Matchers {

  "A Player Win State" when {
    val board = new Board(2, 3, false)
    val players: List[Player] = playerComponent.Player("test1", Color.RED, piecesLeft = 0) :: playerComponent.Player("test2", Color.YELLOW, piecesLeft = 0) :: Nil
    val controller = new Controller(board, players)
    val tui = new Tui(controller)

    controller.state = PlayerWinState(controller, "test1")

    "print correct string" in {
      controller.stateString should startWith ("Congratulations! Player test1 You win.")

    }

    "trigger next state" in {
      controller.state.nextState() shouldBe InGameState(controller)
    }

    "return empty string" in {
      controller.handle("", board) should startWith("")
    }

    "return string representation" in {
      controller.state.toString() should startWith("PlayerWinState")
    }


  }

}
