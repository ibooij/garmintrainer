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

import nl.iljabooij.garmintrainer.model.Length
import nl.iljabooij.garmintrainer.model.MeasuredTrackPoint

/**
 * An Implementation of {@link MeasuredTrackPoint} that uses a {@link TrackPointType} 
 * object to get it's values.
 * @author ilja
 *
 */
class DigesterMeasuredTrackPoint(delegate: TrackPointType) 
     extends MeasuredTrackPoint with NotNull {
  override def heartRate = delegate.heartRate
  override def altitude = delegate.altitude
  override def distance = delegate.distance
  override def time = delegate.time
  override def latitude = delegate.latitude
  override def longitude = delegate.longitude
}


