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

import org.joda.time.{DateTime,Duration}

/**
 * First track point in the Activity. Because the real start point
 * is not noted, this is the first point recorded after the start.
 * 
 * @author ilja
 * @param startTime start time of the {@link Activity}.
 * @param measuredTrackPoint track point as measured by the gps device.
 */
class StartTrackPoint(startTime: DateTime, measuredTrackPoint: MeasuredTrackPoint) 
	extends TrackPointImpl(measuredTrackPoint) {
	
  /** speed travelled between the start and this point. */
  override def speed = distance / new Duration(startTime, time)
    
  override def altitudeDelta = Length.ZERO
}
