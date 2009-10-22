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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.LinkedList;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class LapTest {
	private Lap testLap;
	private LinkedList<Track> tracks;
	private LinkedList<TrackPointImpl> allTrackPoints;

	private static final DateTime START_TIME = new DateTime();
	private static final int NR_OF_TRACK_POINTS = 100;
	private static final int NR_OF_TRACKS = 3;

	@Before
	public void setUp() {
		tracks = Lists.newLinkedList();
		allTrackPoints = Lists.newLinkedList();

		for (int trackNr = 0; trackNr < NR_OF_TRACKS; trackNr++) {
			Track track = mock(Track.class);
			
			LinkedList<TrackPointImpl> trackPoints = Lists.newLinkedList();
			for (int i = trackNr * NR_OF_TRACK_POINTS; i < (trackNr + 1) * NR_OF_TRACK_POINTS; i++) {
				TrackPointImpl trackPoint = mock(TrackPointImpl.class);
				when(trackPoint.getTime()).thenReturn(
						START_TIME.plusSeconds(i + 1));
				trackPoints.add(trackPoint);
			}
			allTrackPoints.addAll(trackPoints);
			when(track.getTrackPoints()).thenReturn(ImmutableList.copyOf(trackPoints));
			DateTime startTime = trackPoints.getFirst().getTime();
			DateTime endTime = trackPoints.getLast().getTime();
			when(track.getStartTime()).thenReturn(startTime);
			when(track.getEndTime()).thenReturn(endTime);
			tracks.add(track);
		}
		testLap = new Lap(START_TIME, tracks);
	}

	@Test
	public void testGetStartTime() {
		assertEquals(START_TIME, testLap.getStartTime());
	}

	@Test
	public void testGetEndTime() {
		assertEquals(allTrackPoints.getLast().getTime(), testLap.getEndTime());
	}

	@Test
	public void testGetTrackPoints() {
		assertEquals(allTrackPoints, testLap.getTrackPoints());
	}

	/**
	 * Test if the Lap class can find pauses between different tracks in the lap.
	 */
	@Test
	public void testGetPauses() {
		// three tracks, with pauses in between
		Track[] tracks = new Track[3];
		tracks[0] = mock(Track.class);
		tracks[1] = mock(Track.class);
		tracks[2] = mock(Track.class);
		
		when(tracks[0].getStartTime()).thenReturn(START_TIME);
		when(tracks[0].getEndTime()).thenReturn(START_TIME.plusMinutes(1));
		when(tracks[1].getStartTime()).thenReturn(START_TIME.plusMinutes(2));
		when(tracks[1].getEndTime()).thenReturn(START_TIME.plusMinutes(3));
		when(tracks[2].getStartTime()).thenReturn(START_TIME.plusMinutes(4));
		when(tracks[2].getEndTime()).thenReturn(START_TIME.plusMinutes(5));
	
		Pause[] pauses = new Pause[2];
		pauses[0] = new Pause(tracks[0].getEndTime(), tracks[1].getStartTime());
		pauses[1] = new Pause(tracks[1].getEndTime(), tracks[2].getStartTime());
		
		Lap lap = new Lap(START_TIME, Arrays.asList(tracks));
		
		assertEquals(Arrays.asList(pauses), lap.getPauses());
	}
	
	@Test
	public void testGetGrossDuration() {
		// three tracks, with pauses in between
		Track[] tracks = new Track[3];
		tracks[0] = mock(Track.class);
		tracks[1] = mock(Track.class);
		tracks[2] = mock(Track.class);
		
		when(tracks[0].getStartTime()).thenReturn(START_TIME);
		when(tracks[0].getEndTime()).thenReturn(START_TIME.plusMinutes(1));
		when(tracks[1].getStartTime()).thenReturn(START_TIME.plusMinutes(2));
		when(tracks[1].getEndTime()).thenReturn(START_TIME.plusMinutes(3));
		when(tracks[2].getStartTime()).thenReturn(START_TIME.plusMinutes(4));
		when(tracks[2].getEndTime()).thenReturn(START_TIME.plusMinutes(5));
		
		Lap lap = new Lap(START_TIME, Arrays.asList(tracks));
		
		assertEquals(new Duration(tracks[0].getStartTime(), tracks[2].getEndTime()),
				lap.getGrossDuration());	
	}
	
	@Test
	public void testGetNetDuration() {
		// three tracks, with pauses in between
		Track[] tracks = new Track[3];
		tracks[0] = mock(Track.class);
		tracks[1] = mock(Track.class);
		tracks[2] = mock(Track.class);
		
		when(tracks[0].getStartTime()).thenReturn(START_TIME);
		when(tracks[0].getEndTime()).thenReturn(START_TIME.plusMinutes(1));
		when(tracks[1].getStartTime()).thenReturn(START_TIME.plusMinutes(2));
		when(tracks[1].getEndTime()).thenReturn(START_TIME.plusMinutes(3));
		when(tracks[2].getStartTime()).thenReturn(START_TIME.plusMinutes(4));
		when(tracks[2].getEndTime()).thenReturn(START_TIME.plusMinutes(5));
		
		for (Track track: tracks) {
			when(track.getDuration()).thenReturn(new Duration(60000));
		}
		
		Lap lap = new Lap(START_TIME, Arrays.asList(tracks));
		Duration netDuration = new Duration(0);
		for (Track track: tracks) {
			netDuration = netDuration.plus(new Duration(track.getStartTime(), track.getEndTime()));
		}
		assertEquals(netDuration,
				lap.getNetDuration());	
	}
	
	/**
	 * Lap start time and start time of first track may be different (first track may
	 * start a little after the lap. Make sure that {@link Lap#getNetDuration()}
	 * returns the correct value.
	 */
	@Test
	public void testGetNetDurationWithDelayedRecording() {
		final DateTime startOfLap = new DateTime();
		// track starts 5 seconds after lap
		final Duration trackDelay = Duration.standardSeconds(5);
		final DateTime startOfTrack = startOfLap.plus(trackDelay);
		
		final Duration trackDuration = Duration.standardMinutes(5);
		
		Track track = mock(Track.class);
		when(track.getStartTime()).thenReturn(startOfTrack);
		when(track.getDuration()).thenReturn(trackDuration);
		
		Lap lap = new Lap(startOfLap, Lists.newArrayList(track));
		
		assertEquals(trackDelay.plus(trackDuration), lap.getNetDuration());
	}
}
