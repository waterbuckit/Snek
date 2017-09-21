package com.akinevz.snake

import java.util.Timer
import javax.swing.JComponent



sealed trait State

case object Init extends State

case object Running extends State

case object Paused extends State

case object Stopped extends State

