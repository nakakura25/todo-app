/**
 *
 * to do sample project
 *
 */

package controllers

import lib.model.{Category, Todo, User}

import javax.inject._
import play.api.mvc._
import model.ViewValueHome
import play.api.i18n.I18nSupport
import service.CategoryService
import service.CategoryService.categories

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.{DAYS, Duration, MINUTES, SECONDS}
import scala.util.{Failure, Success}

@Singleton
class HomeController @Inject()(
  val controllerComponents: ControllerComponents,
)(implicit ec: ExecutionContext) extends BaseController with I18nSupport {

  import lib.persistence.default._

  def index() = Action async { implicit req =>
    val vv = ViewValueHome(
      title = "Todo一覧",
      cssSrc = Seq("main.css"),
      jsSrc = Seq("main.js")
    )
    for {
      todos <- TodoRepository.list().map(todos => todos.map(_.v))
    } yield {
      Ok(views.html.Home(vv, todos, CategoryService.getCategories()))
    }
  }

  /**
   * 登録画面の表示用
   */
  def register() = Action { implicit request: Request[AnyContent] =>
    val vv = ViewValueHome(
      title = "登録画面",
      cssSrc = Seq("store.css"),
      jsSrc = Seq("store.js")
    )
    // insert test
    val todo:Todo#WithNoId = Todo.build(Category.Id(1L), "test title", "test body", Todo.Status.IS_NOT_YET)
    val id = TodoRepository.add(todo)
    id.onComplete {
      case Success(value) => println(s"register test: ${value}")
      case Failure(exception) => throw exception
    }
    Redirect(routes.HomeController.index())
    //    Ok(views.html.todo.store(vv))
   }

  def edit(id: Long) = Action async { implicit request: Request[AnyContent] =>
    val vv = ViewValueHome(
      title = "更新画面",
      cssSrc = Seq("store.css"),
      jsSrc = Seq("store.js")
    )

    // update test
    val todo: Future[Option[Todo#EmbeddedId]] = TodoRepository.get(Todo.Id(id))
    val testTodo: Todo#EmbeddedId = Await.ready(todo, Duration.Inf).value.get match {
      case Success(value) => value.get.map(_.copy(title = "update test"))
      case Failure(exception) => throw exception
    }
    for {
      _ <- TodoRepository.update(testTodo)
    } yield {
      Redirect(routes.HomeController.index())
    }
    //    Ok(views.html.edit.store(vv))
  }

  def delete(id: Long) = Action async { implicit request: Request[AnyContent] =>
    val vv = ViewValueHome(
      title = "登録画面",
      cssSrc = Seq("store.css"),
      jsSrc = Seq("store.js")
    )
    for {
      _ <- TodoRepository.remove(Todo.Id(id))
    } yield {
      Redirect(routes.HomeController.index())
    }
  }

}