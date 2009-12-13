package nl.iljabooij.garmintrainer

import java.awt.Dimension
import javax.swing._

import scala.swing.MainFrame

import nl.iljabooij.garmintrainer.gui._
import nl.iljabooij.garmintrainer._

import com.google.inject.Guice
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel

/**
 Just a simple main. 
 */
object NewMain {
  val titlePrefix = "Garmin Trainer"
  
  def main(args:Array[String]) {
    println("Hello World!")
    println(Thread.currentThread)
    SwingUtilities.invokeLater(
      
      new Runnable {
        def run = {
          UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel)
          val frame = new MainFrame
          frame.title = titlePrefix
          
          val injector = Guice.createInjector(new GuiceModule)
          
          val scalaGui = injector.getInstance(classOf[ScalaGui])
          scalaGui.init(frame)
          
          frame.size = new Dimension(600, 600)
          frame.pack
          frame.visible = true
        }
      }
    )
  }
}
	  
            
