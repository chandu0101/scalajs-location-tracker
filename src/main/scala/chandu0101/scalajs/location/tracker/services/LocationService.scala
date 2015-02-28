package chandu0101.scalajs.location.tracker.services

import chandu0101.scalajs.facades.pouchdb.{PouchDB, ReplicateOptions}
import chandu0101.scalajs.location.tracker.model.GeoJsonPoint
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

/**
 * Created by chandrasekharkode on 2/27/15.
 */
object LocationService {

  val DB_NAME = "locationtracker"
  
  val localStore = PouchDB.create(DB_NAME)
  val remoteStore = s"https://himparicedgentlyrinducha:54DuBUgpst5MGEUli2lVUiSd@chandu0101.cloudant.com/$DB_NAME"

  def save(loc : GeoJsonPoint) : Future[Future[js.Dynamic]] = {
    localStore.post(loc.toJson).map(doc => localStore.get(doc.id.toString).map(item => item))
  }
  
  def startSync : Unit = {
    var timeout = 10000 // 10secs
    var increment = 2
    localStore.replicate.to(remoteStore,ReplicateOptions(live= true))
      .onChange((resp : js.Dynamic) => timeout = 10000 )// reset retry timer when user came back on
      .onError((err: js.Dynamic) =>
      dom.setTimeout(() => {
        timeout *= increment
        startSync
      },timeout)
      )
  }
}
