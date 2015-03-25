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

import scala.util.Try
import javax.ws.rs.QueryParam
import java.nio.file.Path
import java.nio.file.Files
import java.nio.charset.StandardCharsets
import java.nio.file.FileSystems
import java.io.BufferedWriter
import java.io.File
import play.api._
import play.api.mvc._
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData._
import play.api.mvc.BodyParsers.parse
import com.wordnik.swagger.annotations._
import util.HttpStatus

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

  @ApiOperation(nickname = "download",
    value = "Download a file.",
    notes = "This is just a proof of conept API.",
    response = classOf[Void], httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = HttpStatus.OK, message = "OK: The request was successful."),
    new ApiResponse(code = HttpStatus.NOT_FOUND, message = "The referenced file was not found."),
    new ApiResponse( code= HttpStatus.UNAUTHORIZED, message= "Not authorized")))
  def download(
       @ApiParam(value = "ID of the file to download. Use an id returned by upload", allowMultiple = false)@QueryParam("refid")
       refid: String ) = Action {
    implicit request =>
      FileServiceController.getFile( refid )
        .map { f =>
          Ok.sendFile( f , inline=true)
            .withHeaders( CONTENT_DISPOSITION->("attachment; filename=" + f.toPath().getFileName()),
                          CONTENT_TYPE->"application/octet-stream",
                          CACHE_CONTROL->"private, no-cache, no-store, must-revalidate");
        }
        .getOrElse { NotFound(s"No files matching refid '${refid}' was found.") }
  }
  //$COVERAGE-ON$
}



object FileServiceController extends BaseApiController {

  def saveFile( multiPart: MultipartFormData[TemporaryFile] ):Result = {
    val files = multiPart.files.map { filepart =>
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

  def getFile( ref: String ): Option[File] = {
      ref match {
          case "dummyref" => refidToFile( ref )
          case _ => None
      }
   }

   def refidToFile( file: String ): Option[File] = {
      val path: Path = FileSystems.getDefault().getPath( "/tmp", file + "123")
      if( Files.exists( path ) ) Option( path.toFile() )
      else createDummyFile( path )
   }

   def createDummyFile( path: Path ) : Option[File] = {
     Try {
       var f: BufferedWriter = Files.newBufferedWriter( path,  StandardCharsets.UTF_8 )
       for( b <- 0 until 10000000) f.write( 'b' )
       f.close()
       Option( path.toFile() )
     } getOrElse None
   }
}
