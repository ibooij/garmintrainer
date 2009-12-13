package nl.iljabooij.garmintrainer.gui

import java.beans.{PropertyChangeEvent,PropertyChangeListener}
import javax.swing.{JLabel,JPanel,SpringLayout}
import scala.swing.{Label,LayoutContainer,Panel}
import com.google.inject.Inject
import org.joda.time.Duration
import nl.iljabooij.garmintrainer.model.{Activity,ApplicationState,Length,Property}

class ScalaSummaryPanel @Inject() (state:ApplicationState) 
	extends JPanel with SwingHelper {
  setLayout(new SpringLayout())
  
  val dateLabel = new JLabel
  val distanceLabel = new JLabel
  val totalTimeLabel = new JLabel
  val ridingTimeLabel = new JLabel
  val lapsLabel = new JLabel
  val climbedLabel = new JLabel
  
  add(new JLabel("Date")) 
  add(dateLabel)
  add(new JLabel("Distance"))
  add(distanceLabel)
  add(new JLabel("Total Time"))
  add(totalTimeLabel)
  add(new JLabel("Riding Time"))
  add(ridingTimeLabel)
  add(new JLabel("Nr of Laps"))
  add(lapsLabel)
  add(new JLabel("Meters Climbed"))
  add(climbedLabel)
  
  ScalaSpringUtilities.makeGrid(this, 6, 2, 5, 5, 5, 5)
  
  state.addPropertyChangeListener(Property.CurrentActivity, contentChanger)
  
  def contentChanger: PropertyChangeListener = {
    def durationToString(duration:Duration) = {
      val period = duration.toPeriod();
	  String.format("%d:%02d:%02d", Integer.valueOf(period.getHours), 
                 Integer.valueOf(period.getMinutes), 
                 Integer.valueOf(period.getSeconds));
    }
    
    new PropertyChangeListener {
      /**
       * This method will be performed on a background thread by
       * a SwingWorker.
       */
      def doInBackground(activity: Activity) = {
        val startTime = activity.startTime.toString("yyyy-MM-dd")
        val distance = activity.distance.toKilometers.toString
        val nrLaps = activity.laps.size.toString
        val grossDuration = durationToString(activity.grossDuration)
        val netDuration = durationToString(activity.netDuration)
        val altitudeGain = activity.altitudeGain.toMeters.toString
        Map("startTime" -> startTime,
            "distance" -> distance,
            "nrOfLaps" -> nrLaps,
            "grossDuration" -> grossDuration,
            "netDuration" -> netDuration,
            "altitudeGain" -> altitudeGain
        )
      }
      
      /**
       * The SwingWorker will perform this method on the Swing Event Dispatch Thread.
       */
      def done(values: Map[String,String]) {
        assert(isEdt)
        
        dateLabel.setText(values("startTime"))
        distanceLabel.setText(values("distance"))
        lapsLabel.setText(values("nrOfLaps"))
        totalTimeLabel.setText(values("grossDuration"))
        ridingTimeLabel.setText(values("netDuration"))
        climbedLabel.setText(values("altitudeGain"))
      }
      
      def propertyChange(event: PropertyChangeEvent) {
        if (event.getNewValue != null) {
          val activity = event.getNewValue.asInstanceOf[Option[Activity]]
          if (activity.isDefined) inSwingWorker(doInBackground(activity.get), done)
        }
      }
    } 
  }
}