/** to do sample project
  */

package controllers

import lib.model.{Category, Todo, TodoForm, TodoFormData}
import model.ViewValueHome
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc._
import service.{CategoryService, ColorService}

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject() (
    val controllerComponents: ControllerComponents
)(implicit ec:                ExecutionContext)
    extends BaseController
    with I18nSupport {

  import lib.persistence.default._

  val form: Form[TodoFormData] = TodoForm.form

  def index() = Action async { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo一覧",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    for {
      todos      <- TodoRepository.list().map(todos => todos.map(_.v))
      categories <- CategoryService.getCategoryMap()
    } yield {
      Ok(views.html.Home(vv, todos, categories, ColorService.getColorMap()))
    }
  }

  /** 登録画面の表示用
    */
  def register() = Action async { implicit request: Request[AnyContent] =>
    val vv = ViewValueHome(
      title  = "登録画面",
      cssSrc = Seq("store.css"),
      jsSrc  = Seq("store.js")
    )
    for {
      categoryOption <- CategoryService.getCategoryOptions()
    } yield {
      Ok(views.html.todo.store(vv, form, categoryOption))
    }
  }

  def store() = Action async { implicit request: Request[AnyContent] =>
    val vv = ViewValueHome(
      title  = "登録画面",
      cssSrc = Seq("store.css"),
      jsSrc  = Seq("store.js")
    )
    form
      .bindFromRequest()
      .fold(
        (formWithErrors: Form[TodoFormData]) => {
          println(formWithErrors)
          Future
            .successful(
              BadRequest(views.html.todo.store(vv, formWithErrors, Seq()))
            )
        },
        (todoFormData: TodoFormData) => {
          for {
            _ <- TodoRepository.add(
                   Todo.build(
                     Category.Id(todoFormData.category),
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
    val vv = ViewValueHome(
      title  = "更新画面",
      cssSrc = Seq("store.css"),
      jsSrc  = Seq("store.js")
    )
    for {
      todoOption      <- TodoRepository.get(Todo.Id(id))
      categoryOptions <- CategoryService.getCategoryOptions()
    } yield {
      val statusOption =
        Todo.Status.values.map(status => (status.code.toString, status.name))
      todoOption match {
        case Some(todo) =>
          Ok(
            views.html.todo.edit(
              vv,
              form.fill(
                TodoFormData(
                  todo.v.title,
                  todo.v.body,
                  todo.v.categoryId.get.toLong,
                  todo.v.state.code
                )
              ),
              categoryOptions,
              statusOption,
              id
            )
          )
        case None       => NotFound(views.html.error.page404(vv))
      }
    }
  }

  def update(id: Long) = Action async { implicit request: Request[AnyContent] =>
    val vv = ViewValueHome(
      title  = "更新画面",
      cssSrc = Seq("store.css"),
      jsSrc  = Seq("store.js")
    )
    form
      .bindFromRequest()
      .fold(
        (formWithErrors: Form[TodoFormData]) => {
          println(formWithErrors)
          Future
            .successful(
              BadRequest(
                views.html.todo.edit(vv, formWithErrors, Seq(), Seq(), id)
              )
            )
        },
        (todoFormData: TodoFormData) => {
          for {
            _ <- TodoRepository.update(
                   Todo(
                     Some(Todo.Id(id)),
                     Some(Category.Id(todoFormData.category)),
                     todoFormData.title,
                     todoFormData.body,
                     Todo.Status(todoFormData.state)
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
