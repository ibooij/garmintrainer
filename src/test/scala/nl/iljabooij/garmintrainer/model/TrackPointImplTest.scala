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
import org.scalatest.junit.{JUnit3Suite,AssertionsForJUnit}
import org.scalatest.mock.MockitoSugar

class TrackPointImplTest extends JUnit3Suite with AssertionsForJUnit with MockitoSugar {
  private val DELTA_FOR_DOUBLE_EQUALITY = 0.0001
  
  private var trackPointImpl:TrackPointImpl = _
  private var measuredTrackPoint:MeasuredTrackPoint = _
  /**
   * This is a concrete version of {@link TrackPointImpl}. We need this
   * because {@link TrackPointImpl} is abstract, but we still want to 
   * test it. It contains dummy implementations of the abstract methods
   * of {@link TrackPointImpl}, plus a constructor that just calls the
   * super() constructor with it's argument.
   * @author ilja
   *
   */
  private class ConcreteTrackPointImpl(measuredTrackPoint:MeasuredTrackPoint)
  		extends TrackPointImpl(measuredTrackPoint) {
    def altitudeDelta = null
    
    def speed = null
		
  }
	
  override def setUp() {
    // TrackPointImpl is abstract, so we need to create a subclass of
    // measured track point to test with.
    measuredTrackPoint = mock[MeasuredTrackPoint]
	trackPointImpl = new ConcreteTrackPointImpl(measuredTrackPoint)
  }
  
  def testAltitude {
    val altitude = new Length.Meter(100.0)
    when(measuredTrackPoint.altitude).thenReturn(altitude)
    
    assertEquals(altitude, trackPointImpl.altitude)
    verify(measuredTrackPoint, times(1)).altitude
  }
  
  def testDistance {
    val distance = new Length.Meter(100.0)
    when(measuredTrackPoint.distance).thenReturn(distance)
    
    assertEquals(distance, trackPointImpl.distance)
    verify(measuredTrackPoint, times(1)).distance
  }
  
  def testheartRate {
    val heartRate = 127
    when(measuredTrackPoint.heartRate).thenReturn(heartRate)
    
    assertEquals(heartRate, trackPointImpl.heartRate)
    verify(measuredTrackPoint, times(1)).heartRate
  }
  
  def testlatitude {
    val latitude = 20.0
    when(measuredTrackPoint.latitude).thenReturn(latitude)
    
    assertEquals(latitude, trackPointImpl.latitude, DELTA_FOR_DOUBLE_EQUALITY)
    verify(measuredTrackPoint, times(1)).latitude
  }
  		
  def testlongitude {
    val longitude = 20.0
    when(measuredTrackPoint.longitude).thenReturn(longitude)
    
    assertEquals(longitude, trackPointImpl.longitude, DELTA_FOR_DOUBLE_EQUALITY)
    verify(measuredTrackPoint, times(1)).longitude
  }
  
  def testtime {
    val time = new DateTime
    when(measuredTrackPoint.time).thenReturn(time)
    
    assertEquals(time, trackPointImpl.time)
    verify(measuredTrackPoint, times(1)).time
  }
	
  def testHasPositionWithNulPosition {
    when(measuredTrackPoint.latitude).thenReturn(0.0)
    when(measuredTrackPoint.longitude).thenReturn(0.0)
    
    assertEquals(false, trackPointImpl.hasPosition)
    verify(measuredTrackPoint, atMost(1)).latitude
	verify(measuredTrackPoint, atMost(1)).longitude
  }
}
