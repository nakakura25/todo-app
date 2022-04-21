package controllers.api

import lib.model.json._
import lib.model.{Category, Todo}
import play.api.i18n.I18nSupport
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
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

  def index() = Action async { implicit req =>
    for {
      (todos, categories) <-
        TodoRepository.list().map(todos => todos.map(_.v)) zip
          categoryService.getCategoryMap()
    } yield {
      val jsonTodos    = Json.toJson(
        todos.map(todo =>
          TodoToJson(
            todo,
            categories(todo.categoryId.getOrElse(Category.Id(0L)))
          )
        )
      )
      val jsonColor    = Json.toJson(
        ColorService
          .getColorMap()
          .map(c => {
            val color = Color(c._1, c._2)
            ColorToJson(color)
          })
      )
      val jsonCategory = Json.toJson(
        categories.map(cat => CategoryToJson(cat._2)).toSeq
      )
      val jsonStatus   = Json.toJson(
        Todo.Status.values.map(status => StatusToJson(status))
      )
      val map          = Map(
        "todos"    -> jsonTodos,
        "color"    -> jsonColor,
        "category" -> jsonCategory,
        "status"   -> jsonStatus
      )
      Ok(Json.toJson(map))
    }
  }

  def store() = Action(parse.json) async { implicit request: Request[JsValue] =>
    val todo: Todo#WithNoId = request.body
      .validate[TodoFromJson]
      .fold(
        errors => {
          throw new Exception("json validation errors")
        },
        todoFromJson => {
          Todo.build(
            Category.Id(todoFromJson.categoryId),
            todoFromJson.title,
            todoFromJson.body,
            Todo.Status(todoFromJson.state)
          )
        }
      )
    for {
      _ <- TodoRepository.add(todo)
    } yield {
      NoContent
    }
  }

  def update() = Action(parse.json) async {
    implicit request: Request[JsValue] =>
      val todo: Todo#EmbeddedId = request.body
        .validate[TodoFromJson]
        .fold(
          errors => {
            throw new Exception("json validation errors")
          },
          todoFromJson => {
            Todo(
              Some(Todo.Id(todoFromJson.id)),
              Some(Category.Id(todoFromJson.categoryId)),
              todoFromJson.title,
              todoFromJson.body,
              Todo.Status(todoFromJson.state)
            ).toEmbeddedId
          }
        )
      for {
        _ <- TodoRepository.update(todo)
      } yield {
        NoContent
      }
  }

  def delete(id: Long) = Action async { implicit request: Request[AnyContent] =>
    for {
      _ <- TodoRepository.remove(Todo.Id(id))
    } yield NoContent
  }
}
