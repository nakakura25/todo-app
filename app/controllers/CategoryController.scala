package controllers

import lib.model.{Category, Todo}
import model.ViewValueHome
import play.api.i18n.I18nSupport
import play.api.mvc.{AnyContent, BaseController, ControllerComponents, Request}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class CategoryController @Inject() (
    val controllerComponents: ControllerComponents
)(implicit ec:                ExecutionContext)
    extends BaseController
    with I18nSupport {

  import lib.persistence.default._

  def index() = Action async { implicit req =>
    val vv = ViewValueHome(
      title  = "カテゴリー一覧",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    for {
      categories <-
        CategoryRepository.list().map(categorise => categorise.map(_.v))
    } yield {
      Ok(views.html.category.list(vv, categories))
    }
  }

  def register() = Action async { implicit request: Request[AnyContent] =>
    ???
  }

  def store() = Action async { implicit request: Request[AnyContent] =>
    ???
  }

  def edit(id: Long) = Action async { implicit request: Request[AnyContent] =>
    ???
  }

  def update(id: Long) =
    Action async { implicit request: Request[AnyContent] =>
      ???
    }

  def delete(id: Long) = Action async { implicit request: Request[AnyContent] =>
    for {
      _ <- CategoryRepository.remove(Category.Id(id))
    } yield {
      Redirect(routes.CategoryController.index())
    }
  }

}
