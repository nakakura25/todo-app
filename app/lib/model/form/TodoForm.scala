package lib.model.form

import lib.model.Category
import lib.model.Todo.Status
import play.api.data.Form
import play.api.data.Forms._

case class TodoFormData(
    title:    String,
    body:     String,
    category: Category.Id,
    state:    Status
)

object TodoForm {

  val form: Form[TodoFormData] = Form(
    mapping(
      "title"    -> nonEmptyText,
      "body"     -> nonEmptyText,
      "category" -> longNumber.transform[Category.Id](Category.Id(_), _.toLong),
      "state"    -> default(
        shortNumber.transform[Status](Status(_), _.code),
        Status.IS_NOT_YET
      )
    )(TodoFormData.apply)(TodoFormData.unapply)
  )
}
