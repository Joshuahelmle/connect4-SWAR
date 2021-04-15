package de.htwg.se.connect4.model.fileIoComponent

import de.htwg.se.connect4.model.fileIoComponent.State
import de.htwg.se.connect4.model.boardComponent.BoardInterface
import play.api.libs.json.JsValue

trait FileIoInterface {

  def load: (BoardInterface, State)

  def save(board: BoardInterface, state: State): Unit

}
