package de.htwg.se.connect4.model

import de.htwg.se.connect4.model.boardComponent.boardBaseImpl
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.{Cell, Color}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SetSpec extends AnyWordSpec with Matchers {

  "A set represents a row or column of cells" when {

    "a set with empty cells is created" should {
      val set = boardBaseImpl.Set(Vector[Cell](Cell(false), Cell(false)))

      "with empty cells" in {
        set.cells(0) should be(Cell(false))
        set.cells(1) should be(Cell(false))
      }
    }

    "a set with one cell set is created" should {
      val set = boardBaseImpl.Set(Vector[Cell](Cell(true, Color.RED), Cell(false)))

      "contain a cell with value" in {
        set.cells(0).isSet should be(true)
        set.cells(1).isSet should be(false)
        set.cells(0).color should be(Color.RED)
      }
    }
  }

}
