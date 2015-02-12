lazy val apiVersion = SettingKey[String]("api-version", "The base version of the api.")

name := """met-api"""

apiVersion := "0.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.4"

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
 "com.wordnik" %% "swagger-play2" % "1.3.12",
 "com.wordnik" %% "swagger-play2-utils" % "1.3.12",
  ws
)
