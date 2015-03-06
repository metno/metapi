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

package util

import com.fasterxml.jackson.annotation.JsonInclude._
import com.fasterxml.jackson.core.JsonGenerator.Feature
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object JsonUtil {
  val m = new ObjectMapper()
  m.setSerializationInclusion(Include.NON_EMPTY)
  m.registerModule(new DefaultScalaModule())
  m.setSerializationInclusion(Include.NON_NULL);
  m.setSerializationInclusion(Include.NON_DEFAULT)
  m.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
  m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  m.enable(SerializationFeature.INDENT_OUTPUT);

  val mapper = m
}
