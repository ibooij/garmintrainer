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
import org.joda.time.{DateTime,Duration}
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
//	
//	@Test
//	public void testGetNetDuration() {
//		// three tracks, with pauses in between
//		Track[] tracks = new Track[3];
//		tracks[0] = mock(Track.class);
//		tracks[1] = mock(Track.class);
//		tracks[2] = mock(Track.class);
//		
//		when(tracks[0].getStartTime()).thenReturn(START_TIME);
//		when(tracks[0].getEndTime()).thenReturn(START_TIME.plusMinutes(1));
//		when(tracks[1].getStartTime()).thenReturn(START_TIME.plusMinutes(2));
//		when(tracks[1].getEndTime()).thenReturn(START_TIME.plusMinutes(3));
//		when(tracks[2].getStartTime()).thenReturn(START_TIME.plusMinutes(4));
//		when(tracks[2].getEndTime()).thenReturn(START_TIME.plusMinutes(5));
//		
//		for (Track track: tracks) {
//			when(track.getDuration()).thenReturn(new Duration(60000));
//		}
//		
//		Lap lap = new Lap(START_TIME, Arrays.asList(tracks));
//		Duration netDuration = new Duration(0);
//		for (Track track: tracks) {
//			netDuration = netDuration.plus(new Duration(track.getStartTime(), track.getEndTime()));
//		}
//		assertEquals(netDuration,
//				lap.getNetDuration());	
//	}
//	
//	/**
//	 * Lap start time and start time of first track may be different (first track may
//	 * start a little after the lap. Make sure that {@link Lap#getNetDuration()}
//	 * returns the correct value.
//	 */
//	@Test
//	public void testGetNetDurationWithDelayedRecording() {
//		final DateTime startOfLap = new DateTime();
//		// track starts 5 seconds after lap
//		final Duration trackDelay = Duration.standardSeconds(5);
//		final DateTime startOfTrack = startOfLap.plus(trackDelay);
//		
//		final Duration trackDuration = Duration.standardMinutes(5);
//		
//		Track track = mock(Track.class);
//		when(track.getStartTime()).thenReturn(startOfTrack);
//		when(track.getDuration()).thenReturn(trackDuration);
//		
//		Lap lap = new Lap(startOfLap, Lists.newArrayList(track));
//		
//		assertEquals(trackDelay.plus(trackDuration), lap.getNetDuration());
//	}
}
