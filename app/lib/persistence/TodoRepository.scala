package lib.persistence

import ixias.persistence.SlickRepository
import lib.model.Todo
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

case class TodoRepository[P <: JdbcProfile]()(implicit val driver: P)
    extends SlickRepository[Todo.Id, Todo, P]
    with db.SlickResourceProvider[P] {

  import api._

  def list(): Future[Seq[EntityEmbeddedId]] =
    RunDBAction(TodoTable, "slave") { _.result }

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
