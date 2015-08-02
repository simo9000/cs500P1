name := """new-project"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc4"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// ther, legacy style, accesses its actions statically.
// routesGenerator := InjectedRoutesGenerator
