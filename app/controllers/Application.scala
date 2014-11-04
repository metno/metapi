package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def Index = Action {
    Ok("METAPI: Needs Version")
  }
  
  def Observation( format : String ) = Action { req =>  
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
  
  def HelloWorld = Action {
    req =>
      
      Ok("Hello World")
      
  }
}