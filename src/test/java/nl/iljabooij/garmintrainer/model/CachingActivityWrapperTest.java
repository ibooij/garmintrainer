package nl.iljabooij.garmintrainer.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class CachingActivityWrapperTest {
	private Activity activity;
	private CachingActivityWrapper cachingActivityWrapper;

	private static final Length EXAMPLE_LENGTH = Length
			.createLengthInMeters(1.0);

	@Before
	public void setUp() throws Exception {
		activity = mock(Activity.class);
		cachingActivityWrapper = new CachingActivityWrapper(activity);
	}

	/**
	 * Constructor should throw NPE if activity is null
	 */
	@Test(expected = NullPointerException.class)
	public void testCachingActivityWrapper() {
		new CachingActivityWrapper(null);
	}

	@Test
	public void testGetAltitudeGain() {
		when(activity.getAltitudeGain()).thenReturn(EXAMPLE_LENGTH);

		assertEquals(EXAMPLE_LENGTH, cachingActivityWrapper.getAltitudeGain());
		verify(activity, atLeastOnce()).getAltitudeGain();
		
		// call again, check that cached value is used
		assertEquals(EXAMPLE_LENGTH, cachingActivityWrapper.getAltitudeGain());
		verify(activity, atMost(1)).getAltitudeGain();
	}

	@Test
	public void testGetDistance() {
		when(activity.getDistance()).thenReturn(EXAMPLE_LENGTH);

		assertEquals(EXAMPLE_LENGTH, cachingActivityWrapper.getDistance());
		verify(activity, atLeastOnce()).getDistance();
	}

	@Test
	public void testGetEndTime() {
		DateTime dt = new DateTime();

		when(activity.getEndTime()).thenReturn(dt);

		assertEquals(dt, cachingActivityWrapper.getEndTime());
		verify(activity, atLeastOnce()).getEndTime();
	}

	@Test
	public void testGetGrossDuration() {
		Duration duration = Duration.standardMinutes(60);
		when(activity.getGrossDuration()).thenReturn(duration);
		assertEquals(duration, cachingActivityWrapper.getGrossDuration());
		verify(activity, atLeastOnce()).getGrossDuration();
	}

	@Test
	public void testGetLaps() {
		ImmutableList<Lap> laps = ImmutableList.of(mock(Lap.class),
				mock(Lap.class));
		when(activity.getLaps()).thenReturn(laps);
		assertEquals(laps, cachingActivityWrapper.getLaps());
		verify(activity, atLeastOnce()).getLaps();
	}

	@Test
	public void testGetMaximumAltitude() {
		when(activity.getMaximumAltitude()).thenReturn(EXAMPLE_LENGTH);

		assertEquals(EXAMPLE_LENGTH, cachingActivityWrapper.getMaximumAltitude());
		verify(activity, atLeastOnce()).getMaximumAltitude();
		
		assertEquals(EXAMPLE_LENGTH, cachingActivityWrapper.getMaximumAltitude());
		verify(activity, atMost(1)).getMaximumAltitude();
	}

	@Test
	public void testGetMaximumSpeed() {
		Speed speed = Speed.createExactSpeedInMetersPerSecond(1.0);
		when(activity.getMaximumSpeed()).thenReturn(speed);
		assertEquals(speed, cachingActivityWrapper.getMaximumSpeed());
		verify(activity, atLeastOnce()).getMaximumSpeed();
		assertEquals(speed, cachingActivityWrapper.getMaximumSpeed());
		verify(activity, atMost(1)).getMaximumSpeed();
	}

	@Test
	public void testGetMinimumAltitude() {
		when(activity.getMinimumAltitude()).thenReturn(EXAMPLE_LENGTH);

		assertEquals(EXAMPLE_LENGTH, cachingActivityWrapper.getMinimumAltitude());
		verify(activity, atLeastOnce()).getMinimumAltitude();
		
		assertEquals(EXAMPLE_LENGTH, cachingActivityWrapper.getMinimumAltitude());
		verify(activity, atMost(1)).getMinimumAltitude();
	}

	@Test
	public void testGetNetDuration() {
		Duration duration = Duration.standardHours(1);
		when(activity.getNetDuration()).thenReturn(duration);
		assertEquals(duration, cachingActivityWrapper.getNetDuration());
		verify(activity, atLeastOnce()).getNetDuration();
	}

	@Test
	public void testGetStartTime() {
		DateTime dt = new DateTime();

		when(activity.getStartTime()).thenReturn(dt);

		assertEquals(dt, cachingActivityWrapper.getStartTime());
		verify(activity, atLeastOnce()).getStartTime();
	}

	@Test
	public void testGetTrackPoints() {
		ImmutableList<TrackPoint> trackPoints = ImmutableList.of(
				mock(TrackPoint.class), mock(TrackPoint.class), mock(TrackPoint.class));
		when(activity.getTrackPoints()).thenReturn(trackPoints);
		assertEquals(trackPoints, cachingActivityWrapper.getTrackPoints());
		verify(activity, atLeastOnce()).getTrackPoints();
		assertEquals(trackPoints, cachingActivityWrapper.getTrackPoints());
		verify(activity, atMost(1)).getTrackPoints();
	}

	@Test
	public void testCompareTo() {
		int[] returnValues = new int[] {-1, 0, 1};
		Activity other = mock(Activity.class);
		when(activity.compareTo(other)).thenReturn(returnValues[0],
				returnValues[1], returnValues[2]);
		for (int returnValue: returnValues) {
			assertEquals(returnValue, cachingActivityWrapper.compareTo(other));
		}
		
		verify(activity, atLeast(3)).compareTo(other);		
	}

	@Test
	public void toStringNotEmpty() {
		String s = "ActivityImpl#toString()";
		when(activity.toString()).thenReturn(s);
		assertTrue(cachingActivityWrapper.toString().contains(s));
		// It would be nice to verify toString(), but that's not possible in
		// Mockito. The following line will make this test fail. 
		// verify(activity, atLeastOnce()).toString();
	}
}
