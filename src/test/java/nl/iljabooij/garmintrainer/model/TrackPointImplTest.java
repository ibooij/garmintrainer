package nl.iljabooij.garmintrainer.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class TrackPointImplTest {
	private static final double DELTA_FOR_DOUBLE_EQUALITY = 0.0001;
	
	private TrackPointImpl trackPointImpl;
	private MeasuredTrackPoint previousMeasuredTrackPoint;
	private MeasuredTrackPoint measuredTrackPoint;

	/**
	 * This is a concrete version of {@link TrackPointImpl}. We need this
	 * because {@link TrackPointImpl} is abstract, but we still want to 
	 * test it. It contains dummy implementations of the abstract methods
	 * of {@link TrackPointImpl}, plus a constructor that just calls the
	 * super() constructor with it's argument.
	 * @author ilja
	 *
	 */
	private class ConcreteTrackPointImpl extends TrackPointImpl {
		ConcreteTrackPointImpl(final MeasuredTrackPoint measuredTrackPoint) {
			super(measuredTrackPoint);
		}
		@Override
		public Length getAltitudeDelta() {
			return null;
		}

		@Override
		public Speed getSpeed() {
			return null;
		}
		
	}
	
	@Before
	public void setUp() throws Exception {
		// TrackPointImpl is abstract, so we need to create a subclass of
		// measured track point to test with.
		measuredTrackPoint = mock(MeasuredTrackPoint.class);
		trackPointImpl = new ConcreteTrackPointImpl(measuredTrackPoint);
	}

	@Test(expected=NullPointerException.class)
	public void testConstructorFailsWithNullMeasuredTrackPoint() {
		// Because TrackPointImpl is abstract we cannot call it's constructor
		// directly. We'll take the route through NonStartTrackPoint.
		new ConcreteTrackPointImpl(null);
	}
	
	@Test
	public void testToString() {
		String verificationString = "OK";
		when(trackPointImpl.toString()).thenReturn(verificationString);
		String out = trackPointImpl.toString();
		
		// there isn't a real way
		assertTrue(verificationString.equals(out));
	}

	@Test
	public void testGetAltitude() {
		Length altitude = Length.createLengthInMeters(100.0);
		when(measuredTrackPoint.getAltitude()).thenReturn(altitude);
		
		assertEquals(altitude, trackPointImpl.getAltitude());
		verify(measuredTrackPoint, times(1)).getAltitude();
	}

	@Test
	public void testGetDistance() {
		Length distance = Length.createLengthInMeters(100.0);
		when(measuredTrackPoint.getDistance()).thenReturn(distance);
		
		assertEquals(distance, trackPointImpl.getDistance());
		verify(measuredTrackPoint, times(1)).getDistance();
	}

	@Test
	public void testGetHeartRate() {
		int heartRate = 127;
		when(measuredTrackPoint.getHeartRate()).thenReturn(heartRate);
		
		assertEquals(heartRate, trackPointImpl.getHeartRate());
		verify(measuredTrackPoint, times(1)).getHeartRate();
	}

	@Test
	public void testGetLatitude() {
		double latitude = 4.213; // just some latitude
		when(measuredTrackPoint.getLatitude()).thenReturn(latitude);
		
		assertEquals(latitude, trackPointImpl.getLatitude(), DELTA_FOR_DOUBLE_EQUALITY);
		verify(measuredTrackPoint, times(1)).getLatitude();
	}

	@Test
	public void testGetLongitude() {
		double longitude = 14.213; // just some longitude
		when(measuredTrackPoint.getLongitude()).thenReturn(longitude);
		
		assertEquals(longitude, trackPointImpl.getLongitude(), DELTA_FOR_DOUBLE_EQUALITY);
		verify(measuredTrackPoint, times(1)).getLongitude();
	}

	@Test
	public void testGetTime() {
		DateTime time = new DateTime();
		when(measuredTrackPoint.getTime()).thenReturn(time);
		
		assertEquals(time, trackPointImpl.getTime());
		verify(measuredTrackPoint, times(1)).getTime();
	}
	
	@Test
	public void testHasPosition() {
		double latitude = 0.0; // just some latitude
		when(measuredTrackPoint.getLatitude()).thenReturn(latitude);
		double longitude = 0.0; // just some longitude
		when(measuredTrackPoint.getLongitude()).thenReturn(longitude);
		
		assertEquals(false, trackPointImpl.hasPosition());
		verify(measuredTrackPoint, atMost(1)).getLatitude();
		verify(measuredTrackPoint, atMost(1)).getLongitude();
	}
}
