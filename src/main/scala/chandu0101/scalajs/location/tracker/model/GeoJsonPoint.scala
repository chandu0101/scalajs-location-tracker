package chandu0101.scalajs.location.tracker.model

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{literal => json}

/**
 * Created by chandrasekharkode on 2/27/15.
 */

case class GeoJsonPoint(tpe : String = "Feature" , geometry : Geometry ,properties : GeoProperties ) {
  def toJson = json( "properties" -> properties.toJson , "geometry" -> geometry.toJson , "type" -> tpe )
}

object GeoJsonPoint {
  def fromJson(obj : js.Dynamic) = GeoJsonPoint(properties = GeoProperties.fromJson(obj.properties),geometry = Geometry.fromJson(obj.geometry),tpe = obj.`type`.toString)
}

case class Geometry(tpe : String = "Point",coordinates : Coordinates) {
  def toJson = json( "coordinates" -> coordinates.toJson , "type" -> tpe )
}

object Geometry {
  def fromJson(obj : js.Dynamic) = Geometry(coordinates = Coordinates.fromJson(obj.coordinates),tpe = obj.`type`.toString)
}
case class Coordinates(lng : Double,lat : Double) {
  def toJson = js.Array(lng,lat)
  
}

object Coordinates {
  
  def fromJson(obj : js.Dynamic) = {
    val ar = obj.asInstanceOf[js.Array[Double]]
    Coordinates(ar.head,ar.last)
  }
  
}

case class GeoProperties(timestamp : Double) {
  def toJson = json( "timestamp" -> timestamp )
}

object GeoProperties {
  def fromJson(obj : js.Dynamic) = GeoProperties(timestamp = obj.timestamp.asInstanceOf[Double])
}