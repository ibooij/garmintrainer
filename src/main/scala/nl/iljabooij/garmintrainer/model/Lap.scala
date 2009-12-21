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

import scala.collection.mutable.ListBuffer
import scala.collection.jcl.Conversions.convertList

/**
 * Represents a Lap. A Lap is defined by it's start and end time.
 * @author ilja
 *
 */

class Lap(val startTime:DateTime, val tracks:List[Track]) {
  def endTime = tracks.last.endTime
  
  def trackPoints = {
    val buffer = new ListBuffer[TrackPoint]
    tracks.foreach(track => buffer ++= track.trackPoints)
    buffer.toList
  }

  /**
   * Get gross duration of Lap. That is, the total time from start of the lap 
   * to the end.
   * @return gross duration of the lap
   */
  def grossDuration = new Duration(startTime, endTime)
  
  /**
   * Get net duration of Lap. That is, the time from start to end, minus the
   * pauses.
   * @return net duration of lap.
   */
  def netDuration = {
    tracks.foldLeft(new Duration)((duration,track) => duration + track.duration)
  }
}
