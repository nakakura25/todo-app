package service

import lib.model.Category

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

object CategoryService {
  import lib.persistence.default._

  private var categories: Future[Map[Category.Id, Category]] =
    for {
      category <- CategoryRepository.list()
    } yield category
      .map(_.v)
      .foldLeft(Map[Category.Id, Category]())((k, v) => k.updated(v.id.get, v))

  def getCategories(): Future[Map[Category.Id, Category]] = categories

  def init(): Unit =
    for {
      category <- CategoryRepository.list()
    } yield category
      .map(_.v)
      .foldLeft(Map[Category.Id, Category]())((k, v) => k.updated(v.id.get, v))
}
