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

package util

object HttpStatus {

  final val CONTINUE = 100
  final val SWITCHING_PROTOCOLS = 101
  final val PROCESSING = 102

  final val OK = 200
  final val CREATED = 201
  final val ACCEPTED = 202
  final val NON_AUTHORITATIVE_INFORMATION = 203
  final val NO_CONTENT = 204
  final val RESET_CONTENT = 205
  final val PARTIAL_CONTENT = 206
  final val MULTI_STATUS = 207

  final val MULTIPLE_CHOICES = 300
  final val MOVED_PERMANENTLY = 301
  final val FOUND = 302
  final val SEE_OTHER = 303
  final val NOT_MODIFIED = 304
  final val USE_PROXY = 305
  final val TEMPORARY_REDIRECT = 307
  final val BAD_REQUEST = 400
  final val UNAUTHORIZED = 401
  final val PAYMENT_REQUIRED = 402
  final val FORBIDDEN = 403
  final val NOT_FOUND = 404
  final val METHOD_NOT_ALLOWED = 405
  final val NOT_ACCEPTABLE = 406
  final val PROXY_AUTHENTICATION_REQUIRED = 407
  final val REQUEST_TIMEOUT = 408
  final val CONFLICT = 409
  final val GONE = 410
  final val LENGTH_REQUIRED = 411
  final val PRECONDITION_FAILED = 412
  final val REQUEST_ENTITY_TOO_LARGE = 413
  final val REQUEST_URI_TOO_LARGE = 414
  final val UNSUPPORTED_MEDIA_TYPE = 415
  final val REQUEST_RANGE_NOT_SATISFIABLE = 416
  final val EXPECTATION_FAILED = 417
  final val UNPROCESSABLE_ENTITY = 422
  final val LOCKED = 423
  final val FAILED_DEPENDENCY = 424
  final val NO_CODE = 425
  final val UPGRADE_REQUIRED = 426
  final val RETRY_WITH = 449

  final val INTERNAL_SERVER_ERROR = 500
  final val NOT_IMPLEMENTED = 501
  final val BAD_GATEWAY = 502
  final val SERVICE_UNAVAILABLE = 503
  final val GATEWAY_TIMEOUT = 504
  final val HTTP_VERSION_NOT_SUPPORTED = 505
  final val VARIANT_ALSO_NEGOTIATES = 506
  final val INSUFFICIENT_STORAGE = 507
  final val BANDWIDTH_LIMIT_EXCEEDED = 509
  final val NOT_EXTENDED = 510

}
