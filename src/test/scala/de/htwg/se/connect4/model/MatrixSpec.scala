package de.htwg.se.connect4.model

import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.{Cell, Color, Matrix}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class MatrixSpec extends AnyWordSpec with Matchers {

  "A Matrix is part of the game board. A Matrix" when {

    "is initialized" should {
      val matrix = new Matrix[Cell](2, 3, Cell(false))

      "contains empty cells" in {
        matrix.rows(0) should be(Vector[Cell](Cell(false), Cell(false), Cell(false)))
        matrix.cell(0, 0) should be(Cell(false))
        matrix.cell(0, 1) should be(Cell(false))
        matrix.cell(0, 2) should be(Cell(false))
        matrix.cell(1, 0) should be(Cell(false))
        matrix.cell(1, 1) should be(Cell(false))
        matrix.cell(1, 2) should be(Cell(false))
      }

      "has 2 rows and 3 cols" in {
        matrix.sizeOfRows should be(2)
        matrix.sizeOfCols should be(3)
      }

    }

    "containing a cell" should {
      val matrix = new Matrix[Cell](2, 3, Cell(false))

      "return a cell correctly" in {
        matrix.cell(1, 1) should be(Cell(false))

      }

    }

    "replaces a cell" should {
      val matrix = new Matrix[Cell](2, 3, Cell(false))
      val newMatrix = matrix.replaceCell(1, 1, Cell(true, Color.RED))

      "and return it correctly" in {
        newMatrix.cell(1, 1) should be(Cell(true, Color.RED))
      }
    }
  }

}
