package de.htwg.se.connect4.model.boardComponent.boardBaseImpl

import Color.Color
import com.google.inject.Inject
import de.htwg.se.connect4.model.boardComponent.BoardInterface

import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks.{break, breakable}


case class Board @Inject() (cells: Matrix[Cell]) extends BoardInterface {

  def this(sizeOfRows: Int, sizeOfCol: Int, isSet: Boolean) = this(new Matrix[Cell](sizeOfRows, sizeOfCol, Cell(isSet)))

  def sizeOfRows: Int = cells.sizeOfRows

  def sizeOfCols: Int = cells.rows(0).length

  def cell(row: Int, col: Int): Cell = cells.cell(row, col)

  def col(col: Int): Set = Set(cells.rows.map(row => row(col)))

  def row(row: Int): Set = Set(cells.rows(row))

  def set(row: Int, col: Int, color: Color.Value, isSet: Boolean): Board = copy(cells.replaceCell(row, col, Cell(isSet, color)))

  def checkRow(row: Int, color: Color): Boolean = {
    var pieces = new ListBuffer[Color]()
    for (col <- 0 until sizeOfCols) {
      pieces += cell(row, col).color
    }
    var counter = 0
    breakable {
      for (elem <- pieces) {
        if (elem.equals(color)) counter += 1 else counter = 0
        if(counter >= 4) break
      }
    }


    if (counter >= 4) true else false
  }

  def checkCols(col: Int, color: Color): Boolean = {
    var pieces = new ListBuffer[Color]()
    for (row <- 0 until sizeOfRows) {
      pieces += cell(row, col).color
    }
    var counter = 0
    breakable {
      for (elem <- pieces) {
        if (elem.equals(color)) counter += 1 else counter = 0
        if(counter >= 4) break
      }
    }


    if (counter >= 4) true else false

  }

  def checkDiagonal(row: Int, col: Int, playerColor: Color): Boolean =
    checkDiagonalRight(row, col, playerColor) || checkDiagonalLeft(row, col, playerColor)


  private def checkDiagonalRight(row: Int, col: Int, playerColor: Color): Boolean = {
    var rowCounter = row
    var colCounter = col

    while (rowCounter != 0 && colCounter != 0) {
      rowCounter -= 1
      colCounter -= 1
    }

    var counter = 0
    while (rowCounter < sizeOfRows && colCounter < sizeOfCols && counter < 4) {
      val color = cell(rowCounter, colCounter).color

      if (color.equals(playerColor)) counter += 1 else counter = 0
      rowCounter += 1
      colCounter += 1
    }

    if (counter == 4) true else false

  }

  private def checkDiagonalLeft(row: Int, col: Int, playerColor: Color): Boolean = {
    var rowCounter = row
    var colCounter = col
    while (colCounter > 0 && rowCounter < sizeOfRows - 1) {
      rowCounter += 1
      colCounter -= 1
    }

    var counter = 0
    while (colCounter < sizeOfCols && rowCounter >= 0 && counter <4) {
      val color = cell(rowCounter, colCounter).color

      if (color.equals(playerColor)) counter += 1 else counter = 0
      rowCounter -= 1
      colCounter += 1
    }

    if (counter == 4) true else false
  }

  def getBoardAsString(matrix: Matrix[Cell]): String = {
    val rows = matrix.sizeOfRows
    val cols = matrix.sizeOfCols
    var returnString = ""
    val oneLine = " __ " * cols

    for {
      row <- 0 until rows
      col <- 0 until cols

    } {

      if (col == 0) returnString += "\n" + oneLine + "\n"
      if (matrix.cell(row, col).isSet) {
        matrix.cell(row, col).color match {
          case Color.RED => returnString += " r |"
          case Color.YELLOW => returnString += " y |"
          case Color.EMPTY => returnString += " - |"


        }
      } else {
        returnString += " - |"
      }
    }

    returnString
  }

  def getCells: Matrix[Cell] = cells
}


case class Creator() {
  def sizeOfBoard(sizeOfRows: Int, sizeOfCols: Int): Board = {
    new Board(sizeOfRows, sizeOfCols, false)
  }
}

object Board {
  import play.api.libs.json._
  implicit val gridWrites = Json.writes[Board]
  implicit val gridReads = Json.reads[Board]
}

