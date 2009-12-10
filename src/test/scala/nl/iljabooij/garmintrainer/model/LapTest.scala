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

import org.junit.Assert._
import org.mockito.Mockito._
import org.joda.time.{DateTime,Duration,Minutes}
import org.scalatest.junit.{JUnit3Suite,AssertionsForJUnit}
import org.scalatest.mock.MockitoSugar
import scala.collection.jcl.Conversions._
import scala.collection.mutable.ListBuffer

class LapTest extends JUnit3Suite with AssertionsForJUnit with MockitoSugar {
  private var testLap:Lap = _
  private var tracks:List[Track] = _
  private var allTrackPoints:List[TrackPoint] = _

  private val START_TIME = new DateTime
  private val NR_OF_TRACK_POINTS = 100
  private val NR_OF_TRACKS = 3

  override def setUp() {
    val trackPointsBuffer = new ListBuffer[TrackPoint]
    val tracksBuffer = new ListBuffer[Track]
    
    for (trackNr <- 0 until NR_OF_TRACKS) {
      val trackPoints = new ListBuffer[TrackPoint]
      for (tpNr <- 0 until NR_OF_TRACK_POINTS) {
        val trackPoint = mock[TrackPoint]
        when(trackPoint.getTime).thenReturn(START_TIME.plusSeconds(tpNr + 1))
        trackPoints += trackPoint
      }
      trackPointsBuffer ++= trackPoints
      val track = mock[Track]
      when(track.trackPoints).thenReturn(trackPoints.toList)
      val trackStartTime = trackPoints.toList.head.getTime
      val trackEndTime = trackPoints.toList.last.getTime
      when(track.startTime).thenReturn(trackStartTime)
      when(track.endTime).thenReturn(trackEndTime)
      
      tracksBuffer += track
    }
    allTrackPoints = trackPointsBuffer.toList
    
    testLap = new Lap(START_TIME, tracksBuffer.toList)
  } 

  def testGetStartTime {
    val track = mock[Track]
    val lap = new Lap(START_TIME, List(track))
    
    assertEquals(START_TIME, testLap.startTime)
  }
  
  def testEndTime {
    assertEquals(allTrackPoints.last.getTime, testLap.endTime)
  }

  def testTrackPoints {
    assertEquals(allTrackPoints, testLap.trackPoints)
  }

  def testGetGrossDuration {
    val tracks = List(mock[Track],mock[Track],mock[Track])
    
    when(tracks(0).startTime).thenReturn(START_TIME)
    when(tracks(0).endTime).thenReturn(START_TIME.plusMinutes(1))
    when(tracks(1).startTime).thenReturn(START_TIME.plusMinutes(2))
    when(tracks(1).endTime).thenReturn(START_TIME.plusMinutes(3))
    when(tracks(2).startTime).thenReturn(START_TIME.plusMinutes(4))
    when(tracks(2).endTime).thenReturn(START_TIME.plusMinutes(5))
    
    val lap = new Lap(START_TIME, tracks)
    
    assertEquals(new Duration(tracks.head.startTime, tracks.last.endTime),
                 lap.grossDuration)
  }
  
  def testNetDuration {
    val GAP = Minutes.ONE
    val TRACK_TIME = Minutes.TWO
  
    def startTime(trackNr: Int) = {
      START_TIME.plus(GAP.plus(TRACK_TIME).multipliedBy(trackNr))
    }
    	
    def endTime(trackNr: Int) = {
      startTime(trackNr + 1).minus(GAP)
    }
    
    val tracks = (0 to 1).map(trackNr => {
      val track = mock[Track]
      when(track.startTime).thenReturn(startTime(trackNr))
      when(track.endTime).thenReturn(endTime(trackNr))
      when(track.duration).thenReturn(TRACK_TIME.toStandardDuration)
      
      track
    }).toList
    
    val lap = new Lap(START_TIME, tracks)
    
    val netDuration = TRACK_TIME.multipliedBy(tracks.size).toStandardDuration
    
    assertEquals(netDuration, lap.netDuration)
  }
   
  /** Test if net duration is correct when there is a difference in
      the start of the lap and start of the first track within that lap. */
  def testNetDurationWithDelayedRecording {
    val startTime = new DateTime
    val delay = Duration.standardSeconds(5)
    val startOfTrack = startTime.plus(delay)
    val trackDuration = Duration.standardMinutes(5)
    
    val track = mock[Track]
    when(track.startTime).thenReturn(startOfTrack)
    when(track.duration).thenReturn(trackDuration)
    
    val lap = new Lap(startTime, List(track))
    
    assertEquals(trackDuration, lap.netDuration)
  }
}
