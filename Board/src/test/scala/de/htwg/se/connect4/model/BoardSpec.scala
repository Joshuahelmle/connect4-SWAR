package de.htwg.se.connect4.model


import de.htwg.se.connect4.model.boardComponent.boardBaseImpl
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.{Board, Cell, Color, Matrix}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec



class BoardSpec extends AnyWordSpec with Matchers {
  "A Board is the Gamefield of Connect4. A Board" when {

    "to be constructed" should {
      "be created with the size 6x7 and Cells should be initialized with false" in {
        val board = new Board(6, 7, false)
        board.sizeOfRows should be(6)
        board.sizeOfCols should be(7)

      }

      "for test purpose created with a Matrix of Cells" in {
        val board = new Board(Matrix[Cell](Vector(Vector(Cell(false), Cell(false)), Vector(Cell(false), Cell(false)))))

      }

      "created should be empty. For test purpose only a small board with 2x3 is created." should {
        val board = new Board(2, 3, false)

        "give access to its Cells" in {
          board.cell(0, 0) should be(Cell(false))
          board.cell(0, 1) should be(Cell(false))
          board.cell(0, 2) should be(Cell(false))
          board.cell(1, 0) should be(Cell(false))
          board.cell(1, 1) should be(Cell(false))
          board.cell(1, 2) should be(Cell(false))
        }

      }
      "get access to cols and rows" should {
        val board = new Board(2, 3, false)

        "return requested cols and rows" in {
          board.row(0) should be(boardBaseImpl.Set(Vector.fill(3)(Cell(false))))
          board.col(2) should be(boardBaseImpl.Set(Vector.fill(2)(Cell(false))))

        }

      }

      "empty" should {
        val board = new Board(2, 3, false)

        "be printed out empty" in {
          val boardAsString = board.getBoardAsString(board.cells)

          boardAsString should startWith(
            "\n" +
              " __  __  __ \n" +
              " - | - | - |\n" +
              " __  __  __ \n" +
              " - | - | - |")

        }

      }
      "first piece from red player is set" should {
        val board = new Board(2, 3, false)
        val newBoard = Board(board.cells.replaceCell(1, 1, Cell(true, Color.RED)))
        "print one cell set at second row and second col with red piece" in {
          val boardAsString = newBoard.getBoardAsString(newBoard.cells)

          boardAsString should startWith(
            "\n" +
              " __  __  __ \n" +
              " - | - | - |\n" +
              " __  __  __ \n" +
              " - | r | - |")
        }
      }

      "first piece from yellow player is set" should {
        val board = new Board(2, 3, false)
        val newBoard = Board(board.cells.replaceCell(1, 1, Cell(true, Color.YELLOW)))
        "print one cell set at second row and second col with yellow piece" in {
          val boardAsString = newBoard.getBoardAsString(newBoard.cells)

          boardAsString should startWith(
            "\n" +
              " __  __  __ \n" +
              " - | - | - |\n" +
              " __  __  __ \n" +
              " - | y | - |")
        }
      }

      "detect no color available" should {
        val board = new Board(2, 3, false)
        val newBoard = Board(board.cells.replaceCell(1, 1, Cell(true, Color.EMPTY)))

        "print cell with '-'" in {
          val boardAsString = newBoard.getBoardAsString(newBoard.cells)

          boardAsString should startWith(
            "\n" +
              " __  __  __ \n" +
              " - | - | - |\n" +
              " __  __  __ \n" +
              " - | - | - |")
        }
      }

      "checks rows correctly if match found" should {
        var board = new Board(2, 4, false)
        board = Board(board.cells.replaceCell(0, 0, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(0,1, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(0,2, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(0,3, Cell(true, Color.YELLOW)))

        "return true" in {
          board.checkRow(0, Color.YELLOW) shouldBe true


        }
      }

      "check rows correctly if no match found" should {
        var board = new Board(2, 4, false)
        board = Board(board.cells.replaceCell(0,1, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(0,2, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(0,3, Cell(true, Color.YELLOW)))

        "return false" in {
          board.checkRow(0, Color.YELLOW) shouldBe false
        }

      }

      "check rows correctly" should {
        var board = new Board(2, 4, false)
        board = Board(board.cells.replaceCell(0,0, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(0,2, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(0,3, Cell(true, Color.YELLOW)))

        "return false" in {
          board.checkRow(0, Color.YELLOW) shouldBe false
        }

      }

      "check rows" should {
        var board = new Board(2, 6, false)
        board = Board(board.cells.replaceCell(0,0, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(0,2, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(0,3, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(0,4, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(0,5, Cell(true, Color.YELLOW)))

        "retrun true" in {
          board.checkRow(0, Color.YELLOW) shouldBe true
        }
      }

      "check columns" should {
        var board = new Board(4, 6, false)
        board = Board(board.cells.replaceCell(0,2, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(1,2, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(2,2, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(3,2, Cell(true, Color.YELLOW)))

        "return true" in {
          board.checkCols(2, Color.YELLOW) shouldBe true
        }
      }

      "check columns" should {
        var board = new Board(4, 6, false)
        board = Board(board.cells.replaceCell(0,2, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(2,2, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(3,2, Cell(true, Color.YELLOW)))

        "return false" in {
          board.checkCols(2, Color.YELLOW) shouldBe false
        }
      }

      "check diagonal right" should {
        var board = new Board(4,5,false)
        board = Board(board.cells.replaceCell(0,1, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(1,2, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(2,3, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(3,4, Cell(true, Color.YELLOW)))

        "return true" in {
          board.checkDiagonal(2,3, Color.YELLOW) shouldBe true
        }

      }

      "check diagonal right" should {
        var board = new Board(4, 6, false)
        board = Board(board.cells.replaceCell(0,2, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(2,2, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(3,2, Cell(true, Color.YELLOW)))

        "return false" in {
          board.checkDiagonal(2,3, Color.YELLOW) shouldBe false
        }
      }

      "check diagonal left" should {
        var board = new Board(4, 6, false)
        board = Board(board.cells.replaceCell(1,4, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(2,3, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(3,2, Cell(true, Color.YELLOW)))

        "return false" in {
          board.checkDiagonal(2,3, Color.YELLOW) shouldBe false
        }
      }

      "check diagonal left" should {
        var board = new Board(4, 5, false)
        board = Board(board.cells.replaceCell(0,3, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(1,2, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(2,1, Cell(true, Color.YELLOW)))
        board = Board(board.cells.replaceCell(3,0, Cell(true, Color.YELLOW)))

        "return true" in {
          board.checkDiagonal(1,2, Color.YELLOW) shouldBe true
        }
      }





    }
  }

}
