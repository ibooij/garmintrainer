package nl.iljabooij.garmintrainer.gui

import java.beans.{PropertyChangeEvent,PropertyChangeListener}
import javax.swing.table.AbstractTableModel
import com.google.inject.Inject
import org.joda.time.{DateTime,DateTimeZone,Duration}
import nl.iljabooij.garmintrainer.model.{Activity,ApplicationState,Length,Speed}
import ApplicationState.Property
import Length.{Unit => LengthUnit}
import Speed.{Unit => SpeedUnit}


class ActivityTableModel @Inject() (private val applicationState:ApplicationState) 
    extends AbstractTableModel with SwingHelper {
  
  private val columns = List("Time", "Distance", "Altitude", "Altitude Gain", "Speed", "Heart Rate")
  applicationState.addPropertyChangeListener(Property.CurrentActivity, activityChanger)
  
  /**
   * Get the value for the cell at [RowwIndex, ColumnIndex] 
   */
  override def getValueAt(rowIndex:Int, columnIndex:Int): Object = {
    val activity = applicationState.getCurrentActivity
    if (activity == null) return null
    
    val trackPoint = activity.getTrackPoints.get(rowIndex)
    
    val column = columns(columnIndex)
    column match {
      case "Time" => 
        val dateTime = new DateTime(0L, DateTimeZone.UTC)
        val fromStart = new Duration(activity.getStartTime, trackPoint.getTime)
        val timeFromStart = dateTime.plus(fromStart)
        timeFromStart.toString("HH:mm:ss")
      case "Distance" =>
        trackPoint.getDistance.convert(LengthUnit.Kilometer)
      case "Altitude" =>
        trackPoint.getAltitude.convert(LengthUnit.Meter)
      case "Altitude Gain" =>
        trackPoint.getAltitudeDelta.convert(LengthUnit.Meter)
      case "Heart Rate" =>
        "" + trackPoint.getHeartRate
      case "Speed" =>
        trackPoint.getSpeed.convert(SpeedUnit.KilometersPerHour)
      case _ => "unknown"
    }
  }
  
  override def getRowCount: Int = {
    val activity = applicationState.getCurrentActivity
    if (activity == null) 0 else activity.getTrackPoints.size
  }
  
  override def getColumnCount:Int = columns.length
  
  override def getColumnName(columnIndex:Int): String = columns(columnIndex)
    
  /**
   * Creates a new PropertyChangeListener that updates the model when needed. 
   */
  private def activityChanger: PropertyChangeListener = {
    new PropertyChangeListener {
      def propertyChange(event: PropertyChangeEvent) {
        onEdt(fireTableDataChanged)
      }
    }
  }
}
