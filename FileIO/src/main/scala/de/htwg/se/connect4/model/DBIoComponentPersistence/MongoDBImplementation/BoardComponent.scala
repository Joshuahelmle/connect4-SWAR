package de.htwg.se.connect4.model.DBIoComponentPersistence.MongoDBImplementation




object BoardComponent {
  def apply(xValue : Int, yValue : Int, isSet : Boolean, color : String): BoardComponent = {
    new BoardComponent(xValue,yValue,isSet, color)
  }
}


case class BoardComponent(xValue : Int, yValue : Int, isSet : Boolean, color: String) {

}