package nl.iljabooij.garmintrainer.gui

import java.beans.{PropertyChangeEvent,PropertyChangeListener}
import scala.swing.{BorderPanel,ScrollPane,Table}
import com.google.inject.Inject

/**
 * Panel holding a Table with all samples.
 */
class ScalaTablePanel @Inject() 
	(val tableModel: ActivityTableModel, val table: Table) extends BorderPanel with SwingHelper {
  table.model = tableModel
  private val scrollPane = new ScrollPane(table)
  add(scrollPane, BorderPanel.Position.Center)
}
