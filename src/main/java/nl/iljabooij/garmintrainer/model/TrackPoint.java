package nl.iljabooij.garmintrainer.model;

import java.io.Serializable;
import java.util.Comparator;

import org.joda.time.DateTime;

public interface TrackPoint {
	int getHeartRate();

	Length getAltitude();

	Length getDistance();

	/**
	 * Get the time at which the sample was taken.
	 * @return the sample time.
	 */
	DateTime getTime();

	double getLatitude();

	double getLongitude();

	boolean hasPosition();

	/**
	 * Get the speed from the last track point to this one.
	 * @return the Speed.
	 */
	Speed getSpeed();

	/**
	 * Get altitude gained or lost in this track point compared to the last
	 * @return the altitude delta
	 */
	Length getAltitudeDelta();
	
	/**
	 * Ordering that can be used to order Track points according to altitude
	 * @author ilja
	 */
	public static class AltitudeComparator implements Comparator<TrackPoint>, Serializable {
		private static final long serialVersionUID = 1L;

		public int compare(TrackPoint o1, TrackPoint o2) {
			return o1.getAltitude().compareTo(o2.getAltitude());
		}
	}
	
	/**
	 * Ordering that can be used to order Track points according to speed
	 * @author ilja
	 */
	public static class SpeedComparator implements Comparator<TrackPoint>, Serializable {
		private static final long serialVersionUID = 1L;
		
		public int compare(TrackPoint o1, TrackPoint o2) {
			return o1.getSpeed().compareTo(o2.getSpeed());
		}
	}
	
	/**
	 * Comparator for trackpoint times
	 */
	public static class TimeComparator implements Comparator<TrackPoint>, Serializable {
		private static final long serialVersionUID = 1L;
		
		public int compare(final TrackPoint tp1, final TrackPoint tp2) {
			return tp1.getTime().compareTo(tp2.getTime());
		}
	}
}