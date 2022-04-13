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

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.{Duration, SECONDS}
import scala.util.{Failure, Success}

@Singleton
class HomeController @Inject()(
  val controllerComponents: ControllerComponents,
)(implicit ec: ExecutionContext) extends BaseController {
  import lib.persistence.default._

  def index() = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Home",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )

    val names: Seq[String] = Await.ready(UserRepository.list(), Duration.Inf).value.get match {
      case Success(result) => result.map(user => user.v.name)
      case Failure(_) => Seq()
    }
    val todos: Seq[Todo] = Await.ready(TodoRepository.list(), Duration.Inf).value.get match {
      case Success(result) => result.map(todo => todo.v)
      case Failure(_)      => Seq()
    }
    val categories: Seq[Category] = Await.ready(CategoryRepository.list(), Duration.Inf).value.get match {
      case Success(result) => result.map(cat => cat.v)
      case Failure(_)      => Seq()
    }

    println(todos)
    println(categories)
    Ok(views.html.Home(vv, names))
  }
}
