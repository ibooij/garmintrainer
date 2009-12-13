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

import nl.iljabooij.garmintrainer.Preconditions._
import org.joda.time.DateTime



/**
 * Represents a single track point sample.
 * @author "Ilja Booij <ibooij@gmail.com>"
 *
 */
abstract class TrackPointImpl(measuredTrackPoint:MeasuredTrackPoint) extends TrackPoint {
  /**
   * {@inheritDoc}
   */
  override def toString =  measuredTrackPoint.toString
	
  /**
   * {@inheritDoc}
   */
  override def heartRate = measuredTrackPoint.heartRate
  
  /**
   * {@inheritDoc}
   */
  override def altitude = measuredTrackPoint.altitude
		
  /**
   * {@inheritDoc}
   */
  override def distance = measuredTrackPoint.distance
	
  /**
   * {@inheritDoc}
   */
  override def time = measuredTrackPoint.time

  /**
   * {@inheritDoc}
   */
  override def latitude = measuredTrackPoint.latitude
	
  /**
   * {@inheritDoc}
   */
  override def longitude = measuredTrackPoint.longitude
	
  override def hasPosition = latitude != 0.0 && longitude != 0.0
}
