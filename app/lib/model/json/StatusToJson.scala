package lib.model.json

import lib.model.Todo
import play.api.libs.json.{Json, Writes}

case class StatusToJson(
    code: Short,
    name: String
)

object StatusToJson {
  implicit val writes: Writes[StatusToJson] = Json.writes[StatusToJson]

  def apply(status: Todo.Status): StatusToJson =
    StatusToJson(
      code = status.code,
      name = status.name
    )
}
