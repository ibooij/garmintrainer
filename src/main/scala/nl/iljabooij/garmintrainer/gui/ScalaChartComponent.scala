package nl.iljabooij.garmintrainer.gui

import java.awt.{Color,Graphics,Graphics2D,GraphicsEnvironment}
import java.beans.{PropertyChangeEvent,PropertyChangeListener}
import scala.swing._
import com.google.inject.Inject

import nl.iljabooij.garmintrainer.gui.chart.AltitudeDiagramPainter
import nl.iljabooij.garmintrainer.model.{Activity,ApplicationState}
import nl.iljabooij.garmintrainer.model.Property

class ScalaChartComponent @Inject() 
	(val applicationState:ApplicationState,
	 val altitudeDiagramPainter: AltitudeDiagramPainter) 
	extends Component with LoggerHelper {
  applicationState.addPropertyChangeListener(Property.CurrentActivity, reactToNewActivity)
  
  def reactToNewActivity: PropertyChangeListener = {
	new PropertyChangeListener {
	  def propertyChange(event: PropertyChangeEvent) = {
	    repaint
	  }
	}
  }
  
  protected override def paintComponent(g: Graphics) = {
    val g2d = g.asInstanceOf[Graphics2D]
    g2d.setBackground(Color.white)
	g2d.clearRect(0, 0, size.width, size.height)
 
	val currentActivity = applicationState.currentActivity
	if (currentActivity.isDefined) {
	  drawChart(g2d, currentActivity.get)
	}
  }
  
  private def drawChart(g2d: Graphics2D, activity: Activity) = {
    val graphImage = GraphicsEnvironment
		.getLocalGraphicsEnvironment().getDefaultScreenDevice()
		.getDefaultConfiguration().createCompatibleImage(
		size.width, size.height)
		
	val imageGraphics = graphImage.createGraphics
	imageGraphics.setBackground(Color.white);
	imageGraphics.clearRect(0, 0, graphImage.getWidth, graphImage.getHeight);
		
    println("calling painter")
	altitudeDiagramPainter.paintDiagram(activity, graphImage)
	g2d.drawImage(graphImage, 0, 0, null)
  }

}
