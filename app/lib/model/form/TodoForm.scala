package lib.model.form

import lib.model.Todo.Status
import lib.model.Category
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formatter

case class TodoFormData(
    title:    String,
    body:     String,
    category: Category.Id,
    state:    Status
)

object TodoForm {
  import lib.model.form.MappingFormatter._

  val form: Form[TodoFormData] = Form(
    mapping(
      "title"    -> nonEmptyText,
      "body"     -> nonEmptyText,
      "category" -> of[Category.Id],
      "state"    -> default(of[Status], Status.IS_NOT_YET)
    )(TodoFormData.apply)(TodoFormData.unapply)
  )
}
