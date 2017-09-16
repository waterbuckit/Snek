package com.akinevz.snake

import java.awt.event.{WindowEvent, WindowListener}
import java.awt.{Color, Graphics, Graphics2D}
import javax.swing.JComponent

trait WindowAdapter extends WindowListener {
  override def windowActivated(e: WindowEvent): Unit = ()

  override def windowClosed(e: WindowEvent): Unit = ()

  override def windowClosing(e: WindowEvent): Unit = ()

  override def windowOpened(e: WindowEvent): Unit = ()

  override def windowIconified(e: WindowEvent): Unit = ()

  override def windowDeiconified(e: WindowEvent): Unit = ()

  override def windowDeactivated(e: WindowEvent): Unit = ()
}

class GameFrame(val game: StateManager) extends JComponent with WindowAdapter {

  this setBackground Color.lightGray
  this setForeground Color.gray

  def fruitColour = Color.pink

  def blockColour = Color.darkGray

  def snakeColour = Color.green

  Keybindings register this


  override def paint(g: Graphics): Unit = {
    val g2 = g.asInstanceOf[Graphics2D]
    game.state match {
      case s@PlayingState(snake,size) =>
        val windowSize = this.getSize()
        val scale = math.min(windowSize.width/size.x,windowSize.height/size.y)
        val realSize = (size.x*scale , size.y*scale)
        val offset = (windowSize.width - realSize.x, windowSize.height - realSize.y)
        g2.translate(offset.x/2,offset.y/2)
        g2.scale(scale,scale)
        g2.setColor(blockColour) and g2.fillRect(0,0,size.x,size.y)
        g2.setColor(snakeColour)
        snake.elements foreach { case SnakeElement(pos) => g2.drawRect(pos.x,pos.y,1,1)}
        val (fx,fy) = s.fruit.pos
        g2.setColor(fruitColour) and g2.drawRect(fx,fy,1,1)
      case Win | Lost => g2.drawString("Game end!",
        this.getSize().getWidth.toInt/2 ,
        this.getSize().getHeight.toInt/2)
    }

    //acquire drawn image
    //scale up, draw to self
    //draw score?
  }

  override def windowActivated(e: WindowEvent): Unit =
    super.windowActivated(e) and game.resume()

  override def windowDeactivated(e: WindowEvent): Unit =
    super.windowDeactivated(e) and game.pause()

  override def windowClosing(e: WindowEvent): Unit =
    super.windowClosing(e) and game.stop()

  override def windowOpened(e: WindowEvent): Unit =
    super.windowOpened(e) and game.start()
}
