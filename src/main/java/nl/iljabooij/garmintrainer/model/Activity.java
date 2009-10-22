/*
 * Copyright 2009 Ilja Booij
 * 
 * This file is part of GarminTrainer.
 * 
 * GarminTrainer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GarminTrainer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GarminTrainer.  If not, see <http://www.gnu.org/licenses/>.
 */
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
	
	/**
	 * Get the TrackPoint for a certain point in time.
	 * @param dateTime the time to search for
	 * @return the TrackPoint for the time. If the time is not found in the
	 * trackpoints, it returns the track point with the latest time before dateTime.
	 * if the time is before the first track point's time, the first track point
	 * is returned anyway.
	 */
	public TrackPoint getTrackPointForTime(final DateTime dateTime);
}