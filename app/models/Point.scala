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

package models
import com.wordnik.swagger.annotations._

import scala.annotation.meta.field
import java.util.Date

// scalastyle:off magic.number
@ApiModel("Point")
case class Point(
  @(ApiModelProperty @field)(position = 1, value = "The data value")
    value: Float,
  @(ApiModelProperty @field)(position = 2, value = "ID of the data source")
    source: String,
  @(ApiModelProperty @field)(position = 3, value = "Place (geographic location) of the data, specified using WGS84 coordinates in EWKT")
    place: String,
  @(ApiModelProperty @field)(position = 4, value = "Reference time (instant) of the data")
    refTime: Date,
  @(ApiModelProperty @field)(position = 5, value = "Valid time (instant or period) of the data")
    validTime: Date,
  @(ApiModelProperty @field)(position = 6, value = "Value parameter of the data (specified as a MET parameter)")
    parameter: String,
  @(ApiModelProperty @field)(position = 7, value = "Aggregation period of the data. 0 if the value is an instant value")
    aggregationPeriod: Int,
  @(ApiModelProperty @field)(position = 8, value = "The MET quality indicator of the data value")
    quality: Float,
  @(ApiModelProperty @field)(position = 9, value = "The data version (0 is always the original version; the number is incremented for each edit)")
    version: Int,
  @(ApiModelProperty @field)(position = 10, value = "The instant when the data value was stored in our systems")
    storetime: Date)
