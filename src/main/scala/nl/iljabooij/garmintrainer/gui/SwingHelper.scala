package nl.iljabooij.garmintrainer.gui

import javax.swing.SwingUtilities
import javax.swing.SwingWorker

/**
 * Simple trait that makes some Swing stuff easier.
 */
trait SwingHelper {
  def onEdt(f: => Unit) = {
    SwingUtilities invokeLater new Runnable { def run = f }
  }
  
  def inSwingWorker[T](f: => T, g: T => Unit) {
    object worker extends SwingWorker[T,Any] {
      override def doInBackground: T = {
        f
      }
      override def done {
       g(get)
      }
    }
    worker.execute
  }
  /**
   * Check whether or not we're on the event dispatch thread.
   */
  def isEdt = SwingUtilities.isEventDispatchThread

}
