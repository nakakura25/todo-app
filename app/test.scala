import lib.model.Todo

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object test {
  def main(args: Array[String]): Unit = {
    val start  = System.currentTimeMillis
//    val result = for {
//      h <- Future { Thread.sleep(1000); "hello " }
//      s <- Future { Thread.sleep(1000); "scala" }
//    } yield h + s
    val result = for {
      (h, s) <- Future { Thread.sleep(1000); "hello " } zip Future {
                  Thread.sleep(1000); "scala"
                }
    } yield h + s
    Await.ready(result, Duration.Inf)
    println(result)
    println("処理時間： " + (System.currentTimeMillis - start) + " ミリ秒")

    val res = Todo.Status.values.map(status => (status.code, status.name)).toMap
    println(res)
  }
}
