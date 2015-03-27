lazy val apiVersion = SettingKey[String]("api-version", "The base version of the api.")

name := """met-api"""

apiVersion := "0.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.5"

version <<= (apiVersion, git.gitHeadCommit) { (ver, commit) =>
  val commitVer = commit map( v => "+" + v ) getOrElse ""
  sys.props.get("buildnumber" ) match {
    case None => ver + "-SNAPSHOT"
      case Some(build) => ver + "-" + build + commitVer
  }
}

resourceGenerators in Compile += Def.task {
  val file = new File( (resourceManaged in Compile).value, "version.properties")
  val prop = "version=%s" format ( version.value )
  IO.write( file, prop )
  Seq( file )
}.taskValue

sourceDirectory in Test := new File(baseDirectory.value, "tests")

scalaSource in Test := new File(baseDirectory.value, "tests")

javaSource in Test := new File(baseDirectory.value, "tests")

javaOptions += "-Djunit.outdir=target/test-report"

parallelExecution in Test := false

ScoverageSbtPlugin.ScoverageKeys.coverageHighlighting := true

ScoverageSbtPlugin.ScoverageKeys.coverageMinimum := 100

ScoverageSbtPlugin.ScoverageKeys.coverageFailOnMinimum := true

ScoverageSbtPlugin.ScoverageKeys.coverageExcludedPackages := """
    <empty>;
    util.HttpStatus;
    views.html.swaggerUi.*;
    value.ApiResponse;
    ReverseApplication;
    ReverseAssets;
    Routes
"""

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
 "com.wordnik" %% "swagger-play2" % "1.3.12",
 "com.wordnik" %% "swagger-play2-utils" % "1.3.12",
  ws,
 "no.met.data" %% "authorization" % "0.1-SNAPSHOT",
 "no.met.data" %% "kdvh" % "0.1-SNAPSHOT"
)

// Plugin configuration - test this:

//lazy val admin = (project in file("modules/admin")).enablePlugins(PlayScala)
//lazy val main = (project in file("."))
//    .enablePlugins(PlayScala).dependsOn(admin).aggregate(admin)

// following does not work as it overrides the main routes file
//PlayKeys.devSettings += ("application.router", "test.Routes")

// from kdvh - delete when fixed

resolvers += "metno repo" at "http://maven.met.no/content/groups/public"

libraryDependencies ++= Seq(
 "com.github.nscala-time" %% "nscala-time" % "1.8.0",
 "com.oracle" % "ojdbc14" % "10.2.0.1.0"
)
