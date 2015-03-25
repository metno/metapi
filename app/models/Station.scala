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
@ApiModel("Station")
case class Station(
  @(ApiModelProperty @field)(position = 1, value = "The MET ID of the source")
    id: String,
  @(ApiModelProperty @field)(position = 2, value = "The official name of the source")
    name: String,
  @(ApiModelProperty @field)(position = 3, value = "The period for which this source is/was valid/operational")
    validTime: String,
  @(ApiModelProperty @field)(position = 4, value = "The WMO number, if the source has one. Null otherwise.")
    wmoNo: Int,
  @(ApiModelProperty @field)(position = 5, value = "The place (geo-spatial location) of the source")
    currentPlace: String,
  @(ApiModelProperty @field)(position = 6, value = "The level (typically height) of the source")
    currentLevel: Level,
  @(ApiModelProperty @field)(position = 7, value = "The municipality ID of the source (outside Norway, this will typically be null)")
    municipalityId: String,
  @(ApiModelProperty @field)(position = 8, value = "The county ID of the source (outside Norway, this will typically be null)")
    countyId: String,
  @(ApiModelProperty @field)(position = 9, value = "The country ID of the source")
    countryId: String
)