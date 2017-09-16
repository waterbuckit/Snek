package com.akinevz.snake

import javax.swing.JComponent

object Keybindings {
  type Keycode = Int
  type GameEvent = (GameState) => GameState
  def keybinds:Map[Keycode,GameEvent] = ???

  def register(component: JComponent):Unit = {
    val inputMap = component.getInputMap
    val actionMap = component.getActionMap

  }
}
