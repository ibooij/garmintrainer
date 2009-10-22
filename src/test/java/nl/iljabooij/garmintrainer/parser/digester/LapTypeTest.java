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

import java.util.LinkedList;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Test cases for {@link LapType}.
 * @author ilja
 *
 */
public class LapTypeTest {
	private LapType testLapType;
	private LinkedList<TrackType> tracks;
	
	private static final DateTime START_TIME = new DateTime();
	private static final Duration[] START_INTERVALS = {
		Duration.standardSeconds(1),
		Duration.standardSeconds(1),
		Duration.standardSeconds(1),
		Duration.standardSeconds(1),
		Duration.standardSeconds(1)
	};
	private static final Duration[] END_INTERVALS = {
		Duration.standardSeconds(10),
		Duration.standardSeconds(120),
		Duration.standardSeconds(10),
		Duration.standardSeconds(120),
		Duration.standardSeconds(10)
	};
	
	private static final int NR_OF_TRACKS = 5;
	
	/**
	 * set up tests
	 */
	@Before
	public void setUp() {
		testLapType = new LapType();
		testLapType.setStartTime(START_TIME);
		tracks = Lists.newLinkedList();
		
		DateTime startTime = START_TIME;
		DateTime endTime = START_TIME;
		for (int i = 0; i < NR_OF_TRACKS; i++) {
			TrackType trackType = mock(TrackType.class);
			
			startTime = endTime.plus(START_INTERVALS[i]);
			when(trackType.getStartTime()).thenReturn(startTime);
			endTime = startTime.plus(END_INTERVALS[i]);
			when(trackType.getEndTime()).thenReturn(endTime);
		
			tracks.add(trackType);
			testLapType.addTrack(trackType);
		}
	}
	
	/**
	 * Test addition and getting of tracks
	 */
	@Test
	public void testAddAndGetTracks() {
		assertEquals(tracks, testLapType.getTracks());
	}
	
	/**
	 * Test getStartTime()
	 */ 
	@Test
	public void testGetStartTime() {
		assertEquals(START_TIME, testLapType.getStartTime());
	}
	
	/**
	 * Test getEndTime()
	 */ 
	@Test
	public void testGetEndTime() {
		assertEquals(tracks.getLast().getEndTime(), testLapType.getEndTime());
	}
	
}
