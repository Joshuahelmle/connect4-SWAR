package de.htwg.se.connect4.controller.controllerComponent.controllerBaseImpl

import de.htwg.se.connect4.aview.Tui
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.{Board, Color}
import de.htwg.se.connect4.model.playerComponent
import de.htwg.se.connect4.model.playerComponent.Player
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class InGameStateSpec extends AnyWordSpec with Matchers {

  "An InGameState" when {

    "gets integer input" should {
      val board = new Board(2, 3, false)
      val players: List[Player] = playerComponent.Player("test1", Color.RED) :: playerComponent.Player("test2", Color.YELLOW) :: Nil
      val controller = new Controller(board, players)
      val tui = new Tui(controller)
      controller.state = InGameState(controller)


      tui.processInputLine("1 2", board)

      "mark cell and set to true" in {
        controller.board.cell(1, 2).isSet shouldBe true
      }

      "return string representation" in {
        controller.state.toString() should startWith("InGameState")
      }

    }

    "gets no integer input " should {
      val board = new Board(2, 3, false)
      val players: List[Player] = playerComponent.Player("test", Color.RED) :: Nil
      val controller = new Controller(board, players)
      val tui = new Tui(controller)
      controller.state = InGameState(controller)


      "print error message" in {
        tui.processInputLine("a", board) should startWith("Please Enter two numbers separated by a whitespace.")

      }
    }

    "gets to many arguments" should {
      val board = new Board(2, 3, false)
      val players: List[Player] = playerComponent.Player("test", Color.RED) :: Nil
      val controller = new Controller(board, players)
      val tui = new Tui(controller)
      controller.state = InGameState(controller)


      "print error message" in {
        tui.processInputLine("1 2 3", board) should startWith("Please Enter two numbers separated by a whitespace.")
      }
    }

    "gets input out of board" should {
      val board = new Board(2, 3, false)
      val players: List[Player] = playerComponent.Player("test", Color.RED) :: Nil
      val controller = new Controller(board, players)
      val tui = new Tui(controller)
      controller.state = InGameState(controller)

      "print error message" in {
        tui.processInputLine("5 6", board) should startWith("Please Enter two numbers separated by a whitespace.")
      }
    }

    "trigger next state to player win state" should {
      var board = new Board(2, 4, false)
      board = board.set(0, 0, Color.RED, true)
      board = board.set(0, 1, Color.RED, true)
      board = board.set(0, 2, Color.RED, true)
      board = board.set(0, 3, Color.RED, true)
      val players: List[Player] = Player("test1", Color.RED, 1) :: Player("test2", Color.YELLOW, 0) :: Nil
      val controller = new Controller(board, players)
      controller.state = InGameState(controller)

      "change state correctly" in {
        controller.state.nextState() shouldBe PlayerWinState(controller, "test1")

      }

    }

  }
}
