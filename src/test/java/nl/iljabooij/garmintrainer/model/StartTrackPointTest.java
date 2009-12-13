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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link StartTrackPoint}.
 * @author ilja
 *
 */
public class StartTrackPointTest {
	private static final DateTime START_TIME = new DateTime();
	private static final DateTime TRACK_POINT_TIME = START_TIME.plusSeconds(10);
	private static final Length DISTANCE = Length.createLengthInMeters(100.0);
	
	private StartTrackPoint startTrackPoint;
	private MeasuredTrackPoint measuredTrackPoint;
	
	@Before
	public void setUp() {
		measuredTrackPoint = mock(MeasuredTrackPoint.class);
		startTrackPoint = new StartTrackPoint(START_TIME, measuredTrackPoint);
	}
	
	@Test
	public void testGetSpeed() {
		when (measuredTrackPoint.getTime()).thenReturn(TRACK_POINT_TIME);
		when (measuredTrackPoint.getDistance()).thenReturn(DISTANCE);
		// speed as calculated:
		Speed speed = Speed.createSpeedInMetersPerSecond(DISTANCE, new Duration(START_TIME, TRACK_POINT_TIME));
		assertEquals(speed, startTrackPoint.getSpeed());
		
		verify(measuredTrackPoint, times(1)).getTime();
		verify(measuredTrackPoint, times(1)).getDistance();
	}
	
	@Test
	public void testAltitudeDelta() {
		assertEquals("Start point should not have altitude gain",
				Length.createLengthInMeters(0.0), startTrackPoint.getAltitudeDelta());
	}

}
