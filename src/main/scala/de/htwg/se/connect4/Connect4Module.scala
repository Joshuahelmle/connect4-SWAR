package de.htwg.se.connect4

import com.google.inject.AbstractModule
import de.htwg.se.connect4.controller.controllerComponent.ControllerInterface
import de.htwg.se.connect4.model.boardComponent.BoardInterface
import de.htwg.se.connect4.model.boardComponent.boardBaseImpl.BoardSizeStrategy
import de.htwg.se.connect4.model.fileIoComponent.FileIoInterface
import de.htwg.se.connect4.model.playerComponent.Player
import net.codingwell.scalaguice.ScalaModule

class Connect4Module extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[BoardInterface].toInstance(BoardSizeStrategy.execute(6, 7))
    bind[List[Player]].toInstance(Nil)
    bind[ControllerInterface].to[controller.controllerComponent.controllerBaseImpl.Controller]

    bind[FileIoInterface].to[model.fileIoComponent.fileIoJsonImpl.FileIO]
  }
}
