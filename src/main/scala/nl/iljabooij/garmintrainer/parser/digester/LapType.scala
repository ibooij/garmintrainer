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
import scala.collection.mutable.ListBuffer
import scala.reflect.BeanProperty

/**
 * Builder for Lap Objects.
 * 
 * @author "Ilja Booij <ibooij@gmail.com>"
 * 
 */
class LapType extends LoggerHelper {
  private val tracksBuffer = new ListBuffer[TrackType]
  
  @BeanProperty  
  var startTime:DateTime = null

  /**
   * get all tracks in the lap
   */
  def tracks = tracksBuffer.toList
  
  /**
   * get end time of the lap
   */
  def endTime = tracksBuffer.last.getEndTime
  
  /**
   * Add a {@link TrackType} to the lap. the {@link TrackType} will be added
   * as the last track in the lap.
   * 
   * @param track {@link TrackPointType} to add.
   */
  def addTrack(track: TrackType) {
    tracksBuffer += track
  }
}