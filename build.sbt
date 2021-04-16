/*import sbtassembly.AssemblyKeys
import sbtdocker.DockerKeys._

lazy val dockerSettings = Seq(
  docker <<= (docker dependsOn (AssemblyKeys.assembly in global)),
  dockerfile in docker := {
    val artifact : File = (AssemblyKeys.assemblyOutputPath in AssemblyKeys.assembly in global).value
    val artifactPath = s"/app/${artifact.name}"
    new Dockerfile{
      from("hseeberger/scala-sbt:8u212_1.2.8_2.13.0")
      add(artifact, artifactPath)
    }
  }
)
*/
enablePlugins(JavaAppPackaging)
lazy val global = project.in(file(".")).settings(libraryDependencies ++= commonDependencies
).settings(dockerBaseImage := "hseeberger/scala-sbt:8u222_1.3.5_2.13.1")
  .settings(daemonUser in Docker := "sbtuser")
  .settings(dockerExposedPorts := Seq(9000))
  .settings(mainClass in Compile := Some("de.htwg.se.connect4.connect4"))
  .aggregate(board, fileio)
  .dependsOn(board,fileio)
  .enablePlugins(sbtdocker.DockerPlugin, JavaAppPackaging)

lazy val board = project.in(file("Board")).settings(libraryDependencies ++= commonDependencies
)
  .enablePlugins(sbtdocker.DockerPlugin, JavaAppPackaging)
  .settings(dockerBaseImage := "hseeberger/scala-sbt:8u222_1.3.5_2.13.1")
  .settings(daemonUser in Docker := "sbtuser")
  .settings(mainClass in Compile := Some("de.htwg.se.connect4.BoardManager"))
  .settings(dockerExposedPorts := Seq(9003))

lazy val fileio = project.in(file("FileIO")).settings(libraryDependencies ++= commonDependencies
).dependsOn(board)
  .enablePlugins(sbtdocker.DockerPlugin, JavaAppPackaging)
  .settings(dockerBaseImage := "hseeberger/scala-sbt:8u222_1.3.5_2.13.1")
  .settings(daemonUser in Docker:= "sbtuser")
  .settings(mainClass in Compile := Some("de.htwg.se.connect4.FileIOServer"))
  .settings(dockerExposedPorts := Seq(9002))





lazy val dependencies =
  new {
    val AkkaVersion = "2.6.8"
    val AkkaHttpVersion = "10.2.4"

    val akka = "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
    val akkaactor = "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion
    val akkastream = "com.typesafe.akka" %% "akka-stream" % AkkaVersion
    val scalatest = "org.scalatest" %% "scalatest" % "3.3.0-SNAP2" % "test"
    val scalactic = "org.scalactic" %% "scalactic" % "3.3.0-SNAP2"
    val scalaswing = "org.scala-lang.modules" %% "scala-swing" % "2.1.1"
    val guice = "com.google.inject" % "guice" % "4.1.0"
    val scalaguice = "net.codingwell" %% "scala-guice" % "4.2.11"
    val playjson = "com.typesafe.play" %% "play-json" % "2.9.1"
    val scalaxml = "org.scala-lang.modules" %% "scala-xml" % "2.0.0-M2"
  }

val commonDependencies =  Seq(
  dependencies.akka,
  dependencies.scalatest,
  dependencies.scalactic,
  dependencies.scalaswing,
  dependencies.guice,
  dependencies.scalaguice,
  dependencies.playjson,
  dependencies.scalaxml,
  dependencies.akkaactor,
  dependencies.akkastream
)

name := "Vier-Gewinnt-SWAR"
organization in ThisBuild := "de.htwg.konstanz"
version := "0.1"

scalaVersion in ThisBuild := "2.13.3"
/*
libraryDependencies += "org.scalactic" %% "scalactic" % "3.3.0-SNAP2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.3.0-SNAP2" % "test"

libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.1.1"

libraryDependencies += "com.google.inject" % "guice" % "4.1.0"
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.2.11"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.1"

libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.0.0-M2"
*/

