package de.htwg.se.connect4.model.DBIoComponentPersistence.MongoDBImplementation

import org.mongodb.scala.bson.ObjectId

object PlayerComponent {
  def apply(name : String, color : String, piecesLeft : Int): PlayerComponent =
    new PlayerComponent(new ObjectId(), name, color, piecesLeft)

  def apply(player : de.htwg.se.connect4.model.playerComponent.Player) : PlayerComponent = {
    new PlayerComponent(new ObjectId(), player.playerName, player.color.toString, player.piecesLeft)
  }
}

case class PlayerComponent(_id : ObjectId, name : String, color : String, piecesLeft : Int)
