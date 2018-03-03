package com.akinevz.snake


import scala.util.Random


sealed trait GameState {
  def update: GameState
}

object GameState {
  def start: GameState = {
    val player = new Snake(Direction.random, (10, 10))
    val state = PlayingState(player, (10,10))
    state
  }

}

case class PlayingState private(player: Snake, dim: Dimension, fruit: Option[FruitElement]=None) extends GameState {

  def wrapHead(snake:Snake) : Snake = snake match {
    case player.Head(pos) if !(dim contains pos) =>
      player copy (elements = (player.tip move (dim wrap pos)) :: player.elements.init)
    case s if s.elements.isEmpty => snake.copy(elements = List(SnakeElement(dim.x / 2, dim.y / 2)))
    case otherwise => otherwise
  }

  def spawnFruit: FruitElement = {
    val taken = player.elements.map(_.pos).toSet
    lazy val gen: Position = dim.generate match {
      case x if taken.contains(x) => gen
      case x => x
    }
    FruitElement(gen)
  }

  def update: GameState = if (fruit.isEmpty) PlayingState(player, dim, Some(spawnFruit)).update else {
    val next: Snake = wrapHead(player.move)
    val landing: Option[GridElement] = (player.body ++ fruit) find (_.pos == next.head.pos)
    landing match {
      case Some(FruitElement(pos)) =>
        val grown = wrapHead(player.grow)
        if (grown.elements.length == dim.totalSpaces) Win
        else PlayingState(grown, dim)
      case Some(SnakeElement(pos)) => println(s"Collision at $pos") and Lost
      case None => this copy(next, dim)
    }
  }
}

sealed trait GameEnd extends GameState {
  override def update: GameState = this

}

case object Lost extends GameEnd

case object Win extends GameEnd
