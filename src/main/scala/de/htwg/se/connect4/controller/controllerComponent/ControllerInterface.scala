package de.htwg.se.connect4.controller.controllerComponent

import de.htwg.se.connect4.controller.controllerComponent.controllerBaseImpl.ControllerState
import de.htwg.se.connect4.model.boardComponent.BoardInterface
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.Cell
import de.htwg.se.connect4.model.playerComponent.Player
import de.htwg.se.connect4.util.Observable

trait ControllerInterface extends Observable {

  def stateString: String

  def getWelcomeString: String

  def handle(input: String, board: BoardInterface): String

  def save: String

  def load: String

  def setCol(col : Int) : String

  def set(row: Int, col: Int): String

  def triggerNextStateAndEvaluateInput: Unit

  def playerWin(row: Int, col: Int): Boolean

  def playersHaveNoPiecesLeft: Boolean

  def getNextPlayerIndex: Int

  def boardToString: String

  def addPlayer(input: String): String

  def createNewBoard(rows: Int, cols: Int): String

  def getPlayerDemandString: String

  def getIncorrectInputMessage: String

  def undo(): String

  def redo: String

  def sizeOfRows: Int

  def sizeOfCols: Int

  def isSet(row: Int, col: Int): Boolean

  def cell(row: Int, col: Int): Cell

  def getCell(row: Int, col: Int): Cell

  def getBoard: BoardInterface

  def getState: ControllerState

  def getPlayers: List[Player]

  def getCurrentPlayerIndex: Int

}
