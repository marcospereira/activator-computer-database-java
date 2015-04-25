name := "computer-database-java"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaCore, javaJdbc, filters, cache, javaWs,
  "org.webjars"     % "jquery"      % "2.1.1",
  "org.webjars"     % "bootstrap"   % "3.3.1",
  "org.easytesting" % "fest-assert" % "1.4" % Test,
  "junit"           % "junit"       % "4.12" % Test
)

lazy val root = (project in file(".")).enablePlugins(PlayJava, SbtEbean)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v", "-q")

routesGenerator := InjectedRoutesGenerator
