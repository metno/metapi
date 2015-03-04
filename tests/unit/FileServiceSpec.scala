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
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData._
import play.api.mvc.MultipartFormData
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.Files

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class FileServiceSpec extends Specification {

  "Application" should {

    "return 'ok' when uploading a valid file using the fileservice" in new WithApplication {
      val f = TemporaryFile("to_upload","tmp")
      val  theFile: Path = FileSystems.getDefault().getPath("tests", "unit", "ChristmasTux2014.svg.gz")
      Files.copy( theFile, Files.newOutputStream( f.file.toPath() ) );
      val p = FilePart("filename", "ChristmasTux2014.svg.gz", None, f )
      val multipartBody = MultipartFormData(Map(), Seq(p), Seq(), Seq())

      val result = controllers.FileServiceController.saveFile( multipartBody )

      result.header.status must equalTo( OK )
    }

    "return 'bad request' if an empty file is POSTed on the fileservice" in new WithApplication {
      val multipartBody = MultipartFormData(Map(), Seq[FilePart[TemporaryFile]](), Seq(), Seq())
      val result = controllers.FileServiceController.saveFile( multipartBody )
      result.header.status must equalTo( BAD_REQUEST )
    }

  }
}
