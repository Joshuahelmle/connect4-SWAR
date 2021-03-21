package de.htwg.se.connect4.model

import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.BoardSizeStrategy
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class BoardSizeStrategySpec extends AnyWordSpec with Matchers {

  "A BoardSizeStrategy" when {

    "called with defaultStrategy" should {

      "create default board" in {

        val board = BoardSizeStrategy.execute((6, 7))

        board.sizeOfCols shouldBe 7
        board.sizeOfRows shouldBe 6
      }
    }

    "called with tinyStrategy" should {
      "create tiny board" in {
        val board = BoardSizeStrategy.strategy((2, 3))

        board.sizeOfRows shouldBe 2
        board.sizeOfCols shouldBe 3

      }
    }

    "called with bigStrategy" should {
      "create big board" in {
        val board = BoardSizeStrategy.strategy((15,16))

        board.sizeOfCols shouldBe  16
        board.sizeOfRows shouldBe 15
      }
    }

    "called with random values" should {
      "create default board" in {
        val board = BoardSizeStrategy.execute((4,4))

        board.sizeOfRows shouldBe 6
        board.sizeOfCols shouldBe 7
      }
    }
  }

}
