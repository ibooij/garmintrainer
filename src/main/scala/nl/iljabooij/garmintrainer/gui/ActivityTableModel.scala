package nl.iljabooij.garmintrainer.gui

import javax.swing.table.AbstractTableModel
import com.google.inject.Inject
import org.joda.time.{DateTime,DateTimeZone,Duration}
import nl.iljabooij.garmintrainer.model.{Activity,ApplicationState,Length,Speed,TrackPoint}

class ActivityTableModel @Inject() (private val applicationState:ApplicationState) 
    extends AbstractTableModel with SwingHelper {
  private val columns = List("Time", "Distance", "Altitude", 
                             "Altitude Gain", "Speed", "Heart Rate")
  applicationState.addActivityChangeListener(updateTable)
  
  /**
   * Get the value for the cell at [RowwIndex, ColumnIndex] 
   */
  override def getValueAt(rowIndex:Int, columnIndex:Int): Object = {
    applicationState.currentActivity match {
      case None => null
      case Some(activity) => {
        val trackPoint = activity.trackPoints(rowIndex)
        val column = columns(columnIndex)
        column match {
          case "Time" => 
            val dateTime = new DateTime(0L, DateTimeZone.UTC)
            val fromStart = new Duration(activity.startTime, trackPoint.time)
            val timeFromStart = dateTime.plus(fromStart)
            timeFromStart.toString("HH:mm:ss")
          case "Distance" => trackPoint.distance.toKilometers
          case "Altitude" => trackPoint.altitude.toMeters
          case "Altitude Gain" => trackPoint.altitudeDelta.toMeters
          case "Heart Rate" => "" + trackPoint.heartRate
          case "Speed" => trackPoint.speed.toKilometersPerHour
          case _ => "unknown"
        }
      }
    }
  }
  
  override def getRowCount: Int = {
    applicationState.currentActivity match {
      case None => 0
      case Some(activity) => activity.trackPoints.size
    }
  }
  
  override def getColumnCount:Int = columns.length
  
  override def getColumnName(columnIndex:Int): String = columns(columnIndex)
    
  /**
   * Creates a new PropertyChangeListener that updates the model when needed. 
   */
  private def updateTable(activityOption: Option[Activity]) {
    onEdt(fireTableDataChanged)
  }
}
