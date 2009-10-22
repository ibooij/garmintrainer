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
package nl.iljabooij.garmintrainer.parser.digester;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import nl.iljabooij.garmintrainer.model.Length;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class DigesterMeasuredTrackPointTest {
	private static final double DELTA_FOR_DOUBLE_EQUALITY = 0.0001;
	private TrackPointType trackPoint;
	private DigesterMeasuredTrackPoint dmtp;
	
	@Before
	public void setUp() throws Exception {
		trackPoint = mock(TrackPointType.class);
		dmtp = new DigesterMeasuredTrackPoint(trackPoint);
	}

	@Test(expected=NullPointerException.class)
	public void testConstructorDoesNotAcceptNullDelegate() {
		new DigesterMeasuredTrackPoint(null);
	}
	
	@Test
	public void testGetAltitude() {
		Length altitude = Length.createLengthInMeters(100.0);
		when(trackPoint.getAltitude()).thenReturn(altitude);
		
		assertEquals(altitude, dmtp.getAltitude());
		verify(trackPoint, times(1)).getAltitude();
	}

	@Test
	public void testGetDistance() {
		Length distance = Length.createLengthInMeters(100.0);
		when(trackPoint.getDistance()).thenReturn(distance);
		
		assertEquals(distance, dmtp.getDistance());
		verify(trackPoint, times(1)).getDistance();
	}

	@Test
	public void testGetHeartRate() {
		int heartRate = 127;
		when(trackPoint.getHeartRate()).thenReturn(heartRate);
		
		assertEquals(heartRate, dmtp.getHeartRate());
		verify(trackPoint, times(1)).getHeartRate();
	}

	@Test
	public void testGetLatitude() {
		double latitude = 4.213; // just some latitude
		when(trackPoint.getLatitude()).thenReturn(latitude);
		
		assertEquals(latitude, dmtp.getLatitude(), DELTA_FOR_DOUBLE_EQUALITY);
		verify(trackPoint, times(1)).getLatitude();
	}

	@Test
	public void testGetLongitude() {
		double longitude = 14.213; // just some longitude
		when(trackPoint.getLongitude()).thenReturn(longitude);
		
		assertEquals(longitude, dmtp.getLongitude(), DELTA_FOR_DOUBLE_EQUALITY);
		verify(trackPoint, times(1)).getLongitude();
	}

	@Test
	public void testGetTime() {
		DateTime time = new DateTime();
		when(trackPoint.getTime()).thenReturn(time);
		
		assertEquals(time, dmtp.getTime());
		verify(trackPoint, times(1)).getTime();
	}

}
