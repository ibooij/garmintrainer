package nl.iljabooij.garmintrainer

import java.awt.Dimension
import javax.swing._

import nl.iljabooij.garmintrainer.gui._
import nl.iljabooij.garmintrainer._

import com.google.inject.Guice
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel

/**
 Just a simple main. 
 */
object NewMain {
  val TitlePrefix = "Garmin Trainer"
  
  def main(args:Array[String]) {
    println("Hello World!")
    println(Thread.currentThread)
    SwingUtilities.invokeLater(
      new Runnable {

        def run = {
          UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel)
          val frame = new JFrame(TitlePrefix);
          frame.setLocationByPlatform(true);
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          
          val injector = Guice.createInjector(new GuiceModule)
          
          val scalaGui = injector.getInstance(classOf[ScalaGui])
          scalaGui.init(frame)
          
          frame.setSize(new Dimension(600, 600))
          frame.pack
          frame.setVisible(true)
        }
      }
    )
  }
}
	  
            
