package de.htwg.se.connect4.model.fileIoComponent.fileIoXmlImpl

import de.htwg.se.connect4.controller.controllerComponent.controllerBaseImpl.State
import de.htwg.se.connect4.model.boardComponent.BoardInterface
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.{BoardSizeStrategy, Color}
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.Color.Color
import de.htwg.se.connect4.model.fileIoComponent.FileIoInterface
import de.htwg.se.connect4.model.playerComponent.Player

import scala.xml.PrettyPrinter

class FileIO extends FileIoInterface {


  override def load: (BoardInterface, State) = {
    var board: BoardInterface = null
    val file = scala.xml.XML.loadFile("board.xml")
    val rowAttr = file \\ "game"  \\ "board" \ "@row"
    val colAttr = file \\ "game" \\ "board" \  "@col"

    val sizeOfRows = rowAttr.text.toInt
    val sizeOfCols = colAttr.text.toInt

    board = BoardSizeStrategy.execute(sizeOfRows, sizeOfCols)

    val cellNodes = file \\ "cell"
    for (cell <- cellNodes) {
      val row: Int = (cell \ "@row").text.toInt
      val col: Int = (cell \ "@col").text.toInt
      val colorAttr: String = (cell \ "@color").text
      val color: Color = Color.toEnum(colorAttr)
      val isSet: Boolean = (cell \ "@isSet").text.toBoolean
      board = board.set(row, col, color, isSet)
    }

    val currentPlayerIndex: Int = (file \\ "game" \\ "@currentPlayerIndex").text.toInt
    val stateString: String = (file \\ "game" \\ "@stateString").text
    var players: List[Player] = Nil

    val playerNodes = (file \\ "player")
    for (player <- playerNodes) {
      val name: String = (player \ "@name").text
      val colorAttr: String = (player \ "@color").text
      val color: Color = Color.toEnum(colorAttr)
      val piecesLeft: Int = (player \ "@piecesLeft").text.toInt

      val playerObject = new Player(name, color, piecesLeft)
      players = players ::: playerObject :: Nil

    }

    val state = new State(currentPlayerIndex, players, stateString)

    (board, state)

  }

  override def save(board: BoardInterface, state: State): Unit = saveString(board, state)

  def saveString(board: BoardInterface, state: State): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("board.xml"))
    val prettyPrinter = new PrettyPrinter(200, 2)
    val xml = prettyPrinter.format(boardToXml(board, state))
    pw.write(xml)
    pw.close

  }

  def boardToXml(board: BoardInterface, state: State) = {
    <game>
      <currentPlayerIndex currentPlayerIndex =
        {state.currentPlayerIndex.toString}>
      </currentPlayerIndex>
      <stateString stateString =
        {state.state.toString}>
      </stateString>

      <players>
        {for {
        index <- state.players.indices

      } yield playerToXml(state, index)}
      </players>

      <board row={ board.sizeOfRows.toString } col = {board.sizeOfCols.toString} >
        {
        for {
          row <- 0 until board.sizeOfRows
          col <- 0 until board.sizeOfCols
        } yield cellToXml(board, row, col)
        }
      </board>
    </game>


  }

  def cellToXml(board: BoardInterface, row: Int, col: Int) = {
    <cell row={row.toString} col={col.toString} isSet={board.cell(row, col).isSet.toString}
      color={board.cell(row, col).color.toString}>
    </cell>
  }

  def playerToXml(state: State, i: Int) = {
    <player name={state.players(i).playerName.toString} piecesLeft={state.players(i).piecesLeft.toString} color={state.players(i).color.toString}>
    </player>
  }
}
