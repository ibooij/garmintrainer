package nl.iljabooij.garmintrainer.gui

import java.beans.{PropertyChangeEvent,PropertyChangeListener}
import javax.swing.{JLabel,JPanel,SpringLayout}
import scala.swing.{Label,LayoutContainer,Panel}
import com.google.inject.Inject
import org.joda.time.Duration
import nl.iljabooij.garmintrainer.model.{Activity,ApplicationState,Length}
import Length.{Unit => LengthUnit}
import ApplicationState.Property

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
  
  SpringUtilities.makeGrid(this, 6, 2, 5, 5, 5, 5)
  
  state.addPropertyChangeListener(Property.CurrentActivity, contentChanger)
  
  def contentChanger: PropertyChangeListener = {
    def durationToString(duration:Duration) = {
      val period = duration.toPeriod();
	  String.format("%d:%02d:%02d", Integer.valueOf(period.getHours), 
                 Integer.valueOf(period.getMinutes), 
                 Integer.valueOf(period.getSeconds));
    }
    
    new PropertyChangeListener {
      def propertyChange(event: PropertyChangeEvent) {
        if (event.getNewValue != null) {
          val activity = event.getNewValue.asInstanceOf[Activity]
          dateLabel.setText(activity.getStartTime.toString("yyyy-MM-dd"))
          distanceLabel.setText(activity.getDistance.convert(LengthUnit.Kilometer).toString)
          lapsLabel.setText(activity.getLaps.size.toString)
          totalTimeLabel.setText(durationToString(activity.getGrossDuration))
          ridingTimeLabel.setText(durationToString(activity.getNetDuration))
          climbedLabel.setText(activity.getAltitudeGain.convert(LengthUnit.Meter).toString);
        }
      }
    } 
  }
}
