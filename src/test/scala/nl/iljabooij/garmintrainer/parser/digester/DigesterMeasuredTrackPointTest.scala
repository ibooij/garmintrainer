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
import org.junit.Assert._
import org.mockito.Mockito._
import org.scalatest.junit.{JUnit3Suite,AssertionsForJUnit}
import org.scalatest.mock.MockitoSugar

import nl.iljabooij.garmintrainer.model.Length

class DigesterMeasuredTrackPointTest extends JUnit3Suite with MockitoSugar {
	val DELTA_FOR_DOUBLE_EQUALITY = 0.0001
	var trackPoint: TrackPointType = null
	var dmtp: DigesterMeasuredTrackPoint = null
	
	override def setUp {
	  trackPoint = mock[TrackPointType]
      dmtp = new DigesterMeasuredTrackPoint(trackPoint)
	}

	def testConstructorDoesNotAcceptNullDelegate {
	  intercept[NullPointerException] (new DigesterMeasuredTrackPoint(null))
	}
	
	def testGetAltitude {
	  val altitude = Length.createLengthInMeters(100.0)
      when(trackPoint.altitude).thenReturn(altitude)
	  assertEquals(altitude, dmtp.getAltitude())
      verify(trackPoint, times(1)).altitude
	}

	def testGetDistance {
		val distance = Length.createLengthInMeters(100.0)
		when(trackPoint.distance).thenReturn(distance)
		
		assertEquals(distance, dmtp.getDistance())
		verify(trackPoint, times(1)).distance
	}

	def testGetHeartRate {
		val heartRate = 127
		when(trackPoint.heartRate).thenReturn(heartRate)
		
		assertEquals(heartRate, dmtp.getHeartRate())
		verify(trackPoint, times(1)).heartRate
	}

	def testGetLatitude {
		val latitude = 4.213 // just some latitude
		when(trackPoint.latitude).thenReturn(latitude)
		
		assertEquals(latitude, dmtp.getLatitude(), DELTA_FOR_DOUBLE_EQUALITY)
		verify(trackPoint, times(1)).latitude
	}

	def testGetLongitude {
		val longitude = 14.213 // just some longitude
		when(trackPoint.longitude).thenReturn(longitude)
		
		assertEquals(longitude, dmtp.getLongitude(), DELTA_FOR_DOUBLE_EQUALITY)
		verify(trackPoint, times(1)).longitude
	}

	def testGetTime {
		val time = new DateTime()
		when(trackPoint.time).thenReturn(time)
		
		assertEquals(time, dmtp.getTime())
		verify(trackPoint, times(1)).time
	}
}
