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

import org.joda.time.{DateTime,Duration}
import org.junit.Assert._
import org.mockito.Mockito._
import org.scalatest.junit.{JUnit3Suite,AssertionsForJUnit}
import org.scalatest.mock.MockitoSugar
import scala.collection.jcl.Conversions._
import scala.collection.mutable.ListBuffer

/**
 * Tests for {@link Track}.
 * @author ilja
 *
 */
class TrackTest extends JUnit3Suite with AssertionsForJUnit with MockitoSugar {
  private var testTrack:Track = _
  private var trackPoints:List[TrackPoint] = _
  
  private val NR_OF_TRACK_POINTS = 100
  private val START_TIME = new DateTime
  
  override def setUp {
    trackPoints = (0 until NR_OF_TRACK_POINTS).map(tpNr => {
      val trackPoint = mock[TrackPoint]
      when(trackPoint.getTime).thenReturn(START_TIME.plusSeconds(tpNr))
      
      trackPoint
    }).toList
    testTrack = new Track(trackPoints)
  }
	
  def testStartTime {
    assertEquals(START_TIME, testTrack.startTime)
  }
  
  def testEndTime {
    assertEquals(trackPoints.last.getTime, testTrack.endTime)
  }
  
  def testDuration {
    assertEquals(new Duration(START_TIME, trackPoints.last.getTime),
                 testTrack.duration)
  }
  
  def testWrongConstructorUsage {
    intercept[IllegalArgumentException] {
      new Track(List())
    }
    intercept[NullPointerException] {
      new Track(null)
    }
  }
}
