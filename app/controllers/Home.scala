/** to do sample project
  */

package controllers

import lib.model.form.{TodoForm, TodoFormData}
import lib.model.{Category, Todo}
import model.ViewValueHome
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc._
import service.{CategoryService, ColorService}

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject() (
    val controllerComponents: ControllerComponents,
    val categoryService:      CategoryService
)(implicit ec:                ExecutionContext)
    extends BaseController
    with I18nSupport {

  import lib.persistence.default._

  val form: Form[TodoFormData] = TodoForm.form
  val vvList                   = ViewValueHome(
    title  = "Todo一覧",
    cssSrc = Seq("main.css"),
    jsSrc  = Seq("main.js")
  )
  val vvStore                  = ViewValueHome(
    title  = "登録画面",
    cssSrc = Seq(),
    jsSrc  = Seq()
  )
  val vvUpdate                 = ViewValueHome(
    title  = "更新画面",
    cssSrc = Seq(),
    jsSrc  = Seq()
  )
  val vv404                    = ViewValueHome(
    title  = "404 Not Found",
    cssSrc = Seq(),
    jsSrc  = Seq()
  )

  def index() = Action async { implicit req =>
    for {
      todos      <- TodoRepository.list().map(todos => todos.map(_.v))
      categories <- categoryService.getCategoryMap()
    } yield {
      Ok(views.html.Home(vvList, todos, categories, ColorService.getColorMap()))
    }
  }

  /** 登録画面の表示用
    */
  def register() = Action async { implicit request: Request[AnyContent] =>
    for {
      categoryOption <- categoryService.getCategoryOptions()
    } yield {
      Ok(views.html.todo.store(vvStore, form, categoryOption))
    }
  }

  def store() = Action async { implicit request: Request[AnyContent] =>
    form
      .bindFromRequest()
      .fold(
        (formWithErrors: Form[TodoFormData]) => {
          for {
            categoryOption <- categoryService.getCategoryOptions()
          } yield {
            BadRequest(
              views.html.todo.store(vvStore, formWithErrors, categoryOption)
            )
          }
        },
        (todoFormData: TodoFormData) => {
          for {
            _ <- TodoRepository.add(
                   Todo.build(
                     todoFormData.category,
                     todoFormData.title,
                     todoFormData.body,
                     Todo.Status.IS_NOT_YET
                   )
                 )
          } yield {
            Redirect(routes.HomeController.index())
          }
        }
      )
  }

  def edit(id: Long) = Action async { implicit request: Request[AnyContent] =>
    for {
      todoOption      <- TodoRepository.get(Todo.Id(id))
      categoryOptions <- categoryService.getCategoryOptions()
    } yield {
      val statusOption =
        Todo.Status.values.map(status => (status.code.toString, status.name))
      todoOption match {
        case Some(todo) =>
          Ok(
            views.html.todo.edit(
              vvUpdate,
              form.fill(
                TodoFormData(
                  todo.v.title,
                  todo.v.body,
                  todo.v.categoryId.getOrElse(Category.Id(0L)),
                  todo.v.state
                )
              ),
              categoryOptions,
              statusOption,
              id
            )
          )
        case None       => NotFound(views.html.error.page404(vv404))
      }
    }
  }

  def update(id: Long) = Action async { implicit request: Request[AnyContent] =>
    form
      .bindFromRequest()
      .fold(
        (formWithErrors: Form[TodoFormData]) => {
          for {
            categoryOptions <- categoryService.getCategoryOptions()
          } yield {
            val statusOption =
              Todo.Status.values
                .map(status => (status.code.toString, status.name))
            BadRequest(
              views.html.todo.edit(
                vvUpdate,
                formWithErrors,
                categoryOptions,
                statusOption,
                id
              )
            )
          }
        },
        (todoFormData: TodoFormData) => {
          for {
            _ <- TodoRepository.update(
                   Todo(
                     Some(Todo.Id(id)),
                     Some(todoFormData.category),
                     todoFormData.title,
                     todoFormData.body,
                     todoFormData.state
                   ).toEmbeddedId
                 )
          } yield {
            Redirect(routes.HomeController.index())
          }
        }
      )
  }

  def delete(id: Long) = Action async { implicit request: Request[AnyContent] =>
    for {
      _ <- TodoRepository.remove(Todo.Id(id))
    } yield {
      Redirect(routes.HomeController.index())
    }
  }

}
