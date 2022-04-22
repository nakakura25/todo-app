package controllers.api

import lib.model.Todo
import lib.model.json._
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class StatusApiController @Inject() (
    val controllerComponents: ControllerComponents
)(implicit ec:                ExecutionContext)
    extends BaseController
    with I18nSupport {

  def get() = Action { implicit req =>
    val jsonStatus = Json.toJson(
      Todo.Status.values.map(status => StatusToJson(status))
    )
    Ok(jsonStatus)
  }
}
