package nl.iljabooij.garmintrainer.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import nl.iljabooij.garmintrainer.model.Speed.Unit;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;

public class InterpolatedTrackPointTest {
	private static final double DELTA = 0.0001;
	private InterpolatedTrackPoint interpolatedTrackPoint;
	private TrackPoint begin;
	private TrackPoint end;
	
	private static final DateTime BEGIN_TIME = new DateTime();
	// second track point recorded 10 seconds after first.
	private static final DateTime END_TIME = BEGIN_TIME.plusSeconds(10);
	
	// interpolated track point 3.5 seconds after first track point.
	private static final DateTime MIDDLE_TIME = BEGIN_TIME.plusMillis(3500);
	
	@Before
	public void setUp() throws Exception {
		begin = mock(TrackPoint.class);
		end = mock(TrackPoint.class);
		
		when(begin.getTime()).thenReturn(BEGIN_TIME);
		when(end.getTime()).thenReturn(END_TIME);
		
		interpolatedTrackPoint = new InterpolatedTrackPoint(MIDDLE_TIME, begin, end);
	}

	@Test(expected=NullPointerException.class)
	public void testConstructorDoesNotAcceptNullTime() {
		new InterpolatedTrackPoint(null, begin, end);
	}
	
	@Test(expected=NullPointerException.class)
	public void testConstructorDoesNotAcceptNullBegin() {
		new InterpolatedTrackPoint(MIDDLE_TIME, null, end);
	}
	
	@Test(expected=NullPointerException.class)
	public void testConstructorDoesNotAcceptNullEnd() {
		new InterpolatedTrackPoint(MIDDLE_TIME, begin, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorDoesNotAcceptMiddleTimeBeforeBegin() {
		new InterpolatedTrackPoint(BEGIN_TIME.minusSeconds(10), begin, end);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorDoesNotAcceptMiddleTimeAfterEnd() {
		new InterpolatedTrackPoint(END_TIME.plusSeconds(10), begin, end);
	}
	
	/**
	 * It should be possible that the time of the interpolated track point is
	 * exactly the time of the begin point
	 */
	@Test
	public void testIfMiddleTimeCanBeExactlyTheBeginTime() {
		try {
			new InterpolatedTrackPoint(BEGIN_TIME, begin, end);
		} catch (IllegalArgumentException e) {
			fail("Illegal argument exception should not have been thrown");
		}
	}
	
	/**
	 * It should not be possible that the time of the interpolated track point is
	 * exactly that of the end point.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void assureThatTimeCannotBeThatOfEndPoint() {
		new InterpolatedTrackPoint(END_TIME, begin, end);
	}
	
	@Test
	public void testGetBegin() {
		assertEquals(begin, interpolatedTrackPoint.getBegin());
	}
	
	@Test
	public void testGetEnd() {
		assertEquals(end, interpolatedTrackPoint.getEnd());
	}
	
	@Test
	public void testGetAltitude() {
		final Length beginAltitude = Length.createLengthInMeters(0.0);
		when(begin.getAltitude()).thenReturn(beginAltitude);
		final Length endAltitude = Length.createLengthInMeters(10.0);
		when(end.getAltitude()).thenReturn(endAltitude);
		
		// altitude at middle point should be 3.5m
		final Length middleAltitude = Length.createLengthInMeters(3.5);
		assertEquals(middleAltitude, interpolatedTrackPoint.getAltitude());
	}

	@Test
	public void testGetAltitudeDelta() {
		final Length beginAltitude = Length.createLengthInMeters(1.0);
		when(begin.getAltitude()).thenReturn(beginAltitude);
		final Length endAltitude = Length.createLengthInMeters(11.0);
		when(end.getAltitude()).thenReturn(endAltitude);
		
		// this doesn't really have any use. Still, just check it.
		final Length altitudeDelta = Length.createLengthInMeters(3.5);
		
		assertEquals(altitudeDelta, interpolatedTrackPoint.getAltitudeDelta());
	}

	@Test
	public void testGetDistance() {
		final Length beginDistance = Length.createLengthInMeters(0.0);
		when(begin.getDistance()).thenReturn(beginDistance);
		final Length endDistance = Length.createLengthInMeters(10.0);
		when(end.getDistance()).thenReturn(endDistance);
		
		// altitude at middle point should be 3.5m
		final Length middleDistance = Length.createLengthInMeters(3.5);
		assertEquals(middleDistance, interpolatedTrackPoint.getDistance());
	}

	@Test
	public void testGetHeartRate() {
		when(begin.getHeartRate()).thenReturn(120);
		when(end.getHeartRate()).thenReturn(140);
		
		assertEquals(127, interpolatedTrackPoint.getHeartRate());
	}

	@Test
	public void testGetLatitude() {
		when(begin.hasPosition()).thenReturn(true);
		when(end.hasPosition()).thenReturn(true);
		when(begin.getLatitude()).thenReturn(4.0010);
		when(end.getLatitude()).thenReturn(4.0020);
		
		assertEquals(4.00135, interpolatedTrackPoint.getLatitude(), DELTA);
	}
	
	@Test
	public void testGetLatitudeWithNoBeginPosition() {
		when(begin.hasPosition()).thenReturn(false);
		when(end.hasPosition()).thenReturn(true);
		when(begin.getLatitude()).thenReturn(0.0);
		when(end.getLatitude()).thenReturn(4.0020);
		
		assertEquals(4.002, interpolatedTrackPoint.getLatitude(), DELTA);
	}

	@Test
	public void testGetLatitudeWithNoEndPosition() {
		when(begin.hasPosition()).thenReturn(true);
		when(end.hasPosition()).thenReturn(false);
		when(begin.getLatitude()).thenReturn(4.0010);
		when(end.getLatitude()).thenReturn(0.0);
		
		assertEquals(4.001, interpolatedTrackPoint.getLatitude(), DELTA);
	}
	
	@Test
	public void testGetLatitudeWithNoPositions() {
		when(begin.hasPosition()).thenReturn(false);
		when(end.hasPosition()).thenReturn(false);
		when(begin.getLatitude()).thenReturn(0.0);
		when(end.getLatitude()).thenReturn(0.0);
		
		assertEquals(0.0, interpolatedTrackPoint.getLatitude(), DELTA);
	}
	
	@Test
	public void testGetLongitude() {
		when(begin.hasPosition()).thenReturn(true);
		when(end.hasPosition()).thenReturn(true);
		when(begin.getLatitude()).thenReturn(4.0010);
		when(end.getLatitude()).thenReturn(4.0020);
		
		assertEquals(4.00135, interpolatedTrackPoint.getLatitude(), DELTA);
	}

	@Test
	public void testGetLongitudeWithNoBeginPosition() {
		when(begin.hasPosition()).thenReturn(false);
		when(end.hasPosition()).thenReturn(true);
		when(begin.getLongitude()).thenReturn(0.0);
		when(end.getLongitude()).thenReturn(4.0020);
		
		assertEquals(4.002, interpolatedTrackPoint.getLongitude(), DELTA);
	}

	@Test
	public void testGetLongitudeWithNoEndPosition() {
		when(begin.hasPosition()).thenReturn(true);
		when(end.hasPosition()).thenReturn(false);
		when(begin.getLongitude()).thenReturn(4.0010);
		when(end.getLongitude()).thenReturn(0.0);
		
		assertEquals(4.001, interpolatedTrackPoint.getLongitude(), DELTA);
	}
	
	@Test
	public void testGetLongitudeWithNoPositions() {
		when(begin.hasPosition()).thenReturn(false);
		when(end.hasPosition()).thenReturn(false);
		when(begin.getLongitude()).thenReturn(0.0);
		when(end.getLongitude()).thenReturn(0.0);
		
		assertEquals(0.0, interpolatedTrackPoint.getLongitude(), DELTA);
	}
	
	@Test
	public void testGetSpeed() {
		final Length beginDistance = Length.createLengthInMeters(0.0);
		when(begin.getDistance()).thenReturn(beginDistance);
		final Length endDistance = Length.createLengthInMeters(10.0);
		when(end.getDistance()).thenReturn(endDistance);
		
		Speed speed = Speed.createSpeed(endDistance.minus(beginDistance), 
				new Duration(BEGIN_TIME, END_TIME), Unit.KilometersPerHour);
		assertEquals(speed, interpolatedTrackPoint.getSpeed());
	}

	@Test
	public void testGetTime() {
		assertEquals(MIDDLE_TIME, interpolatedTrackPoint.getTime());
	}

	@Test
	public void testHasPositionWithSurroundingTrackPointsHavingPosition() {
		when(begin.hasPosition()).thenReturn(true);
		when(end.hasPosition()).thenReturn(true);
		
		assertTrue(interpolatedTrackPoint.hasPosition());
	}
	
	@Test
	public void testHasPositionWithNoBeginPosition() {
		when(begin.hasPosition()).thenReturn(false);
		when(end.hasPosition()).thenReturn(true);
		
		assertTrue(interpolatedTrackPoint.hasPosition());
	}
	
	@Test
	public void testHasPositionWithNoEndPosition() {
		when(begin.hasPosition()).thenReturn(true);
		when(end.hasPosition()).thenReturn(false);
		
		assertTrue(interpolatedTrackPoint.hasPosition());
	}

	@Test
	public void testHasPositionWithNoBeginOrEndPosition() {
		when(begin.hasPosition()).thenReturn(false);
		when(end.hasPosition()).thenReturn(false);
		
		assertFalse(interpolatedTrackPoint.hasPosition());
	}
}
