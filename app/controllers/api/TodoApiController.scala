package controllers.api

import lib.model.{Category, Todo}
import lib.model.form.{TodoForm, TodoFormData}
import lib.model.json.{Color, ColorToJson, TodoToJson}
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import play.mvc.Http
import service.{CategoryService, ColorService}

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class TodoApiController @Inject() (
    val controllerComponents: ControllerComponents,
    val categoryService:      CategoryService
)(implicit ec:                ExecutionContext)
    extends BaseController
    with I18nSupport {

  import lib.persistence.default._

  val form: Form[TodoFormData] = TodoForm.form

  def index() = Action async { implicit req =>
    for {
      todos      <- TodoRepository.list().map(todos => todos.map(_.v))
      categories <- categoryService.getCategoryMap()
    } yield {
      val jsonTodos = Json.toJson(
        todos.map(todo =>
          TodoToJson(
            todo,
            categories(todo.categoryId.getOrElse(Category.Id(0L)))
          )
        )
      )
      val colors    = ColorService
        .getColorMap()
        .map(c => {
          val color = Color(c._1, c._2)
          ColorToJson(color)
        })
      val jsonColor = Json.toJson(colors)
      val map       = Map(
        "todos" -> jsonTodos,
        "color" -> jsonColor
      )
      Ok(Json.toJson(map)).as(JSON)
    }
  }

  def store() = Action async { implicit request: Request[AnyContent] =>
    form
      .bindFromRequest()
      .fold(
        (formWithErrors: Form[TodoFormData]) => {
          ???
        },
        (todoFormData: TodoFormData) => {
          ???
        }
      )
  }

  def update(id: Long) = Action async { implicit request: Request[AnyContent] =>
    form
      .bindFromRequest()
      .fold(
        (formWithErrors: Form[TodoFormData]) => {
          ???
        },
        (todoFormData: TodoFormData) => {
          ???
        }
      )
  }

  def delete(id: Long) = Action async { implicit request: Request[AnyContent] =>
    for {
      _ <- TodoRepository.remove(Todo.Id(id))
//      _ <- TodoRepository.list()
    } yield NoContent
  }

}
