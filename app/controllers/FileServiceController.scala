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

package controllers

import play.api._
import play.api.mvc._
import com.wordnik.swagger.annotations._
import play.api.mvc.MultipartFormData._
import play.api.mvc.BodyParsers.parse
import java.io.File
import util.HttpStatus
import play.api.libs.Files.TemporaryFile

//scalastyle:off public.methods.have.type

@Api(value = "/fileservice", description = "Proof of concept file service")
class FileServiceController extends BaseApiController {

  //curl -i  -F fileName=@test.sh http://localhost:9000/v0/fileservice/uploadMultipart
  @ApiOperation(nickname = "uploadForm",
    value = "Uploads as multipart form data",
    notes = "This is just a proof of conept API.",
    response = classOf[Void], httpMethod = "POST")
  @ApiResponses(Array(
    new ApiResponse(code = HttpStatus.BAD_REQUEST, message = "Invalid file format"),
    new ApiResponse(code = HttpStatus.BAD_REQUEST, message = "Missing file"),
    new ApiResponse( code= HttpStatus.UNAUTHORIZED, message= "Not authorized")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "File to upload from file system.", required = true, dataType = "file", paramType = "body")))
  //$COVERAGE-OFF$Disabling until we find out how to test multipart form data actions.
  def uploadMultipart = Action(parse.multipartFormData) { request =>
    FileServiceController.saveFile( request.body )
  }
  //$COVERAGE-ON$
}

object FileServiceController extends BaseApiController {

  def saveFile( multiPart: MultipartFormData[TemporaryFile] ):Result = {
    val files = multiPart.files.map { filepart =>
      import java.io.File
      val filename = filepart.filename
      val contentType = filepart.contentType
      val key = filepart.key
      val f = new File("/tmp/" + filename)

      filepart.ref.moveTo( f )
      f.delete() //Just remove the file for no.
      f
    } mkString(", ")

    if( files.length() > 0 ) Ok( "Uploaded file(s) " + files )
    else BadRequest("Missing file")
  }
}
