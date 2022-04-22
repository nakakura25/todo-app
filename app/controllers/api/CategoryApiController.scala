package controllers.api

import lib.model.Category
import lib.model.json.{CategoryFromJson, CategoryToJson, Color, ColorToJson}
import play.api.i18n.I18nSupport
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import service.{CategoryService, ColorService}

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class CategoryApiController @Inject() (
    val controllerComponents: ControllerComponents,
    val categoryService:      CategoryService
)(implicit ec:                ExecutionContext)
    extends BaseController
    with I18nSupport {

  import lib.persistence.default._

  def get() = Action async { implicit req =>
    for {
      categories <- CategoryRepository.list().map(cats => cats.map(_.v))
    } yield {
      val jsonCategories = Json.toJson(
        categories.map(category => CategoryToJson(category))
      )
      Ok(jsonCategories)
    }
  }

  def store() = Action(parse.json) async { implicit request: Request[JsValue] =>
    val category: Category#WithNoId = request.body
      .validate[CategoryFromJson]
      .fold(
        errors => {
          throw new Exception("json validation errors")
        },
        categoryFromJson => {
          Category.build(
            categoryFromJson.name,
            categoryFromJson.slug,
            categoryFromJson.color
          )
        }
      )
    for {
      _ <- CategoryRepository.add(category)
    } yield NoContent
  }

  def update() = Action(parse.json) async {
    implicit request: Request[JsValue] =>
      val category: Category#EmbeddedId =
        request.body
          .validate[CategoryFromJson]
          .fold(
            errors => {
              throw new Exception("json validation errors")
            },
            categoryFromJson => {
              Category(
                Some(Category.Id(categoryFromJson.id)),
                categoryFromJson.name,
                categoryFromJson.slug,
                categoryFromJson.color
              ).toEmbeddedId
            }
          )
      for {
        _ <- CategoryRepository.update(category)
      } yield NoContent
  }

  def delete(id: Long) = Action async { implicit request: Request[AnyContent] =>
    for {
      _ <- CategoryRepository.removeCategory(Category.Id(id))
    } yield NoContent
  }
}
