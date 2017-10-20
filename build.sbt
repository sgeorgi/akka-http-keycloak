
version := "0.1"

scalaVersion := "2.12.4"

lazy val commonSettings = Seq(
  organization := "de.sgeorgi",
  version := "0.1",
  scalaVersion := "2.12.4",
  scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature", "-language:postfixOps"),
  resolvers += "spray repo" at "http://repo.spray.io",
  resolvers += "TypeSafe repo" at "http://repo.typesafe.com/typesafe/releases/",
  resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  autoScalaLibrary := false
)

lazy val depencencies = Seq(
  "org.scala-lang" % "scala-library" % "2.12.4",
  "org.scala-lang" % "scala-compiler" % "2.12.4",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.10",
  "com.typesafe.akka" %% "akka-http" % "10.0.10",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10" % "test",
  "org.scalatest" % "scalatest_2.12" % "3.0.4" % "test",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)
lazy val api = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "akka-http-keycloak",
    libraryDependencies ++= depencencies ++ keycloak
  )
val keycloak = Seq(
  "org.keycloak" % "keycloak-adapter-core" % "3.3.0.CR2",
  "org.keycloak" % "keycloak-core" % "3.3.0.CR2",
  "org.jboss.logging" % "jboss-logging" % "3.3.1.Final"
)
