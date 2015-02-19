#met-api

##Prerequisite

The met-api is an jvm application and at least version 7 is required.
sbt is used to build the application. sbt use the zip-archive format
so unzip must be installed to deploy met-api.

###Install instruction for ubuntu 14.04

Install java and unzip.  `apt-get install  openjdk-7-jdk unzip`

Install sbt.
Download a debian package with wget http://dl.bintray.com/sbt/debian/sbt-0.13.6.deb and
install with `dpkg -i sbt-0.13.6.deb`


##Build met-api

Clone met-api at github

`git clone https://github.com/metno/met-api.git`

Change directory to met-api.

The following command should build met-api.
The command will download all dependencies

`sbt clean stage dist`

This will create a zip archive in the directory target/universal.
Copy the zip file to where to deploy the met-api and unzip the file.

The layout of the archive is:

```
met-api-0.1-SNAPSHOT/lib/
met-api-0.1-SNAPSHOT/bin/
met-api-0.1-SNAPSHOT/conf/application.conf
met-api-0.1-SNAPSHOT/share/
met-api-0.1-SNAPSHOT/README.md
met-api-0.1-SNAPSHOT/README
```
Start the met-api application with:

met-api-0.1-SNAPSHOT/bin/met-api

To test that the application is running type
http://hostname:9000/v0/helloWorld in a browser or
try `wget http://hostname:9000/v0/helloWorld`

The result should be "hello world".

###Runnig unit tests.
To run unit, code coverage and style check.

  * **_Unit test_**:   `sbt clean  test`
  * **_code coverage_**:  `sbt clean coverage test` and to create the reports `sbt coverageReport`
  * **_style check_**: `sbt scalastyle`

##Setting up a development environment
We follow the [scala styleguide](http://docs.scala-lang.org/style/ "scala-lang Homepage").
scala support exist for eclipse, netbean, intelliJ, emacs, TextMate, etc.

###Setting up Eclipse
The version of scala-ide we use is 4.0.0.

The easiest way to install a development enviromnemt for scala is to download
a eclipse bundle http://scala-ide.org/download/sdk.html. This is the
recomended way to install eclipse if you are not familiar with eclipse.

If you allready have eclipse installed. Add scala-ide to the sites eclipse look
for new plugins. Choose the update site according to your installation of eclipse.

  * Luna: http://download.scala-ide.org/sdk/lithium/e44/scala211/stable/site
  * Kepler: http://download.scala-ide.org/sdk/lithium/e38/scala211/stable/site

To add the update site to "known sites" select the menu item "help->install new software".
In the dialog box that pop up push the add button. Copy the url into the "Location"
field and give it a name in the "Name" field, ex scala-ide 4.0.

Now you can install "Scala IDE for eclipse" and "Scala IDE plugins (incubation)".
After a restart, scala-ide is ready.


Change the directory to where you cecked out the met-api from github. To create a
eclipse project use sbt:

   `sbt eclipse`

Now you can import the met-api into eclipse. In eclipse select menu "file->import".
In the dialog that pop up expand "General" and select "Existing Projects into Workspace".
Browse to the met-api dierctory and push the ok button. Select the "met-api"
project and push the ok button.

Every time the build.sbt or any of the files in the directory project/changes we
must rerun `sbt eclipse` and reimport the project.

###Style guide
As an help to comply to the style guide you can import the file

>met-api_style-eclipse_formatterPreferences.properties

To import it select menu item "window->preferences". In the dialog that pop up expand
"Scala->Editor->Formatter" and import the file.

To reformat a scala file to comply with the style guide: press Shift-Ctrl-F.

###Running tests
It is best to run the test on the command line with sbt as sbt is not so well integrated
in eclipe yet.

  * `sbt test` runs the unit testes.
  * `sbt scoverage:test` runs the code coverage.
  * `sbt scalastyle` checks if you comply with the style guide and do a few static checks.

###Running the met-api in development mode
To run the met-api in development mode, start it with:

`sbt run`

To run in developer mode with swagger-ui on localhost, start it with:

`sbt run -Dswagger.api.basepath="http://localhost:9000"`

The met-api starts up and print an url to the console. Paste this url into a web-wbrowser.
Each time you save some code change in eclipse and reload the page in the browser the
result of the code change will show up immediately in the browser.

##Scala and play tutorials
The met-api is implemented in [scala](http://scala-lang.org/ "scala homepage").
We use the [play framework](https://playframework.com/ "play homepage").

If you are not familiar with scala and play you can find tutorials at this links

  * scala: [scala tutorial](http://docs.scala-lang.org/tutorials/?_ga=1.146110064.575812132.1320647231)
  * play: [play tutorials](https://playframework.com/documentation/2.3.x/Tutorials)

A tip, as a help in learnig scala, is to use worksheet in eclipse.
See https://github.com/scala-ide/scala-worksheet/wiki/Getting-Started.
(The person who wrote the tutorial uses obviously mac. If you are using Unix
(Linux, frebsd etc) or  Windows Substitute all use of CMD with Ctl :-) )
