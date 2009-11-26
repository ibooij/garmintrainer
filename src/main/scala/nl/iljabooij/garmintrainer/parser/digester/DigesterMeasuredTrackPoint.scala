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
package nl.iljabooij.garmintrainer.parser.digester

import org.joda.time.DateTime

import com.google.common.base.Preconditions

import net.jcip.annotations.Immutable
import nl.iljabooij.garmintrainer.model.Length
import nl.iljabooij.garmintrainer.model.MeasuredTrackPoint

/**
 * An Implementation of {@link MeasuredTrackPoint} that uses a {@link TrackPointType} 
 * object to get it's values.
 * @author ilja
 *
 */
@Immutable
class DigesterMeasuredTrackPoint(delegate: TrackPointType) extends MeasuredTrackPoint {
  if (delegate == null) throw new NullPointerException
  
  override def getHeartRate = delegate.heartRate
  override def getAltitude = delegate.altitude
  override def getDistance = delegate.distance
  override def getTime = delegate.time
  override def getLatitude = delegate.latitude
  override def getLongitude = delegate.longitude
}


