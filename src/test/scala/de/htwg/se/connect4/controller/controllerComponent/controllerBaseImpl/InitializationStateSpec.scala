package de.htwg.se.connect4.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.connect4.aview.Tui
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.{Board, Color}
import de.htwg.se.connect4.model.playerComponent
import de.htwg.se.connect4.model.playerComponent.Player
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class InitializationStateSpec extends AnyWordSpec with Matchers {

  "An Initialization state" when {
    val board = new Board(2, 3, false)
    val players: List[Player] = playerComponent.Player("test1", Color.RED, piecesLeft = 1) :: Nil
    val controller = new Controller(board, players)
    val tui = new Tui(controller)
    controller.state = InitializationState(controller)

    "print welcome string" in {
      controller.state.stateString() should startWith("Welcome to connect 4. Please Enter your names.")
    }

    "set should add players" in {
      controller.handle("test2", board) should startWith("")

    }

    "trigger next state" in {
      controller.state = InitializationState(controller)
      controller.state.nextState() shouldBe InGameState(controller)
    }


  }

}
