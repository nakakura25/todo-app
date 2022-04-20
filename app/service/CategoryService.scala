package service

import lib.model.Category

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait CategoryServiceInterface {
  def getCategoryMap(): Future[Map[Category.Id, Category]]
  def getCategoryOptions(): Future[Seq[(String, String)]]
}

class CategoryService extends CategoryServiceInterface {
  import lib.persistence.default._

  def getCategoryMap(): Future[Map[Category.Id, Category]] =
    for {
      category <- CategoryRepository.list()
    } yield category
      .map(_.v)
      .foldLeft(
        Map[Category.Id, Category](
          Category.Id(0L) -> Category(
            id    = Some(Category.Id(0L)),
            name  = "None",
            slug  = "None",
            color = 0
          )
        )
      )((k, v) => k.updated(v.id.get, v))

  def getCategoryOptions(): Future[Seq[(String, String)]] =
    for {
      category <- CategoryRepository.list()
    } yield category
      .map(_.v)
      .map(v => (v.id.get.toString, v.name))
}
