#met-api

##Prerequisite

The met-api is an jvm application and at least version 7 is required.
sbt is used to build the application. 

###Install instruction for ubuntu 14:04

Install java.  'apt-get install  openjdk-7-jdk'

Install sbt.
Download a debian package with wget http://dl.bintray.com/sbt/debian/sbt-0.13.5.deb and 
install with 'dpkg -i  sbt-0.13.5.deb'


##Build met-api

Clone met-api at github

git clone https://github.com/metno/met-api.git

Change directory too met-api.

The following command should build met-api.
The command will download all dependencies  

sbt clean stage dist

This will create a zip archive in the directory target/universal. 
Copy the zip file to where to deploy the met-api and unzip the file.

The layout of the archive is:

```
bora-prototype-1.0-SNAPSHOT/lib/
bora-prototype-1.0-SNAPSHOT/bin/
bora-prototype-1.0-SNAPSHOT/conf/application.conf
bora-prototype-1.0-SNAPSHOT/share/
bora-prototype-1.0-SNAPSHOT/README.md
bora-prototype-1.0-SNAPSHOT/README
```
To start the met-api application

bora-prototype-1.0-SNAPSHOT/bin/bora-prototype

To test that the application is running type
http://hostname:9000/v1/hello_world in a browser or
try 'wget http://hostname:9000/v1/hello_world'

The result should be "hello world".

 
