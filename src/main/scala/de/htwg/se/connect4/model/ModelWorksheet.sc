import java.util.Optional


case class Cell (isSet:Boolean, color:Optional[String] = Optional.empty()) {
  def setColor():Unit = {}
}

val cell = Cell(false)
cell.isSet

case class Matrix[Cell](rows:Vector[Vector[Cell]]) {

  def this(sizeOfRows:Int, sizeOfCol:Int, cell: Cell) =
    this(Vector.tabulate(sizeOfRows, sizeOfCol){(row, col) => cell})

  def sizeOfRows:Int = rows.size

  def sizeOfCols: Int = rows(0).length

  def cell(row:Int, col:Int):Cell = rows(row)(col)

  def replaceCell(row:Int, col:Int, cell:Cell): Matrix[Cell] =
    copy(rows.updated(row, rows(row).updated(col, cell)))

}

case class Board(cells: Matrix[Cell]) {
  def this(sizeOfRows: Int, sizeOfCol: Int, isSet: Boolean) =
    this(new Matrix[Cell](sizeOfRows, sizeOfCol, Cell(isSet)))

  def size: Int = cells.sizeOfRows

  def cell(row: Int, col: Int): Cell = cells.rows(row)(col)

  def getBoardAsString(matrix: Matrix[Cell]): String = {
    val rows = matrix.sizeOfRows
    val cols = matrix.sizeOfCols

    var returnString = "\n"
    val oneLine = " __ " * cols


    for {
      row <- 0 until rows
      col <- 0 until cols

    } {

      if (col == 0) returnString += "\n" + oneLine + "\n"
      if (matrix.cell(row, col).isSet) returnString += " y |"
      else returnString +=  " - |"

    }

    returnString
  }
}

  val field = new Board(2, 3, false)
  field.size
  field.cells.rows(0).length
  val newField =
    Board (field.cells.replaceCell(1, 1, Cell(true, Optional.of("red"))))
  newField.cell(1,1)
  newField.getBoardAsString(newField.cells)

  field.cells.cell(1, 1)


  val m = new Matrix(Vector(Vector(Cell(true))))

  m.rows(0)(0).isSet

  val m2 = new Matrix(2, 2, Cell(false))


  case class Player(playerName: String, color: String, var piecesLeft: Int = 21) {

    def setPiece(): Player = copy(piecesLeft = piecesLeft - 1)
  }

  val player = Player("lisa", "rot")
  //player.piecesLeft
  val updatedPlayer = player.setPiece()
  //updatedPlayer.piecesLeft
  //updatedPlayer.playerName




