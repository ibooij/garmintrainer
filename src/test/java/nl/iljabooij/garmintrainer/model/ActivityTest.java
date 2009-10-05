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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class ActivityTest {
	private ActivityImpl activity;
	private LinkedList<Lap> laps;
	private LinkedList<TrackPoint> allTrackPoints;
	private static final DateTime START_TIME = new DateTime(2009, 9, 17, 14,
			45, 36, 0);

	private static final int NR_OF_LAPS = 4;
	private static final int SECONDS_PER_LAP = 1000;
	private static final double[] distances = { 100.0, 200.0, 300.0, 400.0 };
	private static final int SECONDS_PER_POINT = SECONDS_PER_LAP
			/ distances.length;

	@Before
	public void setUp() {
		laps = Lists.newLinkedList();
		allTrackPoints = Lists.newLinkedList();
		for (int lapNr = 0; lapNr < NR_OF_LAPS; lapNr++) {
			Lap lap = mock(Lap.class);

			// track points
			Length lapStartDistance = Length
					.createLengthInMeters(distances[distances.length - 1]
							* lapNr);
			DateTime lapStartTime = START_TIME.plusSeconds(SECONDS_PER_LAP
					* lapNr);
			when(lap.getStartTime()).thenReturn(lapStartTime);
			ImmutableList.Builder<TrackPoint> builder = ImmutableList.builder();
			for (int i = 0; i < distances.length; i++) {
				TrackPoint trackPoint = mock(TrackPoint.class);
				Length tpDistance = Length.createLengthInMeters(distances[i]);
				when(trackPoint.getDistance()).thenReturn(
						lapStartDistance.plus(tpDistance));
				when(trackPoint.getTime()).thenReturn(
						lapStartTime.plusSeconds(i * SECONDS_PER_POINT));
				builder.add(trackPoint);
			}
			when(lap.getTrackPoints()).thenReturn(builder.build());
			allTrackPoints.addAll(builder.build());
			laps.add(lap);
		}
		activity = new ActivityImpl(START_TIME, laps);
	}

	@Test
	public void testGetGrossDuration() {
		Duration grossDuration = new Duration(START_TIME, allTrackPoints
				.getLast().getTime());
		assertEquals(grossDuration, activity.getGrossDuration());
	}

	/**
	 * Test if the net duration (riding time, total time minus pauses) of an
	 * Activity is calculated correctly.
	 */
	@Test
	public void testGetNetDuration() {
		/** construct some laps that have some track points */
		LinkedList<Lap> laps = Lists.newLinkedList();
		final DateTime startTime = new DateTime();
		// 10 minutes net riding time per lap
		final Duration netDurationPerLap = Duration.standardMinutes(10);
		// 11 minutes gross riding time per lap
		final Duration grossDurationPerLap = Duration.standardMinutes(11);

		// first lap starts 1 second after activity starts!
		DateTime lapStartTime = startTime.plus(Duration.standardSeconds(1));
		// 10 laps
		for (int i = 0; i < 10; i++) {
			if (i > 0) {
				lapStartTime = lapStartTime.plus(grossDurationPerLap);
			}
			Lap lap = mock(Lap.class);
			when(lap.getStartTime()).thenReturn(lapStartTime);
			when(lap.getNetDuration()).thenReturn(netDurationPerLap);
			// two track points per lap
			TrackPoint tp1 = mock(TrackPoint.class);
			when(tp1.getTime()).thenReturn(lapStartTime);
			TrackPoint tp2 = mock(TrackPoint.class);
			when(tp2.getTime()).thenReturn(
					lapStartTime.plus(grossDurationPerLap));
			when(lap.getTrackPoints()).thenReturn(ImmutableList.of(tp1, tp2));

			laps.add(lap);
		}
		Activity activity = new ActivityImpl(startTime, laps);

		// we have 10 laps of 10 minutes riding time per lap, so there should be
		// 100 minutes of riding time, plus the first second (difference between
		// Activity start time and the start of the first lap
		assertEquals(Duration.standardMinutes(100).plus(
				Duration.standardSeconds(1)), activity.getNetDuration());
	}

	/**
	 * We'll just test if toString() returns anything..
	 */
	@Test
	public void testToString() {
		assertFalse(activity.toString().isEmpty());
	}

	/**
	 * Simple tests for equals
	 */
	@Test
	public void testEquals() {
		assertFalse("not equal to null", activity.equals(null));
		assertFalse("not equal to unrelated Object", activity
				.equals(new Object()));
		assertTrue("equal to itself", activity.equals(activity));

		/* same content, different object */
		assertTrue("equal to same object", activity.equals(new ActivityImpl(
				START_TIME, laps)));

		/* differently filled objects, should all differ */
		assertFalse("different date time", activity.equals(new ActivityImpl(
				START_TIME.plusMinutes(1), laps)));
		List<Lap> oneLess = laps.subList(0, 2);
		assertFalse("different laps", activity.equals(new ActivityImpl(START_TIME,
				oneLess)));
	}

	/**
	 * 
	 */
	@Test
	public void testGetTrackPoints() {
		List<TrackPoint> trackPoints = Lists.newArrayList();
		for (Lap lap : laps) {
			trackPoints.addAll(lap.getTrackPoints());
		}
		assertEquals(trackPoints, activity.getTrackPoints());
	}

	@Test
	public void testGetStartTime() {
		assertEquals(START_TIME, activity.getStartTime());
	}

	@Test
	public void testGetEndTime() {
		Lap lastLap = laps.getLast();
		LinkedList<TrackPoint> trackPoints = Lists.newLinkedList(lastLap
				.getTrackPoints());

		assertEquals(trackPoints.getLast().getTime(), activity.getEndTime());
	}

	@Test
	public void testGetDistance() {
		Length distance = Length.createLengthInMeters(NR_OF_LAPS
				* distances[distances.length - 1]);
		assertEquals("lengths equal", distance, activity.getDistance());
	}

	@Test
	public void testGetLaps() {
		assertEquals(laps, activity.getLaps());
	}

	@Test(expected = NullPointerException.class)
	public void testNullStartTimeShouldCauseNPE() {
		new ActivityImpl(null, laps);
	}

	@Test(expected = NullPointerException.class)
	public void testNullLapsShouldCauseNPE() {
		new ActivityImpl(START_TIME, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyLapsShouldCouseIAE() {
		new ActivityImpl(START_TIME, new ArrayList<Lap>());
	}

	@Test
	public void testGetAltitudeGain() {
		// first half of points in lap go up by 2.0 meters, other half go down
		final Length gain = Length.createLengthInMeters(3.0);
		final Length loss = Length.createLengthInMeters(-3.0);

		Lap[] laps = new Lap[2];
		for (int i = 0; i < laps.length; i++) {
			laps[i] = mock(Lap.class);

			TrackPoint[] trackPoints = new TrackPoint[10];
			for (int j = 0; j < trackPoints.length; j++) {
				trackPoints[j] = mock(TrackPoint.class);
				if (j < 5) {
					when(trackPoints[j].getAltitudeDelta()).thenReturn(gain);
				} else {
					when(trackPoints[j].getAltitudeDelta()).thenReturn(loss);
				}
			}
			when(laps[i].getTrackPoints()).thenReturn(
					ImmutableList.copyOf(Arrays.asList(trackPoints)));
		}

		Activity activity = new ActivityImpl(START_TIME, Arrays.asList(laps));

		// test gain
		double totalGain = laps.length * (laps[0].getTrackPoints().size() / 2)
				* gain.getValueInMeters();
		assertEquals(Length.createLengthInMeters(totalGain), activity
				.getAltitudeGain());
	}

	/**
	 * Test which checks if altitude gain correctly filters out any noise.
	 */
	@Test
	public void testFilterAltitudeDeltaNoise() {
		// values go up and down every trackpoint.
		final Length gain = Length.createLengthInMeters(4.0);
		final Length loss = Length.createLengthInMeters(-4.0);

		Lap[] laps = new Lap[1];
		for (int i = 0; i < laps.length; i++) {
			laps[i] = mock(Lap.class);

			TrackPoint[] trackPoints = new TrackPoint[10];
			for (int j = 0; j < trackPoints.length; j++) {
				trackPoints[j] = mock(TrackPoint.class);
				Length delta = (j % 2 == 0) ? gain : loss;
				when(trackPoints[j].getAltitudeDelta()).thenReturn(delta);
			}
			when(laps[i].getTrackPoints()).thenReturn(
					ImmutableList.copyOf(Arrays.asList(trackPoints)));
		}

		Activity activity = new ActivityImpl(START_TIME, Arrays.asList(laps));

		assertEquals(Length.createLengthInMeters(0.0), activity
				.getAltitudeGain());
	}

	@Test
	public void testGetMaxAndMinAltitude() {
		final double[] altitudes = new double[] { 1.0, 2.0, 3.0, 2.0 };

		Lap[] laps = new Lap[1];
		for (int i = 0; i < laps.length; i++) {
			laps[i] = mock(Lap.class);

			TrackPoint[] trackPoints = new TrackPoint[altitudes.length];
			for (int j = 0; j < trackPoints.length; j++) {
				trackPoints[j] = mock(TrackPoint.class);
				when(trackPoints[j].getAltitude()).thenReturn(
						Length.createLengthInMeters(altitudes[j]));
			}
			when(laps[i].getTrackPoints()).thenReturn(
					ImmutableList.copyOf(Arrays.asList(trackPoints)));
		}

		Activity activity = new ActivityImpl(START_TIME, Arrays.asList(laps));

		assertEquals(Length.createLengthInMeters(3.0), activity
				.getMaximumAltitude());
		assertEquals(Length.createLengthInMeters(1.0), activity
				.getMinimumAltitude());
	}

	/**
	 * test the getMaximumSpeed() method
	 */
	@Test
	public void testGetMaximumSpeed() {
		final double[] speeds = new double[] { 1.0, 2.0, 3.0, 2.0 };

		Lap[] laps = new Lap[1];
		for (int i = 0; i < laps.length; i++) {
			laps[i] = mock(Lap.class);

			TrackPoint[] trackPoints = new TrackPoint[speeds.length];
			for (int j = 0; j < trackPoints.length; j++) {
				trackPoints[j] = mock(TrackPoint.class);
				when(trackPoints[j].getSpeed()).thenReturn(
						Speed.createExactSpeedInMetersPerSecond(speeds[j]));
			}
			when(laps[i].getTrackPoints()).thenReturn(
					ImmutableList.copyOf(Arrays.asList(trackPoints)));
		}

		Activity activity = new ActivityImpl(START_TIME, Arrays.asList(laps));

		assertEquals(Speed.createExactSpeedInMetersPerSecond(3.0), activity
				.getMaximumSpeed());
	}
}
