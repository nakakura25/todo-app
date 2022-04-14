package controllers

import lib.model.{
  Category,
  CategoryForm,
  CategoryFormData,
  Todo,
  TodoForm,
  TodoFormData
}
import model.ViewValueHome
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{AnyContent, BaseController, ControllerComponents, Request}
import service.CategoryService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class CategoryController @Inject() (
    val controllerComponents: ControllerComponents
)(implicit ec:                ExecutionContext)
    extends BaseController
    with I18nSupport {

  import lib.persistence.default._

  val form: Form[CategoryFormData] = CategoryForm.form

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
    val vv = ViewValueHome(
      title  = "登録画面",
      cssSrc = Seq("store.css"),
      jsSrc  = Seq("store.js")
    )
    for {
      _ <- CategoryRepository.list()
    } yield {
      Ok(views.html.category.store(vv, form))
    }
  }

  def store() = Action async { implicit request: Request[AnyContent] =>
    ???
  }

  def edit(id: Long) = Action async { implicit request: Request[AnyContent] =>
    val vv = ViewValueHome(
      title  = "更新画面",
      cssSrc = Seq("store.css"),
      jsSrc  = Seq("store.js")
    )
    for {
      categryOption <- CategoryRepository.get(Category.Id(id))
    } yield {
      categryOption match {
        case Some(category) =>
          Ok(
            views.html.category.edit(
              vv,
              form.fill(
                CategoryFormData(
                  category.v.name,
                  category.v.slug,
                  category.v.color
                )
              )
            )
          )
        case None           => NotFound(views.html.error.page404(vv))
      }
    }
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
