package chandu0101.scalajs.location.tracker.routes

import chandu0101.scalajs.location.tracker.pages.HomePage
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.{BaseUrl, Redirect, RoutingRules}


/**
 * Created by chandrasekharkode on 2/25/15.
 */
object AppRouter {


  object AppPage extends RoutingRules {

    val root = register(rootLocation(HomePage()))

    register(removeTrailingSlashes)

    override protected val notFound = redirect(root, Redirect.Replace)


    override protected def interceptRender(i: InterceptionR): ReactElement =
      i.element

  }

  val baseUrl = BaseUrl.fromWindowOrigin / "scalajs-location-tracker/"
  // val baseUrl = BaseUrl.fromWindowOrigin / "sjspt/"

  val C = AppPage.router(baseUrl)
}
