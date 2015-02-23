package controllers
import play.api._
import play.api.mvc._
import com.wordnik.swagger.annotations._
import play.api.mvc.MultipartFormData._
import play.api.mvc.BodyParsers.parse
import java.io.File
import util.HttpStatus
@Api(value = "/fileservice", description = "Proof of concept file service")
class FileServiceController extends BaseApiController {

  //curl  -i -X POST http://localhost:9000/v0/fileservice -H "Content-Type: application/octet-stream" --data-binary "@test.sh"
  @ApiOperation(nickname = "uploadFile",
    value = "Post a file to the server",
    notes = "This is just a temporart proof of conept API",
    response = classOf[Void], httpMethod = "POST")
  @ApiResponses(Array(
    new ApiResponse(code = HttpStatus.BAD_REQUEST, message = "Invalid file format")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "File to upload from file system.", required = true, dataType = "file", paramType = "body")))
  def upload = Action(parse.file(to = new File("/tmp/upload"))) { request =>
    Ok("Saved the request content to " + request.body)
  }

  //curl -i  -F fileName=@test.sh http://localhost:9000/v0/fileservice/uploadMultipart
  @ApiOperation(nickname = "uploadForm",
    value = "Uploads multipartFormData",
    notes = "This is just a temporart proof of conept API",
    response = classOf[Void], httpMethod = "POST")
  @ApiResponses(Array(
    new ApiResponse(code = HttpStatus.BAD_REQUEST, message = "Invalid file format")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "File to upload from file system.", required = true, dataType = "file", paramType = "body")))
  def uploadMultipart = Action(parse.multipartFormData) { request =>
    request.body.file("fileName").map { payload =>
      import java.io.File
      val filename = payload.filename
      val contentType = payload.contentType
      println("Filname: " + filename)
      payload.ref.moveTo(new File("/tmp/picture"))
      Ok("File uploaded")
    }.getOrElse {
      Redirect(routes.Application.index).flashing(
        "error" -> "Missing file")
    }
  }
}