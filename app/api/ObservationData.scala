/*
    MET-API

    Copyright (C) 2014 met.no
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
import models.{ Observation }

import java.text.SimpleDateFormat
import java.util.Date

class ObservationData {

  val observations: ListBuffer[Observation] = new ListBuffer[Observation]()
  val dateString = "2015-02-11T00:00:00"
  val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
  val refTime = format.parse(dateString)

  // scalastyle:off magic.number
  observations += Observation(1, "KS18700", "Blindern", refTime, refTime, "air_temperature", -4.5f, 1, 1)
  observations += Observation(2, "KS18700", "Solihøgda", refTime, refTime, "air_temperature", -1.5f, 1, 1)
  observations += Observation(3, "KS18700", "Sola", refTime, refTime, "air_temperature", 3.5f, 1, 1)
  observations += Observation(4, "KS18700", "Flesland", refTime, refTime, "air_temperature", 0.5f, 1, 1)
  // scalastyle:on magic.number

  def findObservationByPlace(place: String): Option[Observation] = {
    observations.filter(observation => observation.place == place) match {
      case observation if (observation.size) > 0 => Some(observation.head)
      case _ => None
    }
  }

  def addObservation(observation: Observation): Unit = {
    observations --= observations.filter(o => o.place == observation.place)
    observations += observation
  }

  def getObservationbyId(obsId: Long): Option[Observation] = {
    observations.filter(observation => observation.id == obsId) match {
      case observations if (observations.size) > 0 => Some(observations.head)
      case _ => None
    }
  }
}

