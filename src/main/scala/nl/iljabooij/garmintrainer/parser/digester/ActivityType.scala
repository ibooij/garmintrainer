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

import java.util.{ArrayList => JAList, List => JList}

import nl.iljabooij.garmintrainer.model._
import nl.iljabooij.garmintrainer.model.Length.Meter

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import scala.collection.jcl.Conversions._
import scala.collection.mutable.ListBuffer
import scala.reflect.BeanProperty

/**
 * Builder for {@link Activity}.
 * 
 * @author "Ilja Booij"
 */
class ActivityType extends NotNull {
  // use Bean Property so it can be set by the commons digester.
  @BeanProperty
  var id: String = null
  
  private var lapTypes = List[LapType]()
  private val dateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis()
  
  /**
   * extract all TrackPointType objects from all laps of the activity.
   */
  private def extractTrackPointTypes: List[TrackPointType] = {
    val trackPoints = new ListBuffer[TrackPointType]
    lapTypes.foreach (lapType => {
      lapType.tracks.foreach (trackType => {
        trackType.trackPointTypes.foreach(trackPoints += _)
      })
    })
    trackPoints.toList
  }
  
  private def adjustForLateFix {
    val trackPointTypes = extractTrackPointTypes
    
    val firstPointWithAltitude = trackPointTypes.find(_.altitude != null)
    val firstAltitude = if (firstPointWithAltitude.isEmpty) Length.ZERO 
                        else firstPointWithAltitude.get.altitude
    
    var lastAltitude = firstAltitude
    var lastDistance:Length = new Meter(0)
    trackPointTypes.foreach (trackPointType => {
      val altitude = trackPointType.altitude
      if (altitude == null) trackPointType.altitude =lastAltitude
      else lastAltitude = altitude
      
      val distance = trackPointType.distance
      if (distance == null) trackPointType.distance = lastDistance
      else lastDistance = distance
    })
  }
    
  /**
   * Build the activity.
   * @return a new Activity
   */
  def build: Activity = {
    val startTime = dateTimeFormatter.parseDateTime(id)
    
    adjustForLateFix
  
    var previousMeasuredTrackPoint:Option[MeasuredTrackPoint] = None
    var laps = new ListBuffer[Lap]

    lapTypes.foreach (lapType => {
      val tracks = new ListBuffer[Track]
      lapType.tracks.foreach (trackType => {
        val trackPoints = new ListBuffer[TrackPoint]
    	trackType.trackPointTypes.foreach(trackPointType => {
    	  val measuredTrackPoint = new DigesterMeasuredTrackPoint(trackPointType)
          val newTrackPoint = 
            if (laps.isEmpty && tracks.isEmpty && trackPoints.isEmpty) 
              new StartTrackPoint(startTime, measuredTrackPoint)
            else
              new NonStartTrackPoint(previousMeasuredTrackPoint.get, measuredTrackPoint)
           trackPoints += newTrackPoint
           previousMeasuredTrackPoint = Some(measuredTrackPoint)
    	})
        tracks += new Track(trackPoints.toList)
      })
      laps += new Lap(lapType.startTime, tracks.toList)
    })
    
    new ActivityImpl(startTime, laps.toList)
  }
    
  /**
   * Add a {@link LapType} to the {@link ActivityType}.
   * @param lapBuilder the {@link LapType} to add.
   * @return the {@link ActivityType}.
   */
  def addLap(lapType: LapType): ActivityType = {
    lapTypes = lapTypes ::: List(lapType)
    return this;
  }
}
