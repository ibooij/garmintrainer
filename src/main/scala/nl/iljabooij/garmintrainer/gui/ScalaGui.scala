package nl.iljabooij.garmintrainer.gui

import java.awt.BorderLayout
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import javax.swing._

import com.google.inject.Inject 

import nl.iljabooij.garmintrainer.model.{Activity,ApplicationState}
import nl.iljabooij.garmintrainer.model.ApplicationState.Property

class ScalaGui @Inject() (val overviewPanel:OverviewPanel,
		val mapViewer: MapViewer,
		val chartPanel: ChartPanel,
        val samplesTablePanel: SampleTablePanel,
		val applicationState: ApplicationState,
		val fileTransferHandler: FileTransferHandler) {
  
    var theFrame:JFrame = null
    
    /**
     * Initialize the Gui.
     * @param frame JFrame to use.
     */
	def init(frame:JFrame) = {
	    theFrame = frame
     
	    applicationState.addPropertyChangeListener(Property.CurrentActivity, titleChanger)
		val pane = frame.getContentPane
		pane.setLayout(new BorderLayout)
		val tabbedPane = new JTabbedPane
		
		panels.foreach(tabbedPane.add(_))
  
		pane.add(tabbedPane, BorderLayout.CENTER)
  
		frame.setTransferHandler(fileTransferHandler)
	} 
 
    def panels : List[JPanel] = {
      List(overviewPanel, mapViewer, chartPanel, samplesTablePanel)
    }
    
    def titleChanger: PropertyChangeListener = {
      new PropertyChangeListener {
        def propertyChange(event: PropertyChangeEvent) {
          var id:String = ""
          if (event.getNewValue != null) {
            val activity:Activity = event.getNewValue.asInstanceOf[Activity]
            id = activity.getStartTime.toString("yyyy-MM-dd")
            theFrame.setTitle(id)
          }  
        }
      }
    }
}
