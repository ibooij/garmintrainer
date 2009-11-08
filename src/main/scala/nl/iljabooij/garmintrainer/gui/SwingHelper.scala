package nl.iljabooij.garmintrainer.gui

import javax.swing.SwingUtilities

/**
 * Simple trait that makes some Swing stuff easier.
 */
trait SwingHelper {
  def onEdt(f: => Unit) = {
    SwingUtilities invokeLater new Runnable { def run = f }
  }
}
