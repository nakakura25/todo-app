package controllers.api

import lib.model.form.{TodoForm, TodoFormData}
import lib.model.json.{
  CategoryToJson,
  Color,
  ColorToJson,
  StatusToJson,
  TodoFromJson,
  TodoToJson
}
import lib.model.{Category, Todo}
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import service.{CategoryService, ColorService}

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class CategoryApiController @Inject() (
    val controllerComponents: ControllerComponents,
    val categoryService:      CategoryService
)(implicit ec:                ExecutionContext)
    extends BaseController
    with I18nSupport {

  import lib.persistence.default._

  def index() = Action async { implicit req =>
    for {
      categories <- CategoryRepository.list().map(cats => cats.map(_.v))
    } yield {
      val jsonCategories = Json.toJson(
        categories.map(category => CategoryToJson(category))
      )
      val jsonColor      = Json.toJson(
        ColorService
          .getColorMap()
          .map(c => {
            val color = Color(c._1, c._2)
            ColorToJson(color)
          })
      )
      val map            = Map(
        "category" -> jsonCategories,
        "color"    -> jsonColor
      )
      Ok(Json.toJson(map))
    }
  }

  def store() = Action(parse.json) async { implicit request: Request[JsValue] =>
    ???
  }

  def update() = Action(parse.json) async {
    implicit request: Request[JsValue] =>
      ???
  }

  def delete(id: Long) = Action async { implicit request: Request[AnyContent] =>
    ???
  }
}
