package com.akinevz.snake

case class Snake(travelling: Direction, elements: List[SnakeElement] = List()) {
  object Head{
    def unapply(arg: Snake): Option[(Position)] = Some(arg.head.pos)
  }
  def head: SnakeElement = elements.head

  def tip: SnakeElement = elements.last

  def grow: Snake = {
    val next = head copy (head.pos offset travelling)
    this copy (elements = next +: elements)
  }

  def move: Snake = {
    val last = tip move (head.pos offset travelling)
    val updated = last +: elements.init
    this copy (elements = updated)
  }

  def body: List[SnakeElement] = elements drop 4 //dropping all above "neck"

}

