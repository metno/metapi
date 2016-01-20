lazy val apiVersion = SettingKey[String]("api-version", "The base version of the api.")

name := """metapi"""

// This code is used by the deployment pipeline to generate the
// version number. Only the apiVersion should be changed.
apiVersion := "0.2"
version <<= (apiVersion, git.gitHeadCommit) { (ver, commit) =>
  val commitVer = commit map( v => "+" + v ) getOrElse ""
  sys.props.get("buildnumber" ) match {
    case None => ver + "-SNAPSHOT"
     case Some(build) => ver + "-" + build + commitVer
  }
}

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
 "no.met.data" %% "elements" % "0.2-SNAPSHOT",
 "no.met.data" %% "observations" % "0.2-SNAPSHOT",
 "no.met.data" %% "sources" % "0.2-SNAPSHOT",
  specs2 % Test
)

resolvers ++= Seq( "metno repo" at "http://maven.met.no/content/groups/public",
                   "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
                   "amateras-repo" at "http://amateras.sourceforge.jp/mvn/"
                    )

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
