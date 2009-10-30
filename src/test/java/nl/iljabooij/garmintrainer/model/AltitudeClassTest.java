package nl.iljabooij.garmintrainer.model;

import static org.junit.Assert.*;

import nl.iljabooij.garmintrainer.model.AltitudeClass;
import nl.iljabooij.garmintrainer.model.Length;

import org.junit.Before;
import org.junit.Test;

public class AltitudeClassTest {
	private final static Length LOW_ALTITUDE = Length.createLengthInMeters(20);
	private final static Length LOW_HILLS_ALTITUDE = Length.createLengthInMeters(100);
	private final static Length HIGH_HILLS_ALTITUDE = Length.createLengthInMeters(300);
	private final static Length LOW_MOUNTAINS_ALTITUDE = Length.createLengthInMeters(800);
	private final static Length MEDIUM_MOUNTAINS_ALTITUDE = Length.createLengthInMeters(1200);
	private final static Length HIGH_MOUNTAINS_ALTITUDE = Length.createLengthInMeters(2600);
	private final static Length EXTREME_MOUNTAINS_ALTITUDE = Length.createLengthInMeters(4100);
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testForAltitude() {
		assertEquals(AltitudeClass.LOW, AltitudeClass.forMaximumAltitude(LOW_ALTITUDE));
		assertEquals(AltitudeClass.HILLS_LOW, AltitudeClass.forMaximumAltitude(LOW_HILLS_ALTITUDE));
		assertEquals(AltitudeClass.HILLS_HIGH, AltitudeClass.forMaximumAltitude(HIGH_HILLS_ALTITUDE));
		assertEquals(AltitudeClass.MOUNTAINS_LOW, AltitudeClass.forMaximumAltitude(LOW_MOUNTAINS_ALTITUDE));
		assertEquals(AltitudeClass.MOUNTAINS_MEDIUM, AltitudeClass.forMaximumAltitude(MEDIUM_MOUNTAINS_ALTITUDE));
		assertEquals(AltitudeClass.MOUNTAINS_HIGH, AltitudeClass.forMaximumAltitude(HIGH_MOUNTAINS_ALTITUDE));
		assertEquals(AltitudeClass.MOUNTAINS_EXTREME, AltitudeClass.forMaximumAltitude(EXTREME_MOUNTAINS_ALTITUDE));
		
	}

}
