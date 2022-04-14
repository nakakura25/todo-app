package lib.model

import ixias.model._
import lib.model.Category._

import java.time.LocalDateTime
case class Category(
    id:        Option[Id],
    name:      String,
    slug:      String,
    color:     Short,
    updatedAt: LocalDateTime = NOW,
    createdAt: LocalDateTime = NOW
) extends EntityModel[Id]

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
