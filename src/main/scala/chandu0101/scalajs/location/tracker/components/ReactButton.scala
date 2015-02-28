package chandu0101.scalajs.location.tracker.components

import chandu0101.scalajs.react.components.all._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._

import scala.scalajs.js

/**
 * Created by chandrasekharkode on 2/27/15.
 */


object ReactButton {

  trait Style {

    val button = Seq(backgroundColor := "#F2706D",
      border := "1px solid transparent",
      boxShadow := "0 1px 3px 0 rgba(0, 0, 0, 0.12), 0 1px 2px 0 rgba(0, 0, 0, 0.24)",
      color := "#F5F4F4",
      cursor := "pointer",
      display := "inline-block",
      fontSize := "15px",
      textDecoration := "none",
      textAlignCenter,
      width := "180px",
      padding := "5px 7px",
      WebkitBoxShadow := "0 1px 3px 0 rgba(0, 0, 0, 0.12), 0 1px 2px 0 rgba(0, 0, 0, 0.24)")

    val buttonHover: TagMod = Seq(backgroundColor := "#DA423E",
      textDecoration := "none")

    val buttonDisable : TagMod = Seq(cursor := "default",disabled := true,backgroundColor := "rgb(161, 152, 152)")
  }

  case class State(buttonHover: Boolean = false)

  class Backend(t: BackendScope[Props, State]) {

    def onButtonClick(e: ReactEventI) = {
      if (!t.props.disable && t.props.onButtonClick != null) t.props.onButtonClick(e)
      e.preventDefault()
    }

    def onMouseEnter = t.modState(_.copy(buttonHover = true))

    def onMouseLeave = t.modState(_.copy(buttonHover = false))
  }

  val component = ReactComponentB[Props]("ReactButton")
    .initialState(State())
    .backend(new Backend(_))
    .render((P, S, B) => {
    val buttonStyle = styleSet1(P.style.button, P.style.buttonHover -> (S.buttonHover && !P.disable),P.style.buttonDisable -> P.disable)
    if (P.linkButton) a(buttonStyle, href := P.href, onMouseEnter --> B.onMouseEnter, onMouseLeave --> B.onMouseLeave)(P.name)
    else a(buttonStyle, onClick ==> P.onButtonClick, onMouseEnter --> B.onMouseEnter, onMouseLeave --> B.onMouseLeave)(P.name)
  })
    .build

  case class Props(name: String, onButtonClick: REventHUnit, linkButton: Boolean, href: String, style: Style,disable : Boolean )

  def apply(name: String, onButtonClick: REventHUnit = null, linkButton: Boolean = false, href: String = "", style: Style = new Style {}, ref: js.UndefOr[String] = "", key: js.Any = {},disable : Boolean = false) = component.set(key, ref)(Props(name, onButtonClick, linkButton, href, style,disable))

}
