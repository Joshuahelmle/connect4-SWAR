package de.htwg.se.connect4.model.fileIoComponent

import de.htwg.se.connect4.controller.controllerComponent.controllerBaseImpl.State
import de.htwg.se.connect4.model.boardComponent.BoardInterface

trait FileIoInterface {

  def load: (BoardInterface, State)

  def save(board: BoardInterface, state: State): Unit

}
