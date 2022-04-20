package lib.model.json

import lib.model.Category
import play.api.libs.json.{Json, Reads, Writes}

case class CategoryToJson(
    id:    Long,
    name:  String,
    slug:  String,
    color: Short
)

object CategoryToJson {
  implicit val writes: Writes[CategoryToJson] = Json.writes[CategoryToJson]

  def apply(category: Category): CategoryToJson =
    CategoryToJson(
      id    = category.id.getOrElse(0L),
      name  = category.name,
      slug  = category.slug,
      color = category.color
    )
}

case class CategoryFromJson(
    id:    Long,
    name:  String,
    slug:  String,
    color: Short
)

object CategoryFromJson {
  implicit val reads: Reads[CategoryFromJson] = Json.reads[CategoryFromJson]
}
