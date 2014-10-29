FROM mpejcoch/sbt-layer
MAINTAINER Martin Grønlien Pejcoch <mpejcoch@gmail.com>

RUN git clone https://github.com/metno/met-api.git

WORKDIR /root/met-api
RUN sbt clean stage dist

RUN mkdir -p /opt/metapi