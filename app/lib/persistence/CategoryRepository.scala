package lib.persistence

import ixias.persistence.SlickRepository
import lib.model.Category
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

case class CategoryRepository[P <: JdbcProfile]()(implicit val driver: P)
    extends SlickRepository[Category.Id, Category, P]
    with db.SlickResourceProvider[P] {

  import api._

  def list(): Future[Seq[EntityEmbeddedId]] =
    RunDBAction(CategoryTable, "slave") { _.result }

  override def get(id: Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(CategoryTable, "slave") {
      _.filter(_.id === id).result.headOption
    }

  override def add(entity: EntityWithNoId): Future[Id] =
    RunDBAction(CategoryTable) { slick =>
      slick returning slick.map(_.id) += entity.v
    }

  override def update(
      entity: EntityEmbeddedId
  ): Future[Option[EntityEmbeddedId]] =
    RunDBAction(CategoryTable) { slick =>
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
    RunDBAction(CategoryTable) { slick =>
      val row = slick.filter(_.id === id)
      for {
        old <- row.result.headOption
        _   <- old match {
                 case None    => DBIO.successful(0)
                 case Some(_) => row.delete
               }
      } yield old
    }

  val DELETED_CATEGORY_ID = Category.Id(0L)
  def remove_(id: Id) = {
    RunDBAction(CategoryTable) { slick =>
      (for {
        _ <- slick.filter(_.id === id).delete
        _ <- TodoTable.query
               .filter(_.categoryId === id)
               .map(_.categoryId)
               .update(DELETED_CATEGORY_ID)
      } yield ()).transactionally
    }
  }
}
