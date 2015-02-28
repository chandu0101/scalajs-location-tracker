package chandu0101.scalajs.location.tracker.pages

import chandu0101.scalajs.facades.pouchdb.PouchDBException
import chandu0101.scalajs.location.tracker.components.ReactButton
import chandu0101.scalajs.location.tracker.model.{Coordinates, GeoJsonPoint, GeoProperties, Geometry}
import chandu0101.scalajs.location.tracker.services.LocationService
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._
import org.scalajs.dom
import org.scalajs.dom.raw.{Position, PositionError}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.scalajs.js.Date
import scala.util.{Failure, Success}

/**
 * Created by chandrasekharkode on 2/25/15.
 */
object HomePage {

  
  case class State(lat : Double = 0, lng : Double = 0,
                   disableStartButton : Boolean = false,
                   disableStopButton : Boolean = true ,
                   status : String = "not recording",
                   message : String = "" )

  case class Backend(t : BackendScope[Unit,State]) {
    
    var watchID : Int = 0
    
    def handleStartTracking(e : ReactEventH) = {
      t.modState(_.copy(disableStartButton = true,
      disableStopButton = false,
      status = "tracking .."))
      watchID = dom.navigator.geolocation.watchPosition(handleGeolocationResponse _,handleGeolocationError _)
    }
    
    def handleStopTracking(e : ReactEventH) = {
      t.modState(_.copy(disableStartButton = false,
      disableStopButton = true,
      status = "not tracking"))
      if(watchID > 0) dom.navigator.geolocation.clearWatch(watchID)
    }
    
    def handleGeolocationResponse(position : Position) = {
      val lat = position.coords.latitude
      val lng = position.coords.longitude
      if(lat != t.state.lat && lng != t.state.lng) { // update only when new value is different than previous
        val timestamp : Double = position.asInstanceOf[js.Dynamic].timestamp.asInstanceOf[Double]
        val coord = GeoJsonPoint(geometry = Geometry(coordinates = Coordinates(lng,lat)) ,properties = GeoProperties(timestamp = timestamp))
        LocationService.save(coord).onComplete {
          case Success(resp) => {
            resp.onComplete {
              case Success(doc) => {
                val point = GeoJsonPoint.fromJson(doc)
                t.modState(_.copy(
                  lat = point.geometry.coordinates.lat,
                  lng = point.geometry.coordinates.lng,
                  message = new Date(point.properties.timestamp*1000).toString()
                ))
              }
              case Failure(ex) => {
                println(s" Error while getting ${ex.asInstanceOf[PouchDBException].err}")
                t.modState(_.copy(status = "Error occurred while getting doc from PouchDB"))
              }
            }
          }
          case Failure(ex) => {
            println(s" Error while saving ${ex.asInstanceOf[PouchDBException].err}")
            t.modState(_.copy(status = "Error occurred while saving doc to PouchDB"))
          }
        }

      }
    }
    
    def handleGeolocationError(error : PositionError) = {
      t.modState(_.copy(
      disableStartButton = true,
      status = s"Error Code : ${error.code} Message : ${error.message }"
      ))
    }
  }
  
  val component = ReactComponentB[Unit]("HomePage")
    .initialState(State())
    .backend(new Backend(_))
    .render((P,S,B) => {
      div( cls := "homepage",
        h2("Location Tracker"),
        div(cls := "tracker",
         span(color := "red" ,s"Status : ${S.status}"),
         h4("longitude : " ,S.lng > 0 ?= S.lng),
         h4("latitude : " ,S.lat > 0 ?= S.lng ,paddingBottom := "20px"),
         ReactButton(name = " Start Tracking",onButtonClick = B.handleStartTracking , disable = S.disableStartButton),
         br(),
         br(),
         ReactButton(name = " Stop Tracking",onButtonClick = B.handleStopTracking , disable = S.disableStopButton),
         S.message.nonEmpty ?= div(paddingTop := "20px" ,S.message)
        )
      )
    })
    .componentDidMount(scope => {
     if(js.isUndefined(dom.window.navigator.geolocation)) { // geolocation not supported
       scope.modState(_.copy(disableStartButton = true,status = "Geolocation not supported by your Browser"))
     } else {
       LocationService.startSync
     }
    })
    .buildU

  def apply() = component()

}
