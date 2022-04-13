/**
 *
 * to do sample project
 *
 */

package controllers

import lib.model.User
import lib.persistence.UserRepository

import javax.inject._
import play.api.mvc._
import model.ViewValueHome

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

@Singleton
class HomeController @Inject()(
  val controllerComponents: ControllerComponents,
)(implicit ec: ExecutionContext) extends BaseController {
  import lib.persistence.default.driver

  def index() = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Home",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )

    val names: Seq[String] = Await.ready(UserRepository().list(), Duration.Inf).value.get match {
      case Success(result) => result.map(user => user.v.name)
      case Failure(_) => Seq()
    }
    //    val id = User.Id(1L)
//    val getRes = UserRepository().get(id)
//    println("get===============================")
//    Await.ready(getRes, Duration.Inf)
//    println(getRes.value)
//    println("===============================")

//    val userWithNoId: User#WithNoId = User.apply("Dave", 23, User.Status(100))
//    UserRepository().add(userWithNoId)

    Ok(views.html.Home(vv, names))
  }
}
