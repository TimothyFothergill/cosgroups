name := """cosgroups"""
organization := "com.timlah"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "3.3.7"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test
libraryDependencies += "org.mindrot" % "jbcrypt" % "0.4"
libraryDependencies ++= Seq(
  "org.postgresql"    %  "postgresql"               % "42.7.8",
  "org.playframework" %% "play-slick"               % "6.2.0",
  "org.playframework" %% "play-slick-evolutions"    % "6.2.0"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.timlah.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.timlah.binders._"
