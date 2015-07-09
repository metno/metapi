name := """metapi"""

version := "0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"


// Test Settings
parallelExecution in Test := false

javaOptions += "-Djunit.outdir=target/test-report"

ScoverageSbtPlugin.ScoverageKeys.coverageHighlighting := true

ScoverageSbtPlugin.ScoverageKeys.coverageMinimum := 95

ScoverageSbtPlugin.ScoverageKeys.coverageFailOnMinimum := true

ScoverageSbtPlugin.ScoverageKeys.coverageExcludedPackages := """
  <empty>;
  util.HttpStatus;
  views.html.index.*;
  views.html.docs.*;
  value.ApiResponse;
  ReverseApplication;
  ReverseAssets;
  router.*;
"""


// Dependencies
libraryDependencies ++= Seq(
  jdbc,
  cache,
  evolutions,
  ws,
 "com.typesafe.play" %% "anorm" % "2.4.0",
 "pl.matisoft" %% "swagger-play24" % "1.4",
 "com.github.nscala-time" %% "nscala-time" % "2.0.0",
 "com.oracle" % "ojdbc14" % "10.2.0.1.0",
 "no.met.data" %% "util" % "0.2-SNAPSHOT",
 "no.met.data" %% "auth" % "0.2-SNAPSHOT",
 //"no.met.data" %% "elements" % "0.2-SNAPSHOT",
 "no.met.data" %% "observations" % "0.2-SNAPSHOT",
  specs2 % Test
)

resolvers ++= Seq( "metno repo" at "http://maven.met.no/content/groups/public",
                   "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases" )

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
