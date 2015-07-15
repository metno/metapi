// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.2")

// Git plugin (used for version resolution)
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.4")

// Eclipse support
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "4.0.0")

//Test plugins
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.1.0")

resolvers ++= Seq( "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/",
                   "jgit-repo" at "http://download.eclipse.org/jgit/maven" )
