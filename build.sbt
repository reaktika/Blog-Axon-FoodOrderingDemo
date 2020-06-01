lazy val axonVersion = "4.1.2"

lazy val root = (project in file(".")).
  settings(
    name := "foodorderingdemo-scala-axon",
    version := "0.0.1",
    scalaVersion := "2.13.1",
    libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime,
    libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.11",
    libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.26",
    libraryDependencies += "org.axonframework" % "axon-configuration" % axonVersion,
    libraryDependencies += "org.axonframework" % "axon-server-connector" % axonVersion,
    libraryDependencies += "org.axonframework" % "axon-modelling" % axonVersion,
    libraryDependencies += "org.axonframework" % "axon-messaging" % axonVersion,
    libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.0" % "test",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % "test"
  )
