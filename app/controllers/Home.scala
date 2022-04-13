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
import util.TodoUtil
import util.TodoUtil.categories

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
      Ok(views.html.Home(vv, todos, TodoUtil.getCategories()))
    }
  }
}