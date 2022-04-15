package controllers

import lib.model.Category
import lib.model.form.{CategoryForm, CategoryFormData}
import model.ViewValueHome
import play.api.data.Form
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

  val form: Form[CategoryFormData] = CategoryForm.form
  val vvList                       = ViewValueHome(
    title  = "Todo一覧",
    cssSrc = Seq("main.css"),
    jsSrc  = Seq("main.js")
  )
  val vvStore                      = ViewValueHome(
    title  = "登録画面",
    cssSrc = Seq("store.css"),
    jsSrc  = Seq("store.js")
  )
  val vvUpdate                     = ViewValueHome(
    title  = "更新画面",
    cssSrc = Seq("store.css"),
    jsSrc  = Seq("store.js")
  )
  val vv404                        = ViewValueHome(
    title  = "404 Not Found",
    cssSrc = Seq("store.css"),
    jsSrc  = Seq("store.js")
  )

  def index() = Action async { implicit req =>
    for {
      categories <-
        CategoryRepository.list().map(categorise => categorise.map(_.v))
    } yield {
      Ok(views.html.category.list(vvList, categories))
    }
  }

  def register() = Action async { implicit request: Request[AnyContent] =>
    for {
      _ <- CategoryRepository.list()
    } yield {
      Ok(views.html.category.store(vvStore, form))
    }
  }

  def store() = Action async { implicit request: Request[AnyContent] =>
    ???
  }

  def edit(id: Long) = Action async { implicit request: Request[AnyContent] =>
    for {
      categryOption <- CategoryRepository.get(Category.Id(id))
    } yield {
      categryOption match {
        case Some(category) =>
          Ok(
            views.html.category.edit(
              vvUpdate,
              form.fill(
                CategoryFormData(
                  category.v.name,
                  category.v.slug,
                  category.v.color
                )
              )
            )
          )
        case None           => NotFound(views.html.error.page404(vv404))
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
