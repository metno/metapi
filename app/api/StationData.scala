/*
    MET-API

    Copyright (C) 2015 met.no
    Contact information:
    Norwegian Meteorological Institute
    Box 43 Blindern
    0313 OSLO
    NORWAY
    E-mail: met-api@met.no

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
    MA 02110-1301, USA
*/
package api
import collection.mutable.ListBuffer
import models.{ Station, Level }

import java.text.SimpleDateFormat
import java.util.Date


class StationData {
  val stations: ListBuffer[Station] = new ListBuffer[Station]()
  val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
  val refTime0 = format.parse("2015-02-11T00:00:00")
  val refTime1 = format.parse("2015-02-11T12:00:00")
  val refTime2 = format.parse("2015-02-12T00:00:00")
  
  // scalastyle:off magic.number
  stations += Station("KS18700", "Oslo - Blindern", "1931-01-01", 1492, "POINT(59.9423 10.7200)", models.Level(94, "m", "height_above_mean_sea_level"), "301", "OSLO", "NO" )
  stations += Station("KS18800", "Bygdøy", "1931-01-01/1941-01-01", 0, "POINT(59.9000 10.6667)", models.Level(23, "m", "height_above_mean_sea_level"), "301", "OSLO", "NO" )
  // scalastyle:on magic.number

  def getStations(source: Option[String]): java.util.List[Station] = {
    var sources = source match {
      case Some(source) => source.split(",") 
      case None => Array[String]()
    }
    var result = new java.util.ArrayList[Station]()
    for (station <- stations) {
      if (sources.size == 0) {
        result.add(station)
      }
      else if (sources.contains(station.id)) {
        result.add(station)
      }
    }
    result
  }
  
  def getStationById(id: String): Option[Station] = {
    stations.filter(station => station.id == id) match {
      case stations if(stations.size) > 0 => Some(stations.head)
      case _                             => None
    }
  }
  
}
