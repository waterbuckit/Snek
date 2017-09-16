package com.akinevz.snake


import scala.util.Random


sealed trait GameState{
  def update:GameState
}

object GameState{
  def start:GameState = {
    val player = Snake(Direction.random)
    val state = PlayingState(player,(50,50))
    state
  }
}

case class PlayingState private(player: Snake, dim: Dimension) extends GameState{


  lazy val fruit:FruitElement = {
    val taken = player.elements.map(_.pos).toSet
    lazy val gen: Position = dim.generate match {
      case x if taken.contains(x) => gen
      case x => x
    }
    FruitElement(gen)
  }

  def update: GameState = {
    val next: Snake = player.move match {
      case player.Head(pos) if !(dim contains pos) =>
        player copy (elements = (player.head move (dim wrap pos)) :: player.elements.tail)
      case snake if snake.elements.isEmpty => snake.copy(elements = List(SnakeElement(dim.x/2,dim.y/2)))
      case otherwise => otherwise
    }
    val landing: Option[GridElement] = (player.body :+ fruit) find ( _.pos == next.head.pos)
    landing match {
      case Some(FruitElement(pos)) =>
        println(s"Ate fruit at $pos")
        val grown = player.grow
        if(grown.elements.length == dim.totalSpaces) Win
        else PlayingState(grown, dim)
      case Some(SnakeElement(pos)) => println(s"Collision at $pos") and Lost
      case None => this copy(next, dim)
    }
  }
}
sealed trait GameEnd extends GameState{
  override def update: GameState =  this

}
case object Lost extends GameEnd
case object Win extends GameEnd
