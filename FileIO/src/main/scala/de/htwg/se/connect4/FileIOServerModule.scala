package de.htwg.se.connect4

import de.htwg.se.connect4.model.fileIoComponent.FileIoInterface
import com.google.inject.AbstractModule
import de.htwg.se.connect4.model.fileIoComponentPersistence.FileIoPersistenceInterface
import net.codingwell.scalaguice.ScalaModule

class FileIOServerModule extends AbstractModule with ScalaModule {

  override def configure(): Unit = {
    bind[FileIoInterface].to[model.fileIoComponent.fileIoJsonImpl.FileIO]
    bind[FileIoPersistenceInterface].to[model.fileIoComponentPersistence.SlickImplementation.FileIoPersistence]
  }
}
