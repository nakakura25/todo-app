package service

import lib.model.Category

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object CategoryService {
  import lib.persistence.default._
  private var categories: Map[Category.Id, Category] =
    Await.result(CategoryRepository.list(), Duration.Inf)
      .map(categoryEmb => categoryEmb.v)
      .foldLeft(Map[Category.Id, Category]())((k, v) => k.updated(v.id.get, v))

  def getCategories(): Map[Category.Id, Category] = categories

  def init(): Unit =
    categories= Await.result(CategoryRepository.list(), Duration.Inf)
      .map(categoryEmb => categoryEmb.v)
      .foldLeft(Map[Category.Id, Category]())((k, v) => k.updated(v.id.get, v))
}
