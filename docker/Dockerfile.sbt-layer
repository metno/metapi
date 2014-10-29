FROM ubuntu:trusty
MAINTAINER Martin Grønlien Pejcoch <mpejcoch@gmail.com>

RUN DEBIAN_FRONTEND=noninteractive apt-get -qq -y update
RUN DEBIAN_FRONTEND=noninteractive apt-get -qq -y install apt-utils
RUN DEBIAN_FRONTEND=noninteractive apt-get -qq -y install git wget openjdk-7-jdk unzip

WORKDIR /root
RUN git clone https://github.com/metno/met-api.git
RUN wget http://dl.bintray.com/sbt/debian/sbt-0.13.5.deb
RUN dpkg -i sbt-0.13.5.deb