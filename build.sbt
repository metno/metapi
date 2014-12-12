name := """bora-prototype"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.4"

sourceDirectory in Test := new File(baseDirectory.value, "tests")

scalaSource in Test := new File(baseDirectory.value, "tests")

javaSource in Test := new File(baseDirectory.value, "tests")

javaOptions += "-Djunit.outdir=target/test-report"

instrumentSettings

parallelExecution in ScoverageTest := false

ScoverageKeys.highlighting := true

ScoverageKeys.minimumCoverage := 100

ScoverageKeys.failOnMinimumCoverage := true

ScoverageKeys.excludedPackages in ScoverageCompile := "<empty>;ReverseApplication;ReverseAssets;Routes"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws
)
