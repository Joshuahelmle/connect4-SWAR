package de.htwg.se.connect4.model.DBIoComponentPersistence

import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.connect4.model.playerComponent.Player
import org.mongodb.scala.bson.ObjectId

trait DBIoPersistenceInterface {

  def create(players: List[Player], rows : Int, cols: Int) : String

  def update(id: String, players : List[Player], board : Board) : Boolean

  def delete(id: String) : Boolean

  def read(id : String) : Option[(List[Player], Board)]

  def test : String

  def getAllGames : String

}
