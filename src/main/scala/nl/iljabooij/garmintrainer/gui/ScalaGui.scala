package nl.iljabooij.garmintrainer.gui

import java.awt.BorderLayout
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import javax.swing._

import scala.swing._

import com.google.inject.Inject 

import nl.iljabooij.garmintrainer.model.{Activity,ApplicationState}
import nl.iljabooij.garmintrainer.model.ApplicationState.Property

class ScalaGui @Inject() (val overviewPanel:OverviewPanel,
		val mapViewer: MapViewer,
		val chartPanel: ChartPanel,
        val samplesTablePanel: SampleTablePanel,
		val applicationState: ApplicationState,
		val fileTransferHandler: FileTransferHandler) extends SwingHelper with LoggerHelper {
    
    /**
     * Initialize the Gui.
     * @param frame JFrame to use.
     */
	def init(frame:MainFrame) = {
	  debug("initializing frame")
   
	  applicationState.addPropertyChangeListener(Property.CurrentActivity, titleChanger(frame))
      val tabbedPane = new TabbedPane
	  applicationState.addPropertyChangeListener(Property.ErrorMessage, errorMessageShower(tabbedPane))
		
	  panels.foreach(tabbedPane.peer.add(_))
  
	  frame.contents = tabbedPane
	  frame.peer.setTransferHandler(fileTransferHandler)
	} 
 
    def panels : List[JPanel] = {
      List(overviewPanel, mapViewer, chartPanel, samplesTablePanel)
    }
    
    def titleChanger(frame:MainFrame): PropertyChangeListener = {
      new PropertyChangeListener {
        def propertyChange(event: PropertyChangeEvent) {
          var id:String = ""
          if (event.getNewValue != null) {
            val activity:Activity = event.getNewValue.asInstanceOf[Activity]
            id = activity.getStartTime.toString("yyyy-MM-dd")
            onEdt(frame.title = id)
          }  
        }
      }
    }
    
    def errorMessageShower(component:Component): PropertyChangeListener = {
      new PropertyChangeListener {
        def propertyChange(event: PropertyChangeEvent) {
          if (event.getNewValue != null) {
            val message = event.getNewValue.asInstanceOf[String]
            onEdt(Dialog.showMessage(component, message))
          }
        }
      }
    }
}
