package de.htwg.se.connect4.aview.gui

import de.htwg.se.connect4.controller.controllerComponent.ControllerInterface
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.Color

import scala.swing.event.MouseClicked
import scala.swing.{BoxPanel, Color, Dimension, FlowPanel, Orientation, Swing}

class CellPanel(row: Int, col: Int, controller: ControllerInterface) extends FlowPanel {


  var cellColor = new Color(255, 250, 250)

  def cellColor(row: Int, col: Int): Color = {
    if (controller.getCell(row, col).isSet) {
      if (controller.getCell(row, col).color == Color.YELLOW) cellColor = new Color(255, 255, 0)
      else cellColor = new Color(255, 0, 0)
    } else cellColor = new Color(255, 250, 250)

    cellColor
  }


  val cell = new BoxPanel(Orientation.Vertical) {
    background = cellColor(row, col)
    preferredSize = new Dimension(100, 100)
    border = Swing.BeveledBorder(Swing.Raised)

    listenTo(mouse.clicks)

    reactions += {
      case MouseClicked(src, pt, mod, clicks, pops) =>
        controller.handle(row.toString + col.toString, controller.getBoard)
        background = cellColor(row, col)
        repaint()
    }
  }


  def redraw = {
    contents.clear()

    cell.background = cellColor(row, col)

    contents += cell

    repaint
  }

}
