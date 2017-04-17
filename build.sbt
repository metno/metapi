organization := "no.met.data"
name := """metapi"""
lazy val apiVersion = SettingKey[String]("api-version", "The base version of the api.")
apiVersion := "0.3"
version <<= (apiVersion, git.gitHeadCommit) { (ver, commit) =>
  val commitVer = commit map( v => "+" + v ) getOrElse ""
  sys.props.get("buildnumber" ) match {
    case None => ver + "-SNAPSHOT"
     case Some(build) => ver + "-" + build + commitVer
  }
}
description := "The metapi master application."
homepage :=  Some(url(s"https://github.com/metno"))
licenses += "GPL-2.0" -> url("https://www.gnu.org/licenses/gpl-2.0.html")

// Scala settings
// ----------------------------------------------------------------------
scalaVersion := "2.11.8"
scalacOptions ++= Seq("-deprecation", "-feature")
lazy val root = (project in file(".")).enablePlugins(PlayScala)

// Play settings
// ----------------------------------------------------------------------

// Dependencies
// ----------------------------------------------------------------------
libraryDependencies ++= Seq(
  jdbc,
  cache,
  evolutions,
  ws,
 "com.typesafe.play" %% "anorm" % "2.4.0",
 "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
 "org.seleniumhq.selenium" % "selenium-htmlunit-driver" % "2.52.0",
 "io.swagger" %% "swagger-play2" % "1.5.2",
 "com.github.nscala-time" %% "nscala-time" % "2.14.0",
 "no.met.data" %% "util" % "0.3-SNAPSHOT",
 "no.met.data" %% "auth" % "0.3-SNAPSHOT",
 "no.met.data" %% "observations" % "0.3-SNAPSHOT",
 "no.met.data" %% "frequencies" % "0.3-SNAPSHOT",
 "no.met.data" %% "climatenormals" % "0.3-SNAPSHOT",
 "no.met.data" %% "sources" % "0.3-SNAPSHOT",
 "no.met.data" %% "locations" % "0.3-SNAPSHOT",
 "no.met.data" %% "elements" % "0.3-SNAPSHOT",
  specs2 % Test
)


resolvers ++= Seq(
  "OJO Artifactory" at "http://oss.jfrog.org/artifactory/oss-snapshot-local",
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "unidata" at "http://artifacts.unidata.ucar.edu/content/repositories/unidata-releases/"
)

// Publish Settings
// ----------------------------------------------------------------------
publishTo := {
  val jfrog = "https://oss.jfrog.org/artifactory/"
  if (isSnapshot.value)
    Some("Artifactory Realm" at jfrog + "oss-snapshot-local;build.timestamp=" + new java.util.Date().getTime)
  else
    Some("Artifactory Realm" at jfrog + "oss-release-local")
}
pomExtra := (
  <scm>
    <url>https://github.com/metno/metapi-{name.value}.git</url>
    <connection>scm:git:git@github.com:metno/metapi-{name.value}.git</connection>
  </scm>
  <developers>
    <developer>
      <id>metno</id>
      <name>Meteorological Institute, Norway</name>
      <url>http://www.github.com/metno</url>
    </developer>
  </developers>)
publishMavenStyle := false
bintrayReleaseOnPublish := false
publishArtifact in Test := false

// Testing
// ----------------------------------------------------------------------
parallelExecution in Test := false
coverageHighlighting := true
coverageMinimum := 95
coverageFailOnMinimum := true
coverageExcludedPackages := """
  <empty>;
  util.HttpStatus;
  views.html.index.*;
  views.html.concepts.*;
  views.html.reference.*;
  views.html.schema.*;
  views.html.examples.*;
  views.html.termsofuse.*;
  views.html.util.navbar.*;
  value.ApiResponse;
  ReverseApplication;
  ReverseAssets;
  router.*;
"""

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
