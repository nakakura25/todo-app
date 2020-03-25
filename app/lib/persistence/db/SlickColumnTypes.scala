/**
 *
 * to do sample project
 *
 */

package lib.persistence.db

import slick.jdbc.JdbcProfile

trait SlickColumnTypes[P <: JdbcProfile] {

  implicit val driver: P
  import driver.api._

  // -- [ 独自で定義した型を暗黙的にプリミティブ型に変換 ] -----------------------
}
