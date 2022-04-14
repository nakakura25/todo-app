package service

import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters.iterableAsScalaIterableConverter

object ColorService {
  def getColorMap(): Map[Int, String] =
    ConfigFactory
      .load("./todo/color.conf")
      .getStringList("todo.color.colors")
      .asScala
      .map(t => t)
      .zipWithIndex
      .map(t => t._2 -> t._1)
      .toMap
}
