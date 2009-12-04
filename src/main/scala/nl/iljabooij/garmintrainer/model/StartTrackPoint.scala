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
 * First track point in the Activity
 * 
 * @author ilja
 * @param startTime start time of the {@link Activity}.
 * @param measuredTrackPoint track point as measured by the gps device.
 */
class StartTrackPoint(startTime: DateTime, measuredTrackPoint: MeasuredTrackPoint) 
	extends TrackPointImpl(measuredTrackPoint) {
  override def getSpeed = {
    val timeTravelled = new Duration(startTime, getTime())
    Speed.createSpeedInMetersPerSecond(getDistance(), timeTravelled)
  }

  override def getAltitudeDelta = Length.createLengthInMeters(0.0)
}
