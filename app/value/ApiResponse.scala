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
package value
import javax.xml.bind.annotation._

object ApiResponse {
  val ERROR = 1
  val WARNING = 2
  val INFO = 3
  val OK = 4
  val TOO_BUSY = 5
}

@XmlRootElement
class ApiResponse(@XmlElement var code: Int, @XmlElement var message: String) {

  @XmlTransient
  def getCode(): Int = code
  def setCode(code: Int): Unit = this.code = code

  def getType(): String = code match {
    case ApiResponse.ERROR    => "error"
    case ApiResponse.WARNING  => "warning"
    case ApiResponse.INFO     => "info"
    case ApiResponse.OK       => "ok"
    case ApiResponse.TOO_BUSY => "too busy"
    case _                    => "unknown"
  }

  def getMessage(): String = message
  def setMessage(message: String): Unit = this.message = message
}
