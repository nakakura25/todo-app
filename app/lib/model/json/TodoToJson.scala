package lib.model.json

import lib.model.Todo
import play.api.libs.json.{Json, Reads, Writes}

case class TodoToJson(
    id:         Long,
    categoryId: Long,
    title:      String,
    body:       String,
    state:      Short
)

object TodoToJson {
  implicit val writes: Writes[TodoToJson] = Json.writes[TodoToJson]

  def apply(todo: Todo): TodoToJson =
    TodoToJson(
      id         = todo.id.getOrElse(0L),
      categoryId = todo.categoryId.getOrElse(0L),
      title      = todo.title,
      body       = todo.body,
      state      = todo.state.code
    )
}

case class TodoFromJson(
    id:         Long,
    categoryId: Long,
    title:      String,
    body:       String,
    state:      Short
)

object JsValueCreateTodo {
  implicit val reads: Reads[TodoFromJson] = Json.reads[TodoFromJson]

}
