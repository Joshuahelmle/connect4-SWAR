package de.htwg.se.connect4.controller.controllerComponent.controllerBaseImpl

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.{Get, Post}
import akka.http.scaladsl.model.{HttpResponse, StatusCode, StatusCodes}
import akka.http.scaladsl.unmarshalling.Unmarshal
import com.google.inject.{Guice, Inject}
import de.htwg.se.connect4.Connect4Module
import de.htwg.se.connect4.controller.controllerComponent.ControllerInterface
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.{BoardSizeStrategy, Cell, Color}
import de.htwg.se.connect4.model.fileIoComponent.{FileIoInterface, State}
import de.htwg.se.connect4.model.playerComponent
import de.htwg.se.connect4.model.playerComponent.Player
import de.htwg.se.connect4.util.{Observable, UndoManager}
import net.codingwell.scalaguice.InjectorExtensions._
import de.htwg.se.connect4.model.boardComponent.BoardInterface
import play.api.libs.json.JsValue.jsValueToJsLookup
import play.api.libs.json.Json

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}


class Controller @Inject()(var board: BoardInterface, var players: List[Player]) extends Observable with ControllerInterface {

  val injector = Guice.createInjector(new Connect4Module)
  val fileIo = injector.instance[FileIoInterface]

  var state: ControllerState = InitializationState(this)
  var currentPlayerIndex: Int = 0
  private val undoManager = new UndoManager

  def stateString: String = state.stateString()


  def handle(input: String, board: BoardInterface): Try[String] = {
    state.handle(input, board)
  }

  def getWelcomeString: String = "Welcome to connect 4. Please Enter your names."

  def setCol(col : Int) : String = {

    val payload = Json.obj(
      "col" -> col,
      "color" -> players(currentPlayerIndex).color
    )
    implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.executionContext
    val responseFuture: Future[HttpResponse] = Http().singleRequest(Post("http://localhost:9003/board", payload.toString()))
    responseFuture.onComplete{
      case Success(res) => {
        if (res.status == StatusCodes.OK) {
          val responseBody : Future[String] = Unmarshal(res.entity).to[String]
          responseBody.onComplete{
            case Success(body) => {
              val b = Json.parse(body)
              val idx = (b \ "idx").as[Int]
              return set(idx,col)
            }
            case Failure(_) => sys.error("Error in setCol")
          }
        } else {
          notifyObservers
          return "Cell is already set. Please chose different one."
        }
      }
      case Failure(_) => sys.error("Error in setCol")
    }
    /*
    var idx = board.sizeOfRows-1
    var found = false;
    while(!found && idx >= 0){
      found = !board.cell(idx,col).isSet
      idx -= 1
    }
    if(found) {
     return
    }
    else {
      notifyObservers
      "Cell is already set. Please chose different one."
    }
    */
    ""
  }


  def set(row: Int, col: Int): String = {
    if (board.cell(row, col).isSet) {
      notifyObservers
      "Cell is already set. Please chose different one."

    } else {

      undoManager.doStep(new SetCommand(row, col, players(currentPlayerIndex), this, true))
      players = players.updated(currentPlayerIndex, players(currentPlayerIndex).setPiece())

      if (playerWin(row, col)) {
        triggerNextStateAndEvaluateInput
        return ""
      }

      if (playersHaveNoPiecesLeft) {
        triggerNextStateAndEvaluateInput
        return ""
      }

      currentPlayerIndex = getNextPlayerIndex
      notifyObservers
      ""
    }
  }

  def triggerNextStateAndEvaluateInput: Unit = {
    state = state.nextState()
    notifyObservers

  }

  def playerWin(row: Int, col: Int): Boolean = {
    val matchInCols = board.hasWon((x,y) => (x+1,y), players(currentPlayerIndex).color, 0, (-1,col))
    val matchInRows =  board.hasWon((x,y) => (x,y+1), players(currentPlayerIndex).color, 0, (row,-1))
    //val matchInCols = board.checkCols(col, players(currentPlayerIndex).color)
    //val matchInRows = board.checkRow(row, players(currentPlayerIndex).color)
    val matchDiagonal = board.checkDiagonal(row, col, players(currentPlayerIndex).color)

    matchInCols || matchInRows || matchDiagonal
  }

  def playersHaveNoPiecesLeft: Boolean = if (players.head.piecesLeft == 0 && players(1).piecesLeft == 0) true else false


  def getNextPlayerIndex: Int = if (currentPlayerIndex == 0) 1 else 0


  def boardToString: String = board.getBoardAsString(board.getCells)


  def addPlayer(input: String): String = {
    if (players.isEmpty) {
      players = players ::: List(playerComponent.Player(input, Color.RED))

    }
    else if (players.size < 2) {
      players = players ::: List(playerComponent.Player(input, Color.YELLOW))
      triggerNextStateAndEvaluateInput
    }
    else triggerNextStateAndEvaluateInput
    ""
  }

  def createNewBoard(rows: Int, cols: Int): String = {
    board = BoardSizeStrategy.execute((rows, cols))
    currentPlayerIndex = 0
    var newPlayers: List[Player] = List()
    newPlayers = newPlayers ::: List(players.head.copy(piecesLeft = 21) )
    newPlayers = newPlayers ::: List(players(1).copy(piecesLeft = 21))
    players = newPlayers
    state = InGameState(this)
    notifyObservers
    "created new Board \n" + getPlayerDemandString
  }

  def getPlayerDemandString: String = "It's your turn Player " + players(currentPlayerIndex).playerName


  def getIncorrectInputMessage: String = "Please Enter two numbers separated by a whitespace."

  def undo(): String = {
    undoManager.undoStep()
    notifyObservers
    "Undo Step."
  }

  def redo: String = {
    undoManager.redoStep()
    notifyObservers
    "Redo Step."
  }

  def sizeOfRows: Int = board.sizeOfRows

  def sizeOfCols: Int = board.sizeOfCols

  def isSet(row: Int, col: Int): Boolean = board.cell(row, col).isSet

  def cell(row: Int, col: Int): Cell = board.cell(row, col)

  def getCell(row: Int, col: Int): Cell = board.cell(row, col)

  def getBoard: BoardInterface = board

  def getState: ControllerState = state

  def getPlayers: List[Player] = players

  def getCurrentPlayerIndex: Int = currentPlayerIndex

  override def save: String = {
    val stateToSave = State(currentPlayerIndex, players, state.toString())
    /*implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.executionContext
    val payload = board.getBoardAsString()
    val responseFuture: Future[HttpResponse] = Http().singleRequest(Post("http://localhost:9002/board", payload.toString()))
*/
    fileIo.save(board, stateToSave)
    "saved"
  }

  override def load: String = {

    val (newBoard, loadedState) = fileIo.load
    board = newBoard
    players = loadedState.players
    currentPlayerIndex = loadedState.currentPlayerIndex

    setStateOfString(loadedState.state)

    notifyObservers
    "loaded"
  }

  def setStateOfString(stateString: String): Unit = {
    stateString match {
      case "GameOverState" => state = GameOverState(this)
      case "InitializationState" => state = InitializationState(this)
      case "PlayerWinState" => PlayerWinState
      case "InGameState" => state = InGameState(this)
    }
  }
}




