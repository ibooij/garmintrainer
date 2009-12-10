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

import com.google.common.base.Preconditions.checkArgument
import com.google.common.base.Preconditions.checkNotNull

import org.apache.commons.lang.builder.{EqualsBuilder,HashCodeBuilder}
import org.joda.time.{DateTime,Duration}

import scala.collection.jcl.Conversions._
import scala.collection.mutable.ListBuffer

/**
 * Implementation of {@link Activity}. Note that instances of ActivityImpl are
 * immutable (hence the @Immutable annotation).
 * 
 * @author ilja
 */

class ActivityImpl(val startTime:DateTime, val laps:List[Lap]) 
    extends Activity with Comparable[Activity] {
  checkNotNull(startTime)
  checkArgument(!laps.isEmpty)
  
  
  override def endTime = trackPoints.last.getTime
  override def maximumAltitude = altitudesInOrder.last
  
  override def altitudeClass = AltitudeClass.forMaximumAltitude(maximumAltitude)
  
  override def minimumAltitude = altitudesInOrder.head
  
  private def altitudesInOrder = trackPoints.map(_.getAltitude).sort((a,b) => (a compareTo b) < 0)
  
  override def maximumSpeed = trackPoints.map(_.getSpeed).sort((a,b) => (a.compareTo(b)) > 0).head 

  override def grossDuration = new Duration(startTime, trackPoints.last.getTime)

  override def netDuration = {
    var netDuration = new Duration(startTime, laps.head.startTime)
    laps.foldLeft(netDuration)((duration,lap) => duration.plus(lap.netDuration))
  }

  override def trackPoints = {
    var buffer = new ListBuffer[TrackPoint]
    laps.foreach(buffer ++= _.trackPoints)
    
    buffer.toList
  }
  
  override def toString = "Activity [start time=" + startTime + ", #laps=" + laps.size + "]";
  
  override def distance = trackPoints.last.getDistance
	
  /**
   Get altitude gain for total activity. Disregard small climbs of less than 5.0 meters up.
   */
  override def altitudeGain = {
    val ZERO_GAIN = Length.createLengthInMeters(0.0)
    val MINIMUM_GAIN = Length.createLengthInMeters(5.0)
    
    // returns a list of climbs. A Climb is a list of track points that all have
    // a positive altitude delta.
    def findClimbs(theClimbs:List[List[TrackPoint]], currentClimb:List[TrackPoint], trackPoints:List[TrackPoint]):List[List[TrackPoint]] = {
      trackPoints match {
        case Nil => currentClimb :: theClimbs
        case trackPoint :: rest => {
            if (trackPoint.getAltitudeDelta.compareTo(ZERO_GAIN) > 0) 
              findClimbs(theClimbs, trackPoint :: currentClimb, rest)
            else if (currentClimb.isEmpty)
              findClimbs(theClimbs, List(), rest)
            else
              findClimbs(currentClimb :: theClimbs, List(), rest)
        }
      }
    }
    // calculate total gain for climb
    def totalGainForClimb(climb:List[TrackPoint]):Length = {
      climb.foldLeft(ZERO_GAIN)((climb, tp) => climb.plus(tp.getAltitudeDelta))
    }
    
    // find all climbs, calculate gain per climb, filter out climbs with less than
    // MINIMUM_GAIN gain and sum the totals.
    findClimbs(List(List()), List(), trackPoints)
      .map(totalGainForClimb)
      .filter(_.compareTo(MINIMUM_GAIN) > 0)
      .foldLeft(ZERO_GAIN)((total,climb) => total.plus(climb))
  }

  /** {@inheritDoc} */
  override def equals(o: Any) = {
    if (o == null) false
    else if (!(o.isInstanceOf[Activity])) false
    else {
      // compare with interface Activity, not with ActivityImpl!
      val other = o.asInstanceOf[Activity]
      new EqualsBuilder().append(other.startTime, startTime)
				.append(other.laps, laps).isEquals()
	}
  }
  
  /** Seeds for {@link HashCodeBuilder}. */
  private val HASH_CODE_SEEDS = List(31, 43)

  /** {@inheritDoc} */
  override def  hashCode = {
    new HashCodeBuilder(HASH_CODE_SEEDS(0), HASH_CODE_SEEDS(1))
      .append(startTime).append(laps).toHashCode()
  }

  /** {@inheritDoc} */
  override def compareTo(o: Activity) = {
    checkNotNull(o)
    
    startTime.compareTo(o.startTime);
  }
}
