package lib.model

import ixias.model._
import lib.model.Category._
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, shortNumber}

import java.time.LocalDateTime

case class Category(
    id:        Option[Id],
    name:      String,
    slug:      String,
    color:     Short,
    updatedAt: LocalDateTime = NOW,
    createdAt: LocalDateTime = NOW
) extends EntityModel[Id]

case class CategoryFormData(
    name:  String,
    slug:  String,
    color: Short
)

object Category {
  val Id = the[Identity[Id]]
  type Id         = Long @@ Category
  type WithNoId   = Entity.WithNoId[Id, Category]
  type EmbeddedId = Entity.EmbeddedId[Id, Category]

  def build(name: String, slug: String, color: Short): WithNoId = {
    new Entity.WithNoId(
      new Category(
        id    = None,
        name  = name,
        slug  = slug,
        color = color
      )
    )
  }
}

object CategoryForm {
  val form: Form[CategoryFormData] = Form(
    mapping(
      "name"  -> nonEmptyText,
      "slug"  -> nonEmptyText,
      "color" -> shortNumber
    )(CategoryFormData.apply)(CategoryFormData.unapply)
  )
}
