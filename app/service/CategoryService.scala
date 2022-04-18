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
      .foldLeft(Map[Category.Id, Category]())((k, v) => k.updated(v.id.get, v))

  def getCategoryOptions(): Future[Seq[(String, String)]] =
    for {
      category <- CategoryRepository.list()
    } yield category
      .map(_.v)
      .map(v => (v.id.get.toString, v.name))

  def getCategoryMapToJson(): Future[Map[Long, String]] =
    for {
      category <- CategoryRepository.list()
    } yield category
      .map(_.v)
      .foldLeft(Map[Long, String]())((k, v) =>
        k.updated(
          v.id match {
            case Some(res) => res.toLong
          },
          v.name
        )
      )

}
