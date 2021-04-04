package de.htwg.se.connect4.model.boardComponent.boardBaseImpl

object BoardSizeStrategy {

  trait BoardSizeStrategy {
    def strategy(boardSize: (Int, Int))

    def defaultSizeStrategy(boardSize: (Int, Int)): Board

    def tinySizeStrategy(boardSize: (Int, Int)): Board

    def bigSizeStrategy(boardSize: (Int, Int)): Board

    def execute(boardSize: Int):Board
  }

  def execute(boardSize: (Int, Int)): Board = strategy(boardSize)


  def strategy(boardSize: (Int, Int)): Board = {
    boardSize match {
      case (6,7) => defaultSizeStrategy(boardSize)
      case (2,3) => tinySizeStrategy(boardSize)
      case (15,16) => bigSizeStrategy(boardSize)
      case (_,_) => defaultSizeStrategy((6,7))
    }
  }

  def defaultSizeStrategy(boardSize: (Int, Int)): Board = {
    Creator().sizeOfBoard(boardSize._1, boardSize._2)
  }

  def tinySizeStrategy(boardSize: (Int, Int)): Board = {
    Creator().sizeOfBoard(boardSize._1, boardSize._2)
  }

  def bigSizeStrategy(boardSize: (Int, Int)): Board = {
    Creator()sizeOfBoard(boardSize._1, boardSize._2)
  }

}
