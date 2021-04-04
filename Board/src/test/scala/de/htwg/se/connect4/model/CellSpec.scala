package de.htwg.se.connect4.model

import de.htwg.se.connect4.model.boardComponent.boardBaseImpl
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.{Cell, Color}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class CellSpec extends AnyWordSpec with Matchers {

  "A Cell represents the place for a gaming piece" when {

    "is created" should {
      val cell = Cell(false)

      "be initialized with false and no color" in {
        cell.isSet should be(false)
        cell.color should be (Color.EMPTY)
      }

    }

    "a piece is set" should {
      val redCell = Cell(true, Color.RED)

      "be red if red player set a piece" in {
        redCell.isSet should be(true)
        redCell.color should be(Color.RED)
      }

      val yellowCell = Cell(true, Color.YELLOW)

      "be initialized if yellow player set a piece" in {
        yellowCell.isSet should be(true)
        yellowCell.color should be(Color.YELLOW)
      }

    }

  }

}
