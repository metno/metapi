resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "jgit-repo" at "http://download.eclipse.org/jgit/maven"

// Git plugin

addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.6.4")

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.8")

//Test plugins
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.0.4")

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"

// web plugins

addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.0.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.0.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-mocha" % "1.0.0")
