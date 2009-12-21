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

import nl.iljabooij.garmintrainer.model.Duration._
import org.junit.Assert._
import org.mockito.Mockito._
import org.scalatest.junit.{JUnit3Suite,AssertionsForJUnit}
import org.scalatest.mock.MockitoSugar

class NonStartTrackPointTest extends JUnit3Suite with AssertionsForJUnit with MockitoSugar {
	private var firstPoint:MeasuredTrackPoint = _
	private var secondPoint:MeasuredTrackPoint = _	
	private var nonStartTrackPoint:NonStartTrackPoint = _

	private val START_TIME = new DateTime
	private val SECOND_TIME = START_TIME + second * 10
	private val FIRST_ALTITUDE = new Length.Meter(150.0)
	private val SECOND_ALTITUDE = Length.Meter(151.0)
	private val SECOND_DISTANCE = Length.Meter(20.0)

	/**
	 * Setup for tests
	 */

	override def setUp {
		firstPoint = mock[MeasuredTrackPoint]
		when(firstPoint.time).thenReturn(START_TIME)
		when(firstPoint.distance).thenReturn(Length.ZERO)
		when(firstPoint.altitude).thenReturn(FIRST_ALTITUDE)
		
		secondPoint = mock[MeasuredTrackPoint]
		when(secondPoint.time).thenReturn(SECOND_TIME)
		when(secondPoint.distance).thenReturn(SECOND_DISTANCE)
		when(secondPoint.altitude).thenReturn(SECOND_ALTITUDE)
		
		nonStartTrackPoint = new NonStartTrackPoint(firstPoint, secondPoint)
	}

	/**
	 * Test speed for secondPoint trackpoint.
	 */
	def testSpeedInSecondSample() {
		val duration = new Duration(START_TIME, SECOND_TIME)
		val distance = SECOND_DISTANCE
		val speed = Speed.speed(distance, duration)

		assertEquals(speed, nonStartTrackPoint.speed)
		
		verify(firstPoint, times(1)).distance
		verify(firstPoint, times(1)).time
		verify(secondPoint, times(1)).distance
		verify(secondPoint, times(1)).time
	}

    def testGetAltitudeDelta() {
		val gain = SECOND_ALTITUDE - FIRST_ALTITUDE
		assertEquals(gain, nonStartTrackPoint.altitudeDelta)
	}
}
