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
import models.{ Place }

import java.text.SimpleDateFormat
import java.util.Date


class PlaceData {
  val places: ListBuffer[Place] = new ListBuffer[Place]()

  places += Place("norway_oslo_oslo_blindern", "Blindern", "POINT(10.72307 59.94063)", "Oslo", "Oslo", "NO", "Part of a city" )
  places += Place("norway_sør-trøndelag_trondheim_trondheim", "Trondheim", "POINT(10.39506 63.43051)", "Trondheim", "Sør-Trøndelag", "NO", "City - large town" )
  places += Place("norway_rogaland_stavanger_stavanger", "Stavanger", "POINT(5.71796 58.96527)", "Stavanger", "Rogaland", "NO", "City - large town" )

  def getPlaces(id: Option[String]): java.util.List[Place] = {
    var ids = id match {
      case Some(id) => id.split(",")
      case None => Array[String]()
    }
    var result = new java.util.ArrayList[Place]()
    for (place <- places) {
      if (ids.size == 0) {
        result.add(place)
      }
      else if (ids.contains(place.id)) {
        result.add(place)
      }
    }
    result
  }

  def getPlaceById(id: String): Option[Place] = {
    places.filter(place => place.id == id) match {
      case places if(places.size) > 0 => Some(places.head)
      case _ => None
    }
  }

  def addPlace(pl: Place): Option[Place] = {
    places.filter(place => place.id == pl.id) match {
      case places if (places.size) > 0 => None
      case _ => {
        places += pl
        Some(pl)
      }
    }
  }

  def updatePlace(pl: Place): Option[Place] = {
    places.filter(place => place.id == pl.id) match {
      case places if (places.size) > 0 => {
        places --= places.filter(existing => existing.id == pl.id)
        places += pl
        Some(pl)
      }
      case _ => None
    }
  }

}
