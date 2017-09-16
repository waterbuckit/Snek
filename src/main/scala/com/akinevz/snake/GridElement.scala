package com.akinevz.snake

sealed trait GridElement {
  def pos: Position
}

case class FruitElement(pos: Position) extends GridElement

case class SnakeElement(pos: Position) extends GridElement {
  def move(other: Position): SnakeElement = this copy other
}