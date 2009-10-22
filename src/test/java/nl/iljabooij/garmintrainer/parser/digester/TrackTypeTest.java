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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * Test Case for {@link TrackType}.
 * @author ilja
 *
 */
public class TrackTypeTest {
	LinkedList<TrackPointType> trackPointTypes;

	private static final int NR_OF_TRACK_POINTS = 100;
	private static final DateTime START_OF_TRACK = new DateTime(2009, 9, 24, 9, 11, 0, 0);
	
	private TrackType testTrackType;
	
	@Before
	public void setUp() {
		trackPointTypes = Lists.newLinkedList();
		for(int i = 0; i < NR_OF_TRACK_POINTS; i++) {
			TrackPointType trackPointType = mock(TrackPointType.class);
			when(trackPointType.getTime()).thenReturn(START_OF_TRACK.plusSeconds(i));
			trackPointTypes.add(trackPointType);
			
		}
		
		testTrackType = new TrackType();
		for (TrackPointType trackPointType: trackPointTypes) {
			testTrackType.addTrackPoint(trackPointType);
		}
	}
	
	@Test
	public void testAddAndGetTrackPoints() {
		TrackType trackType = new TrackType();
		for (TrackPointType trackPointType: trackPointTypes) {
			trackType.addTrackPoint(trackPointType);
		}
		
		assertEquals(trackPointTypes, trackType.getTrackPointTypes());
	}

	/**
	 * Test if the start time is returned correctly.
	 */
	@Test
	public void testGetStartTime() {
		assertEquals("start time equals time of first track point",
				trackPointTypes.getFirst().getTime(), testTrackType.getStartTime());
	}
	
	/**
	 * Test if the end time is returned correctly.
	 */
	@Test
	public void testGetEndTime() {
		assertEquals("end time equals time of last track point",
				trackPointTypes.getLast().getTime(), testTrackType.getEndTime());
	}
}
