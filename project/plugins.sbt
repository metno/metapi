// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.1")

// Eclipse support
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "4.0.0")

//Test plugins
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.1.0")

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"
