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

@ApiModel("Parameter")
case class Parameter(
  @(ApiModelProperty @field)(position = 1, value = "ID of the parameter")
    id: String,
  @(ApiModelProperty @field)(position = 2, value = "Name of the parameter")
    name: String,
  @(ApiModelProperty @field)(position = 3, value = "SI unit of measure for the parameter")
    unit: String,
  @(ApiModelProperty @field)(position = 4, value = "Description of the parameter")
    description: String
)