package lib.model.form

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, shortNumber}

case class CategoryFormData(
    name:  String,
    slug:  String,
    color: Short
)

object CategoryForm {
  val form: Form[CategoryFormData] = Form(
    mapping(
      "name"  -> nonEmptyText,
      "slug"  -> nonEmptyText.verifying(
        error      = "only alphabet or digit",
        constraint = _.matches("[0-9a-zA-Z]+")
      ),
      "color" -> shortNumber
    )(CategoryFormData.apply)(CategoryFormData.unapply)
  )
}
