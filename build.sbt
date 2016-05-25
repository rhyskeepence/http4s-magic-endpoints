lazy val versions = new {
  val scala = "2.11.8"
  val http4s = "0.13.2"
}

lazy val endpoint = project
  .settings(Seq(
    version := "1.0.0",
    organization := "com.rhyskeepence",
    moduleName := "http4s-magic-endpoints",
    scalaVersion := versions.scala,
    crossScalaVersions := Seq("2.11.8", "2.10.6"),
    scalacOptions ++= Seq("-feature", "-language:implicitConversions")
  ))
  .settings(Seq(
    libraryDependencies ++= Seq(
      compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "org.http4s" %% "http4s-dsl" % versions.http4s,

      "org.scalatest" %% "scalatest" % "2.2.4" % "test"
    )
  ))
