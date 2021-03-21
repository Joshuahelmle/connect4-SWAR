package de.htwg.se.connect4.model.boardComponent.boardBaseImpl

import play.api.libs.json.Json

case class Matrix[Cell](rows: Vector[Vector[Cell]]) {

  def this(sizeOfRows: Int, sizeOfCol: Int, cell: Cell) = this(Vector.tabulate(sizeOfRows, sizeOfCol) { (row, col) => cell })

  def sizeOfRows: Int = rows.size

  def sizeOfCols: Int = rows(0).length

  def cell(row: Int, col: Int): Cell = rows(row)(col)

  def replaceCell(row: Int, col: Int, cell: Cell): Matrix[Cell] = copy(rows.updated(row, rows(row).updated(col, cell)))

}

object Matrix {
  implicit val matrixWrites = Json.writes[Matrix[Cell]]
  implicit val matrixReads = Json.reads[Matrix[Cell]]
}
