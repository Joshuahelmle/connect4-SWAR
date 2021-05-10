package de.htwg.se.connect4.model.DBIoComponentPersistence

import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.connect4.model.playerComponent.Player

trait DBIoPersistenceInterface {

  def create(players: List[Player], rows : Int, cols: Int) : Int

  def update(id: Int, players : List[Player], board : Board) : Boolean

  def delete(id: Int) : Boolean

  def read(id : Int) : Option[(List[Player], Board)]

}
