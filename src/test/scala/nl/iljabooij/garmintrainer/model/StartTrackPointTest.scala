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

/**
 * Test for {@link StartTrackPoint}.
 * @author ilja
 *
 */
class StartTrackPointTest extends JUnit3Suite with AssertionsForJUnit with MockitoSugar {
	private val START_TIME = new DateTime()
	private val TRACK_POINT_TIME = START_TIME.plusSeconds(10)
	private val DISTANCE = Length.createLengthInMeters(100.0)
	
	private var startTrackPoint:StartTrackPoint = null
	private var measuredTrackPoint:MeasuredTrackPoint = null
	
	
	override def setUp() {
		measuredTrackPoint = mock[MeasuredTrackPoint]
		startTrackPoint = new StartTrackPoint(START_TIME, measuredTrackPoint)
	}
	
	def testGetSpeed() {
		when (measuredTrackPoint.time).thenReturn(TRACK_POINT_TIME)
		when (measuredTrackPoint.distance).thenReturn(DISTANCE)
		// speed as calculated:
		val speed = Speed.createSpeedInMetersPerSecond(DISTANCE, new Duration(START_TIME, TRACK_POINT_TIME))
		
		assertEquals(speed, startTrackPoint.speed)
		
		verify(measuredTrackPoint, times(1)).time
		verify(measuredTrackPoint, times(1)).distance
	}
	
	def testAltitudeDelta() {
		assertEquals("Start point should not have altitude gain",
				Length.createLengthInMeters(0.0), startTrackPoint.altitudeDelta)
	}
}
