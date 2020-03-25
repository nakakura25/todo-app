/**
 *
 * to do sample project
 *
 */

package lib.persistence.db

import slick.jdbc.JdbcProfile

trait SlickResourceProvider[P <: JdbcProfile] {

  implicit val driver: P

  // --[ テーブル定義 ] --------------------------------------
  lazy val AllTables = Seq(
  )
}
