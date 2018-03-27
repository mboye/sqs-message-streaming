import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "sqs-reader",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.5",
    libraryDependencies += "com.lightbend.akka" %% "akka-stream-alpakka-sqs" % "0.17",
    libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.0.002"
  )
