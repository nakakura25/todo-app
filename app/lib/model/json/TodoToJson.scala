package lib.model.json

import lib.model.{Category, Todo}
import play.api.libs.json.{Json, Reads, Writes}

case class TodoToJson(
    id:           Long,
    categoryId:   Long,
    title:        String,
    body:         String,
    state:        Short,
    stateName:    String,
    color:        Short,
    categoryName: String
)

object TodoToJson {
  implicit val writes: Writes[TodoToJson] = Json.writes[TodoToJson]

  def apply(todo: Todo, category: Category): TodoToJson =
    TodoToJson(
      id           = todo.id.getOrElse(0L),
      categoryId   = todo.categoryId.getOrElse(0L),
      title        = todo.title,
      body         = todo.body,
      state        = todo.state.code,
      stateName    = todo.state.name,
      color        = category.color,
      categoryName = category.name
    )
}

case class TodoFromJson(
    id:         Long,
    categoryId: Long,
    title:      String,
    body:       String,
    state:      Short
)

object TodoFromJson {
  implicit val reads: Reads[TodoFromJson] = Json.reads[TodoFromJson]
}
