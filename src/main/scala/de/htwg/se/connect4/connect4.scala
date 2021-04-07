package de.htwg.se.connect4

import com.google.inject.Guice
import de.htwg.se.connect4.aview.Tui
import de.htwg.se.connect4.aview.gui.SwingGui
import de.htwg.se.connect4.controller.controllerComponent.ControllerInterface
import de.htwg.se.connect4.model.boardComponent.BoardInterface

import scala.io.StdIn.readLine

object connect4 {

  val injector = Guice.createInjector(new Connect4Module)
  var board = injector.getInstance(classOf[BoardInterface])

  val controller = injector.getInstance(classOf[ControllerInterface])
  val tui = new Tui(controller)
  val gui = new SwingGui(controller)

  controller.notifyObservers


  def main(args: Array[String]): Unit = {

    var input: String = ""
    do {

      input = readLine()
      println(tui.processInputLine(input, board))
    } while (input != "q")

  }
}
