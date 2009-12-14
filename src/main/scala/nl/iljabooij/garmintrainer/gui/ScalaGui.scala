package nl.iljabooij.garmintrainer.gui

import java.awt.BorderLayout
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import javax.swing._

import scala.swing._

import com.google.inject.Inject 

import nl.iljabooij.garmintrainer.model.{Activity,ApplicationState}

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
   
      applicationState.addActivityChangeListener(titleChanger(frame)_)
	  applicationState.addErrorChangeListener(
	    errorMessageUpdated(tabbedPane)_)
	} 
 
    private def panels : List[JPanel] = {
      List(mapViewer)
    }
    
    private def titleChanger(frame:MainFrame)(activityOption: Option[Activity]) {
      var id:String = ""
      if (activityOption.isDefined) 
        id = activityOption.get.startTime.toString("yyyy-MM-dd")
      onEdt(frame.title = id)    
    }
    
    private def errorMessageUpdated(component:Component)(errorMessage:Option[String]) {
      if (errorMessage.isDefined) 
        onEdt(Dialog.showMessage(component,errorMessage.get))
    }
}
