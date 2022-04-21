package lib.model.json

import play.api.libs.json.{Json, Writes}

case class ColorToJson(
    id:    Int,
    color: String
)

case class Color(
    id:    Int,
    color: String
)

object ColorToJson {
  implicit val writes: Writes[ColorToJson] = Json.writes[ColorToJson]

  def apply(color: Color): ColorToJson =
    ColorToJson(
      id    = color.id,
      color = color.color
    )
}
