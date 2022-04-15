package lib.model.form

import play.api.data.Form
import play.api.data.Forms._

case class TodoFormData(
    title:    String,
    body:     String,
    category: Long,
    state:    Short
)

object TodoForm {
  val form: Form[TodoFormData] = Form(
    mapping(
      "title"    -> nonEmptyText,
      "body"     -> nonEmptyText,
      "category" -> longNumber,
      "state"    -> default(shortNumber, 0.toShort)
    )(TodoFormData.apply)(TodoFormData.unapply)
  )
}
