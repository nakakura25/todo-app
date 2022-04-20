package service

import com.typesafe.config.ConfigFactory
import lib.model.json.ColorToJson

import scala.collection.JavaConverters.iterableAsScalaIterableConverter

object ColorService {
  def getColorMap(): Map[Int, String] =
    ConfigFactory
      .load("./todo/color.conf")
      .getStringList("todo.color.colors")
      .asScala
      .map(t => t)
      .zipWithIndex
      .map(t => (t._2 + 1, t._1))
      .toMap

  def getColorOption(): Seq[(String, String)] =
    getColorMap().map(k => (k._1.toString, k._2)).toSeq

}
