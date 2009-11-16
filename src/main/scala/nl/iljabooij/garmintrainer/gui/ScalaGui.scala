package nl.iljabooij.garmintrainer.gui

import java.awt.BorderLayout
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import javax.swing._

import scala.swing._

import com.google.inject.Inject 

import nl.iljabooij.garmintrainer.model.{Activity,ApplicationState}
import nl.iljabooij.garmintrainer.model.ApplicationState.Property

class ScalaGui @Inject() (val overviewPanel:ScalaOverviewPanel,
		val mapViewer: ScalaMapViewer,
		val chartComponent: ScalaChartComponent,
        val tablePanel: ScalaTablePanel,
		val applicationState: ApplicationState,
		val fileTransferHandler: ScalaFileTransferHandler) extends SwingHelper with LoggerHelper {
    
    /**
     * Initialize the Gui.
     * @param frame JFrame to use.
     */
	def init(frame:MainFrame) = {
	  debug("initializing frame")
   
	  val tabbedPane = new TabbedPane	
	  tabbedPane.pages += new TabbedPane.Page("Overview", overviewPanel)
      val chartPanel = new BorderPanel {
        add(chartComponent, BorderPanel.Position.Center)
      }
	  tabbedPane.pages += new TabbedPane.Page("Chart", chartPanel)
	  panels.foreach(tabbedPane.peer.add(_))
	  tabbedPane.pages += new TabbedPane.Page("Samples", tablePanel)
  
	  frame.contents = tabbedPane
	  frame.peer.setTransferHandler(fileTransferHandler)
   
      applicationState.addPropertyChangeListener(Property.CurrentActivity, titleChanger(frame))
	  applicationState.addPropertyChangeListener(Property.ErrorMessage, errorMessageShower(tabbedPane))
	} 
 
    def panels : List[JPanel] = {
      List(mapViewer)
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
