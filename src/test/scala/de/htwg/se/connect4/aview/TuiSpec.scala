package de.htwg.se.connect4.aview

import de.htwg.se.connect4.controller.controllerComponent.controllerBaseImpl.{Controller, InGameState}
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.{Board, Color}
import de.htwg.se.connect4.model.playerComponent
import de.htwg.se.connect4.model.playerComponent.Player
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec




class TuiSpec extends AnyWordSpec with Matchers {


  "A Tui represents the game with text. A Tui" when {


    "gets input to create new Board with 'n' " should {

      val board = new Board(2, 3, false)
      val players: List[Player] = playerComponent.Player("test", Color.RED) :: playerComponent.Player("test2", Color.YELLOW) :: Nil
      val controller = new Controller(board, players)
      val tui = new Tui(controller)

      tui.processInputLine("n", board)

      "return new Board" in {
        controller.board should be(new Board(6, 7, false))
      }
    }

  }

  "gets 'q' input" should {
    val board = new Board(2, 3, false)
    val players: List[Player] = playerComponent.Player("test", Color.RED) :: Nil
    val controller = new Controller(board, players)
    val tui = new Tui(controller)

    "exit game" in {
      tui.processInputLine("q", board) should startWith("exit game")
    }
  }

  "gets 'u' input" should {
    val board = new Board(2, 3, false)
    val players: List[Player] = playerComponent.Player("test", Color.RED) :: playerComponent.Player("test2", Color.YELLOW) :: Nil
    val controller = new Controller(board, players)
    val tui = new Tui(controller)
    controller.state = InGameState(controller)

    "doStep" in {
      tui.processInputLine("0 0", board)
      controller.board.cell(0,0).isSet shouldBe true
    }

    "undo step" in {

      tui.processInputLine("u", board)
      controller.board.cell(0,0).isSet shouldBe false
    }

    tui.processInputLine("12", board)

    "redo step" in {
      tui.processInputLine("r", board)
      controller.board.cell(1,2).isSet shouldBe true
    }
  }
}
