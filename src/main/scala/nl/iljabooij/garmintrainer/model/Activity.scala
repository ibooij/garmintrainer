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
package nl.iljabooij.garmintrainer.model

trait Activity extends NotNull {
  /**
   * Get start time of activity.
   * @return start time
   */
  def startTime:DateTime
  
  /**
   * Get end time of activity
   * @return the end time of the activity.
   */
   def endTime:DateTime

	/**
	 * Get maximum altitude of activity.
	 * @return maximum altitude
	 */
	def maximumAltitude: Length
	
	/**
	 * Get minimum altitude of activity.
	 * @return minimum altitude
	 */
	def minimumAltitude:Length

	/**
	 * Get Maximum Speed of Exercise
	 */
	def maximumSpeed:Speed

	/**
	 * Get gross duration (from start time to time of last measurement) of the
	 * Activity.
	 * @return gross duration
	 */
	def grossDuration:Duration

	/**
	 * Get net duration (from start time to time of last measurement, minus
	 * pauses) of the Activity.
	 * @return net duration
	 */
	def netDuration: Duration

	/**
	 * Get all track points in the activity.
	 * @return immutable list of track points.
	 */
	def trackPoints:List[TrackPoint]

	/**
	 * Get Laps in Activity
	 * @return all laps
	 */
	def laps:List[Lap]

	/**
	 * Get distance of the complete {@link ActivityImpl}.
	 * @return total distance.
	 */
	def distance:Length

	/**
	 * Get total altitude gained
	 * @return total altitude gain
	 */
	def altitudeGain: Length
}