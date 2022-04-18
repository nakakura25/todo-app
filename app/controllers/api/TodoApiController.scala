package controllers.api

import lib.model.form.{TodoForm, TodoFormData}
import lib.model.json.TodoToJson
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.libs.json.Json
import play.api.mvc._
import service.CategoryService

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
      todos <- TodoRepository.list().map(todos => todos.map(_.v))
    } yield {
      val jsonTodos: Seq[TodoToJson] = todos.map(todo => TodoToJson(todo))
      Ok(Json.toJson(jsonTodos))
    }
  }

  def catmap() = Action async { implicit req =>
    for {
      categories <- categoryService.getCategoryMapToJson()
    } yield {
      println(categories)
      println(Json.toJson(categories))
      Ok(Json.toJson(categories))
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
    ???
  }

}
