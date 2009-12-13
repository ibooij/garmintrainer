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
	
	@Before
	public void setUp() {
		startTrackPoint = new StartTrackPoint(START_TIME,
				TRACK_POINT_TIME, 0, Length.createLengthInMeters(100.0), DISTANCE, 
				0.0, 0.0);
	}
	
	@Test
	public void testGetSpeed() {
		
		// speed as calculated:
		Speed speed = Speed.createSpeedInMetersPerSecond(DISTANCE, new Duration(START_TIME, TRACK_POINT_TIME));
		assertEquals(speed, startTrackPoint.getSpeed());
	}
	
	@Test
	public void testAltitudeDelta() {
		assertEquals("Start point should not have altitude gain",
				Length.createLengthInMeters(0.0), startTrackPoint.getAltitudeDelta());
	}

}
