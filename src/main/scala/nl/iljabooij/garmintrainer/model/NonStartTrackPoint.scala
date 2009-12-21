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

import org.joda.time.Duration
import nl.iljabooij.garmintrainer.Preconditions._

class NonStartTrackPoint(previous:MeasuredTrackPoint, measuredTrackPoint:MeasuredTrackPoint) 
    extends TrackPointImpl(measuredTrackPoint) {
  checkNotNull(previous)
	
  /** time since last track point */
  private lazy val timeTravelled = new Duration(previous.time, time)
  /** distance travelled from last track point */
  private lazy val distanceTravelled = distance - previous.distance
  
  /** speed of interval from previous point to this point */
  override def speed = distanceTravelled / timeTravelled
  
  override def altitudeDelta = {
    altitude - previous.altitude
  }
}
