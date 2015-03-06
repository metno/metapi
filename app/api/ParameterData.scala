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
import models.{ Parameter }

import java.text.SimpleDateFormat
import java.util.Date


class ParameterData {
  val parameters: ListBuffer[Parameter] = new ListBuffer[Parameter]()
  
  parameters += Parameter("air_temperature", "Air Temperature", "K", "Timeverdi: Aritmetisk middel av minuttverdier Døgnverdi: Aritmetisk middel av 24 timeverdier (kl 00-00), evt formelbasert middelverdi ut fra færre observasjoner (kl 18-18)Månedsverdi: Aritmetisk middel av døgn-TAM [This should be english, unless no has been chosen for the namespace]" )
  parameters += Parameter("precipitation_amount", "Precipitation Amount", "m", "Døgn- eller månedssum for nedbør (nedbørdøgn 07-07) [This should be english, unless no has been chosen for the namespace]" )
  parameters += Parameter("height_above_mean_sea_level", "Height Above Mean Sea Level", "m", "The height above mean sea level in meters" )

  def getParameters(id: Option[String]): java.util.List[Parameter] = {
    var ids = id match {
      case Some(id) => id.split(",") 
      case None => Array[String]()
    }
    var result = new java.util.ArrayList[Parameter]()
    for (parameter <- parameters) {
      if (ids.size == 0) {
        result.add(parameter)
      }
      else if (ids.contains(parameter.id)) {
        result.add(parameter)
      }
    }
    result
  }
  
  def getParameterById(id: String): Option[Parameter] = {
    parameters.filter(parameter => parameter.id == id) match {
      case parameters if(parameters.size) > 0 => Some(parameters.head)
      case _ => None
    }
  }

  def addParameter(param: Parameter): Option[Parameter] = {
    parameters.filter(parameter => parameter.id == param.id) match {
      case parameters if (parameters.size) > 0 => None
      case _ => {
        parameters += param
        Some(param)
      }
    }
  }  

  def updateParameter(param: Parameter): Option[Parameter] = {
    parameters.filter(parameter => parameter.id == param.id) match {
      case parameters if (parameters.size) > 0 => {
        parameters --= parameters.filter(existing => existing.id == param.id)
        parameters += param
        Some(param)
      }
      case _ => None
    }
  }  
  
}
