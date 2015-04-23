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
import models.{ Point }

import java.text.SimpleDateFormat
import java.util.Date


class PointData {

  val points: ListBuffer[Point] = new ListBuffer[Point]()
  val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
  val refTime0 = format.parse("2015-02-11T00:00:00")
  val refTime1 = format.parse("2015-02-11T12:00:00")
  val refTime2 = format.parse("2015-02-12T00:00:00")
  
  // scalastyle:off magic.number
  points += Point(12.5f, "KS18700", "Blindern", refTime0, refTime0, "air_temperature", 0, 0, 1, refTime0)
  points += Point(-1.5f, "KS18700", "Blindern", refTime1, refTime1, "air_temperature", 0,  0, 1, refTime1)
  points += Point(3.5f, "KS18700", "Blindern", refTime2, refTime2, "air_temperature", 0, 0, 1, refTime2)
  points += Point(5.7f, "KS18701", "Bergen", refTime0, refTime0, "air_temperature", 0, 2, 1, refTime0)
  points += Point(13.0f, "KS18702", "Smestad", refTime0, refTime0, "air_temperature", 0, 0, 1, refTime0)
  points += Point(11.2f, "KS18703", "Skien", refTime0, refTime0, "air_temperature", 0, 1, 1, refTime0)
  points += Point(6.0f, "KS18800", "Sollihogda", refTime0, refTime0, "air_temperature", 0, 1, 1, refTime0)
  // scalastyle:on magic.number

  def getPoints(source: String): java.util.List[Point] = {
   var sources = source.split(",")
    var result = new java.util.ArrayList[Point]()
    for (point <- points) {
      if (sources.contains(point.source)) {
        result.add(point)
      }
    }
    result
  }

}
