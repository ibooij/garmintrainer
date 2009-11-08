package nl.iljabooij.garmintrainer.gui

import javax.swing._
import javax.swing.border.TitledBorder
import scala.swing._
import com.google.inject.Inject

class ScalaOverviewPanel @Inject() (summaryPanel:SummaryPanel,
	map: MapViewer, chartComponent: ScalaChartComponent) extends GridPanel(2,2) {
  summaryPanel.setBorder(new TitledBorder("Summary"))
  this.peer.add(summaryPanel)
  
  contents += new BorderPanel {
    border = new TitledBorder("Map")
    this.peer.add(map)
  }
  contents += new BorderPanel
  
  contents += new BorderPanel {
    border = new TitledBorder("Chart")
    add(chartComponent, BorderPanel.Position.Center)
  }
}
