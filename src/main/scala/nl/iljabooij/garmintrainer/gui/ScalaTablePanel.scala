package nl.iljabooij.garmintrainer.gui

import java.beans.{PropertyChangeEvent,PropertyChangeListener}
import scala.swing.{BorderPanel,ScrollPane,Table}
import com.google.inject.Inject

import nl.iljabooij.garmintrainer.model.{Activity,ApplicationState}
import nl.iljabooij.garmintrainer.model.ApplicationState.Property

/**
 * Panel holding a Table with all samples.
 */
class ScalaTablePanel @Inject() (private val applicationState: ApplicationState,
	private val tableModel: ActivityTableModel, private val table: Table) extends BorderPanel with SwingHelper {
  table.model = tableModel
  private val scrollPane = new ScrollPane(table)
  add(scrollPane, BorderPanel.Position.Center)
}
