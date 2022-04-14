package service

import lib.model.Category

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

object CategoryService {
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
}
