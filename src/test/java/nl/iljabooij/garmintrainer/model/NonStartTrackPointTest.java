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

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class NonStartTrackPointTest {
	private MeasuredTrackPoint first;
	private MeasuredTrackPoint second;
	
	private NonStartTrackPoint nonStartTrackPoint;

	private static final DateTime START_TIME = new DateTime();
	private static final DateTime SECOND_TIME = START_TIME.plusSeconds(10);
	private static final Length FIRST_ALTITUDE = Length
		.createLengthInMeters(150.0);
	private static final Length SECOND_ALTITUDE = Length
			.createLengthInMeters(151.0);
	private static final Length SECOND_DISTANCE = Length
			.createLengthInMeters(20.0);

	/**
	 * Setup for tests
	 */
	@Before
	public void setUp() {
		first = mock(MeasuredTrackPoint.class);
		when(first.getTime()).thenReturn(START_TIME);
		when(first.getDistance()).thenReturn(Length.createLengthInMeters(0.0));
		when(first.getAltitude()).thenReturn(FIRST_ALTITUDE);
		
		second = mock(MeasuredTrackPoint.class);
		when(second.getTime()).thenReturn(SECOND_TIME);
		when(second.getDistance()).thenReturn(SECOND_DISTANCE);
		when(second.getAltitude()).thenReturn(SECOND_ALTITUDE);
		
		nonStartTrackPoint = new NonStartTrackPoint(first, second);
	}

	/**
	 * Test speed for second trackpoint.
	 */
	@Test
	public void testSpeedInSecondSample() {
		Duration duration = new Duration(START_TIME, SECOND_TIME);
		Length distance = SECOND_DISTANCE;
		Speed speed = Speed.createSpeedInMetersPerSecond(distance, duration);

		assertEquals(speed, nonStartTrackPoint.getSpeed());
		
		verify(first, times(1)).getDistance();
		verify(first, times(1)).getTime();
		verify(second, times(1)).getDistance();
		verify(second, times(1)).getTime();
	}
	
	@Test(expected=NullPointerException.class)
	public void constructorFailsWithNullPrevious() {
		new NonStartTrackPoint(null, second);
	}
	
	@Test
	public void testGetAltitudeDelta() {
		Length gain = SECOND_ALTITUDE.minus(FIRST_ALTITUDE);
		assertEquals(gain, nonStartTrackPoint.getAltitudeDelta());
	}
	
	@Test
	public void testGetAltitudeDeltaWithPreviousNull() {
		first = mock(MeasuredTrackPoint.class);
		when(first.getTime()).thenReturn(START_TIME);
		when(first.getDistance()).thenReturn(Length.createLengthInMeters(0.0));
		when(first.getAltitude()).thenReturn(null);
		
		nonStartTrackPoint = new NonStartTrackPoint(first, second);
		
		Length noGain = Length.createLengthInMeters(0.0);
		assertEquals(noGain, nonStartTrackPoint.getAltitudeDelta());
	}
}
