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

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;

/**
 * Tests for {@link Track}.
 * @author ilja
 *
 */
public class TrackTest {
	private Track testTrack;
	
	private LinkedList<TrackPoint> trackPoints;
	private static final int NR_OF_TRACK_POINTS = 100;
	private static final DateTime START_TIME = new DateTime();
	
	@Before
	public void setUpAll() {
		trackPoints = Lists.newLinkedList();
		for (int i = 0; i < NR_OF_TRACK_POINTS; i++) {
			TrackPoint trackPoint = Mockito.mock(TrackPoint.class);
			Mockito.when(trackPoint.getTime()).thenReturn(START_TIME.plusSeconds(i));
			
			trackPoints.add(trackPoint);
		}
		testTrack = new Track(trackPoints);
	}
	
	@Test
	public void testGetStartTime() {
		assertEquals(START_TIME, testTrack.getStartTime());
	}
	
	@Test
	public void testGetEndTime() {
		assertEquals(trackPoints.getLast().getTime(), testTrack.getEndTime());
	}
	
	@Test
	public void testGetDuration() {
		assertEquals(new Duration(START_TIME, trackPoints.getLast().getTime()), 
				testTrack.getDuration());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testImpossibleToCreateEmptyTrack() {
		List<TrackPoint> emptyList = Lists.newArrayList();
		new Track(emptyList);
	}
	
	@Test(expected=NullPointerException.class)
	public void testImpossibleToCreateWithNullList() {
		new Track(null);
	}
}
