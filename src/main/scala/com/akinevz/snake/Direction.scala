package com.akinevz.snake

import scala.util.Random

sealed trait Direction {
  def x:Int
  def y:Int

}

sealed class AbstractDirection(x1:Int,y1:Int) extends Direction {
  override def x: Int = x1

  override def y: Int = y1
}

case object North extends AbstractDirection(0,-1)
case object South extends AbstractDirection(0, 1)
case object West  extends AbstractDirection(-1,0)
case object East  extends AbstractDirection(1, 0)

object Direction{
  def random:Direction = (Random shuffle List(North, East, South, West)).head
}