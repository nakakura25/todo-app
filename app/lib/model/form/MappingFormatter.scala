package lib.model.form

import lib.model.{Category, Todo}
import lib.model.Category.Id
import play.api.data._
import play.api.data.format.{Formats, Formatter}

object MappingFormatter {
  implicit val categoryIdFormatter = new Formatter[Category.Id] {
    override def bind(
        key:  String,
        data: Map[String, String]
    ): Either[Seq[FormError], Category.Id] =
      Formats.longFormat.bind(key, data).right.map(t => Category.Id(t))

    override def unbind(key: String, value: Category.Id): Map[String, String] =
      Map(key -> value.toString)
  }

  implicit val statusFormatter = new Formatter[Todo.Status] {
    override def bind(
        key:  String,
        data: Map[String, String]
    ): Either[Seq[FormError], Todo.Status] =
      Formats.shortFormat.bind(key, data).right.map(t => Todo.Status(t))
    override def unbind(key: String, value: Todo.Status): Map[String, String] =
      Map(key -> value.code.toString)
  }
}
