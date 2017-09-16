package com.akinevz.snake

import javax.swing._

object SnakeGame {

  lazy val game: StateManager = new StateManager()
  lazy val frame: GameFrame = new GameFrame(game)
  lazy val window: JFrame = new JFrame() {
    this setPreferredSize new java.awt.Dimension(800, 600)
    this setTitle "Snek"
    this setDefaultCloseOperation JFrame.EXIT_ON_CLOSE
    this setResizable false
    this addWindowListener frame
    this add frame
    this.pack()
  }

  def main(args: Array[String]): Unit = {
    window setVisible true
  }

}
