
enablePlugins(JavaAppPackaging, DockerComposePlugin)
dockerImageCreationTask := docker.value
val dockerAppPath = "/app/"
lazy val global = project.in(file(".")).settings(libraryDependencies ++= commonDependencies
).settings(dockerBaseImage := "hseeberger/scala-sbt:8u222_1.3.5_2.13.1")
  .settings(daemonUser in Docker := "sbtuser")
  .settings(dockerExposedPorts := Seq(9000))
  .settings(mainClass in Compile := Some("de.htwg.se.connect4.connect4"))
  .aggregate(board, fileio)
  .dependsOn(board,fileio)
  .enablePlugins(sbtdocker.DockerPlugin, JavaAppPackaging, DockerComposePlugin)
  .settings(imageNames in docker := Seq(ImageName(repository = name.value.toLowerCase, tag = Some("latest"))))
  .settings(
    dockerfile in docker := {
      new Dockerfile {
        val mainClassString = (mainClass in Compile).value.get
        val classpath = (fullClasspath in Compile).value
        from("java")
        add(classpath.files, dockerAppPath)
        entryPoint("java", "-cp", s"$dockerAppPath:$dockerAppPath/*", s"$mainClassString")
      }
    }
  )

lazy val board = project.in(file("Board")).settings(libraryDependencies ++= commonDependencies
)
  .enablePlugins(sbtdocker.DockerPlugin, JavaAppPackaging)
  .settings(dockerBaseImage := "hseeberger/scala-sbt:8u222_1.3.5_2.13.1")
  .settings(daemonUser in Docker := "sbtuser")
  .settings(mainClass in Compile := Some("de.htwg.se.connect4.BoardManager"))
  .settings(dockerExposedPorts := Seq(9003))
  .settings(imageNames in docker := Seq(ImageName(repository = name.value.toLowerCase, tag = Some("latest"))))
  .settings(
    dockerfile in docker := {
      new Dockerfile {
        val mainClassString = (mainClass in Compile).value.get
        val classpath = (fullClasspath in Compile).value
        from("java")
        add(classpath.files, dockerAppPath)
        entryPoint("java", "-cp", s"$dockerAppPath:$dockerAppPath/*", s"$mainClassString")
      }
    }
  )

lazy val fileio = project.in(file("FileIO")).settings(libraryDependencies ++= commonDependencies
).dependsOn(board)
  .enablePlugins(sbtdocker.DockerPlugin, JavaAppPackaging, DockerComposePlugin)
  .settings(dockerBaseImage := "hseeberger/scala-sbt:8u222_1.3.5_2.13.1")
  .settings(daemonUser in Docker:= "sbtuser")
  .settings(mainClass in Compile := Some("de.htwg.se.connect4.FileIOServer"))
  .settings(dockerExposedPorts := Seq(9002))
  .settings(imageNames in docker := Seq(ImageName(repository = name.value.toLowerCase, tag = Some("latest"))))
  .settings(
    dockerfile in docker := {
      new Dockerfile {
        val mainClassString = (mainClass in Compile).value.get
        val classpath = (fullClasspath in Compile).value
        from("java")
        add(classpath.files, dockerAppPath)
        entryPoint("java", "-cp", s"$dockerAppPath:$dockerAppPath/*", s"$mainClassString")
      }
    }
  )
  .settings(libraryDependencies ++= Seq(dependencies.slick, dependencies.slf4jNop))



enablePlugins(GatlingPlugin)

scalacOptions := Seq(
  "-encoding", "UTF-8", "-target:jvm-1.8",
  "-deprecation", "-feature", "-unchecked",
  "-language:implicitConversions", "-language:postfixOps")
val gatlingVersion = "3.5.1"
libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion % "test,it"
libraryDependencies += "io.gatling"            % "gatling-test-framework"    % gatlingVersion % "test,it"


lazy val dependencies =
  new {
    val AkkaVersion = "2.6.11"
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
    val slick = "com.typesafe.slick" %% "slick" % "3.3.3"
    val slf4jNop = "org.slf4j" % "slf4j-nop" % "1.6.4"
    val postgres = "org.postgresql" % "postgresql" % "42.2.13"
    val mongodb = "org.mongodb.scala" %% "mongo-scala-driver" % "2.9.0"

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
  dependencies.akkastream,
  dependencies.postgres,
  dependencies.mongodb
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

