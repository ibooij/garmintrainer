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
package nl.iljabooij.garmintrainer.parser.digester;

import org.joda.time.DateTime
import scala.collection.mutable.ListBuffer
import scala.reflect.BeanProperty

class TrackType {
  private val trackPointBuffer = new ListBuffer[TrackPointType]
                                                
  var startTime: DateTime = null
  
  def trackPointTypes = trackPointBuffer.toList
  
  def addTrackPoint(trackPoint: TrackPointType) {
    trackPointBuffer += trackPoint
  }
}
