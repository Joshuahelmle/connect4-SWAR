package de.htwg.se.connect4.model.DBIoComponentPersistence.MongoDBImplementation


import de.htwg.se.connect4.model.playerComponent.Player
import org.bson.types.ObjectId

object GameComponent {
  def apply(players : Seq[PlayerComponent], board : Seq[BoardComponent], sizeOfRows : Int, sizeOfCols : Int): GameComponent = {
   new  GameComponent(new ObjectId(), players, board, sizeOfRows, sizeOfCols)
  }

  def apply(_id: ObjectId, players : Seq[PlayerComponent], board : Seq[BoardComponent] , sizeOfRows : Int, sizeOfCols : Int) : GameComponent = {
    new GameComponent(_id, players, board, sizeOfRows, sizeOfCols)
  }
}


case class GameComponent(_id: ObjectId, players : Seq[PlayerComponent], board : Seq[BoardComponent] , sizeOfRows : Int, sizeOfCols : Int) {

}