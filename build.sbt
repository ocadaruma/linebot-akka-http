lazy val commonSettings = Seq(
  scalaVersion := "2.11.8",
  scalacOptions += "-deprecation",
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http-core" % "2.4.3",
    "com.typesafe.akka" %% "akka-http-experimental" % "2.4.3",
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.3"
  )
)

lazy val root = project.in(file("."))
  .settings(commonSettings: _*)

