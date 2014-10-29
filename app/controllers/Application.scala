package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("BORA prototype is ready."))
  }
  
  def observation( format : String ) = Action { req =>  
    val q = req.queryString
    val qs = for( s <- q  ) yield {
      val k = s._1 
      val v = s._2 
     
      val ss = k match {
        case "n" => "n => '" + v.mkString("") + "'"
        case _ => k + "(unknown)"
      }
      ss.toString()
    }
    
    Ok("observation: " + format + ": '" + req + ": " + qs)
  }

}