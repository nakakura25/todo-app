package lib.persistence

import ixias.persistence.SlickRepository
import lib.model.{Category, Todo}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

case class TodoRepository[P <: JdbcProfile]()(implicit val driver: P)
    extends SlickRepository[Todo.Id, Todo, P]
    with db.SlickResourceProvider[P] {

  import api._

  val DELETED_CATEGORY_ID = Category.Id(0L)

  def list(): Future[Seq[EntityEmbeddedId]] =
    RunDBAction(TodoTable, "slave") { _.result }

  def findByCategoryId(id: Category.Id): Future[Seq[EntityEmbeddedId]] =
    RunDBAction(TodoTable, "slave") { _.filter(_.categoryId === id).result }

  def updateTodos(id: Category.Id): Future[Seq[EntityEmbeddedId]] =
    RunDBAction(TodoTable) { slick =>
      val row = slick.filter(_.categoryId === id)
      for {
        old <- row.result
        _   <- row.map(_.categoryId).update(DELETED_CATEGORY_ID).transactionally
      } yield old
    }

  override def get(id: Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoTable, "slave") { _.filter(_.id === id).result.headOption }

  override def add(entity: EntityWithNoId): Future[Id] =
    RunDBAction(TodoTable) { slick =>
      slick returning slick.map(_.id) += entity.v
    }

  override def update(
      entity: EntityEmbeddedId
  ): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoTable) { slick =>
      val row = slick.filter(_.id === entity.id)
      for {
        old <- row.result.headOption
        _   <- old match {
                 case None    => DBIO.successful(0)
                 case Some(_) => row.update(entity.v)
               }
      } yield old
    }

  override def remove(id: Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoTable) { slick =>
      val row = slick.filter(_.id === id)
      for {
        old <- row.result.headOption
        _   <- old match {
                 case None    => DBIO.successful(0)
                 case Some(_) => row.delete
               }
      } yield old
    }
}
