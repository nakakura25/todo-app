package lib.model

import ixias.model._
import ixias.util.EnumStatus
import lib.model.Todo._

import java.time.LocalDateTime

case class Todo(
    id:         Option[Id],
    categoryId: Option[Category.Id],
    title:      String,
    body:       String,
    state:      Status,
    updatedAt:  LocalDateTime = NOW,
    createdAt:  LocalDateTime = NOW
) extends EntityModel[Id]

object Todo {
  val Id = the[Identity[Id]]
  type Id         = Long @@ Todo
  type WithNoId   = Entity.WithNoId[Id, Todo]
  type EmbeddedId = Entity.EmbeddedId[Id, Todo]

  sealed abstract class Status(val code: Short, val name: String)
      extends EnumStatus
  object Status extends EnumStatus.Of[Status] {
    case object IS_NOT_YET  extends Status(code = 0, name = "TODO")
    case object IS_PROGRESS extends Status(code = 1, name = "進行中")
    case object IS_DONE     extends Status(code = 2, name = "完了")
  }

  def build(
      categoryId: Category.Id,
      title:      String,
      body:       String,
      state:      Status
  ): WithNoId = {
    new Entity.WithNoId(
      new Todo(
        id         = None,
        categoryId = Some(categoryId),
        title      = title,
        body       = body,
        state      = state
      )
    )
  }
}
