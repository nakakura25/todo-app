package controllers.api

import lib.model.json._
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc._
import service.ColorService

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class ColorApiController @Inject() (
    val controllerComponents: ControllerComponents
)(implicit ec:                ExecutionContext)
    extends BaseController
    with I18nSupport {

  def get() = Action { implicit req =>
    val jsonColor = Json.toJson(
      ColorService
        .getColorMap()
        .map(c => {
          val color = Color(c._1, c._2)
          ColorToJson(color)
        })
    )
    Ok(jsonColor)
  }
}
