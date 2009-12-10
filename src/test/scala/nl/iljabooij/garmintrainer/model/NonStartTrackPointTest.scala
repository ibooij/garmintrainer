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
package nl.iljabooij.garmintrainer.model;



import org.joda.time.{DateTime,Duration}
import org.junit.Assert._
import org.mockito.Mockito._
import org.scalatest.junit.{JUnit3Suite,AssertionsForJUnit}
import org.scalatest.mock.MockitoSugar

class NonStartTrackPointTest extends JUnit3Suite with AssertionsForJUnit with MockitoSugar {
	private var first:MeasuredTrackPoint = null
	private var second:MeasuredTrackPoint = null	
	private var nonStartTrackPoint:NonStartTrackPoint = null

	private val START_TIME = new DateTime()
	private val SECOND_TIME = START_TIME.plusSeconds(10)
	private val FIRST_ALTITUDE = Length
		.createLengthInMeters(150.0)
	private val SECOND_ALTITUDE = Length
			.createLengthInMeters(151.0)
	private val SECOND_DISTANCE = Length
			.createLengthInMeters(20.0)

	/**
	 * Setup for tests
	 */

	override def setUp {
		first = mock[MeasuredTrackPoint]
		when(first.time).thenReturn(START_TIME)
		when(first.distance).thenReturn(Length.createLengthInMeters(0.0))
		when(first.altitude).thenReturn(FIRST_ALTITUDE)
		
		second = mock[MeasuredTrackPoint]
		when(second.time).thenReturn(SECOND_TIME)
		when(second.distance).thenReturn(SECOND_DISTANCE)
		when(second.altitude).thenReturn(SECOND_ALTITUDE)
		
		nonStartTrackPoint = new NonStartTrackPoint(first, second)
	}

	/**
	 * Test speed for second trackpoint.
	 */
	def testSpeedInSecondSample() {
		val duration = new Duration(START_TIME, SECOND_TIME)
		val distance = SECOND_DISTANCE
		val speed = Speed.createSpeedInMetersPerSecond(distance, duration)

		assertEquals(speed, nonStartTrackPoint.speed)
		
		verify(first, times(1)).distance
		verify(first, times(1)).time
		verify(second, times(1)).distance
		verify(second, times(1)).time
	}

	def testConstructorFailsWithNullPrevious() {
	  intercept[NullPointerException] {
	    new NonStartTrackPoint(null, second)
      }
	}
	
	def testGetAltitudeDelta() {
		val gain = SECOND_ALTITUDE.minus(FIRST_ALTITUDE)
		assertEquals(gain, nonStartTrackPoint.altitudeDelta)
	}
}
