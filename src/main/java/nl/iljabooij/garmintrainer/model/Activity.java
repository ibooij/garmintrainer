package nl.iljabooij.garmintrainer.model;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.google.common.collect.ImmutableList;

public interface Activity extends Comparable<Activity> {

	/**
	 * Get start time of activity.
	 * @return start time
	 */
	DateTime getStartTime();

	/**
	 * Get end time of activity
	 * @return the end time of the activity.
	 */
	DateTime getEndTime();

	/**
	 * Get maximum altitude of activity.
	 * @return maximum altitude
	 */
	Length getMaximumAltitude();

	/**
	 * Get minimum altitude of activity.
	 * @return minimum altitude
	 */
	Length getMinimumAltitude();

	/**
	 * Get Maximum Speed of Exercise
	 */
	Speed getMaximumSpeed();

	/**
	 * Get gross duration (from start time to time of last measurement) of the
	 * Activity.
	 * @return gross duration
	 */
	Duration getGrossDuration();

	/**
	 * Get net duration (from start time to time of last measurement, minus
	 * pauses) of the Activity.
	 * @return net duration
	 */
	Duration getNetDuration();

	/**
	 * Get all track points in the activity.
	 * @return immutable list of track points.
	 */
	ImmutableList<TrackPoint> getTrackPoints();

	/**
	 * Get Laps in Activity
	 * @return all laps
	 */
	ImmutableList<Lap> getLaps();

	/**
	 * {@inheritDoc}
	 */
	String toString();

	/**
	 * Get distance of the complete {@link ActivityImpl}.
	 * @return total distance.
	 */
	Length getDistance();

	/**
	 * Get total altitude gained
	 * @return total altitude gain
	 */
	Length getAltitudeGain();
}