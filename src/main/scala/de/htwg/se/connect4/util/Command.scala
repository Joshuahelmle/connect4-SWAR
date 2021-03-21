package de.htwg.se.connect4.util

trait Command {

  def doStep:Unit
  def undoStep:Unit
  def redoStep:Unit

}
