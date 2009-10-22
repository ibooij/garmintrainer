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
import static org.junit.Assert.assertTrue;
import nl.iljabooij.garmintrainer.model.Speed.Unit;

import org.joda.time.Duration;
import org.junit.Test;


public class SpeedTest {
	private static final double DELTA = 0.00001;
	
	private static final Length _100Meters = Length.createLengthInMeters(100);
	private static final Duration _10Seconds = Duration.standardSeconds(10);
	private static final Length _36Km = Length.createLength(36, Length.Unit.Kilometer);
	private static final Duration _1Hour = Duration.standardHours(1);
	
	@Test
	public void createMetersPerSecond() {
		final double value = 21.0;
		
		final Speed speed = Speed.createExactSpeedInMetersPerSecond(value);
		assertEquals("correct number of meters per second", value,
				speed.getValueInMetersPerSecond(), DELTA);
	}
	
	@Test
	public void createSpeedInMetersPerSecond() {
		final Speed speed10MPS = Speed.createSpeedInMetersPerSecond(_100Meters, _10Seconds);
		final Speed _36KmH = Speed.createSpeedInMetersPerSecond(_36Km, _1Hour);
		assertEquals("100 meters in 10 seconds equals 10 m/s", 10.0,
				speed10MPS.getValueInMetersPerSecond(), DELTA);
		assertEquals("36 km in 1 hour equals 10 m/s", 10.0,
				_36KmH.getValueInMetersPerSecond(), DELTA);
	}
	
	@Test
	public void createExactSpeed() {
		final Speed _10MPS = Speed.createExactSpeed(10.0, Unit.MetersPerSecond);
		final Speed _36KmH = Speed.createExactSpeed(36.0, Unit.KilometersPerHour);
		
		assertEquals(_10MPS, _36KmH);
	}
	
	@Test
	public void createSpeed() {
		final Speed _10MPS = Speed.createSpeed(_36Km, _1Hour, Unit.MetersPerSecond);
		assertEquals("100 meters in 10 seconds equals 10 m/s", 10.0,
				_10MPS.getValueInMetersPerSecond(), DELTA);
		assertEquals("100 meters in 10 seconds equals 10 m/s", 10.0,
				_10MPS.getValue(), DELTA);
		assertEquals("unit is Unit.MetersPerSecond", Unit.MetersPerSecond, 
				_10MPS.getUnit());
		
		final Speed _36KmH = Speed.createSpeed(_36Km, _1Hour, Unit.KilometersPerHour);
		assertEquals("36 km/h equals 10 m/s", 10.0,
				_36KmH.getValueInMetersPerSecond(), DELTA);
		assertEquals("should read 36 in km/h", 36,
				_36KmH.getValue(), DELTA);
		assertEquals("check that the right unit is returned", Unit.KilometersPerHour, 
				_36KmH.getUnit());
	}
	
	@Test(expected=NullPointerException.class)
	public void createSpeedThrowsNPEOnNullUnit() {
		Speed.createSpeed(_36Km, _1Hour, null);
	}
	
	@Test(expected=NullPointerException.class)
	public void createSpeedWithNullLengthShouldThrowNPE() {
		Speed.createSpeedInMetersPerSecond(null, _10Seconds);
	}
	
	@Test(expected=NullPointerException.class)
	public void createSpeedWithNullDurationShouldThrowNPE() {
		Speed.createSpeedInMetersPerSecond(_100Meters, null);
	}
	
	@Test
	public void getValue() {
		final Speed speed10MPS = Speed.createSpeedInMetersPerSecond(_100Meters, _10Seconds);
		
		final double value = speed10MPS.getValue(Unit.KilometersPerHour);
		assertEquals("10 m/s is 36 km/h", 36.00,
				value, DELTA);
	}
	
	@Test(expected=NullPointerException.class) 
	public void getValueWithNullUnitThrowsNPE() {
		Speed.createSpeedInMetersPerSecond(_100Meters, _10Seconds).getValue(null);
	}
	
	@Test
	public void equalsWorks() {
		final Speed _10MPS = Speed.createSpeedInMetersPerSecond(_100Meters, _10Seconds);
		final Speed _36KmH = Speed.createSpeedInMetersPerSecond(_36Km, _1Hour);
		
		assertEquals("speeds should be equal", _10MPS, _36KmH);
	}
	
	@Test
	public void hashCodeWorks() {
		final Speed _10MPS = Speed.createSpeedInMetersPerSecond(_100Meters, _10Seconds);
		final Speed _36KmH = Speed.createSpeedInMetersPerSecond(_36Km, _1Hour);
		
		assertEquals("hashcode of speeds should be equal", _10MPS.hashCode(), _36KmH.hashCode());
	}
	
	@Test
	public void toStringWorks() {
		final Speed _10MPS = Speed.createSpeedInMetersPerSecond(_100Meters, _10Seconds);
		final Speed _36KmH = Speed.createSpeed(_36Km, _1Hour, Unit.KilometersPerHour);
		
		assertEquals("speed string ok", "10.0 m/s", _10MPS.toString());
		assertEquals("speed string ok", "36.00 km/h", _36KmH.toString());
	}
	
	@Test
	public void convertWorks() {
		final Speed _10MPS = Speed.createSpeedInMetersPerSecond(_100Meters, _10Seconds);
		final Speed _36KmH = _10MPS.convert(Unit.KilometersPerHour);
		
		assertEquals("speed string ok", "36.00 km/h", _36KmH.toString());
	}
	
	@Test(expected=NullPointerException.class)
	public void convertThrowsNPEOnNullUnit() {
		final Speed _10MPS = Speed.createSpeedInMetersPerSecond(_100Meters, _10Seconds);
		_10MPS.convert(null);
	}
	
	@Test
	public void testCompareTo() {
		final Speed _10MPS = Speed.createSpeedInMetersPerSecond(_100Meters, _10Seconds);
		final Speed _36KmH = _10MPS.convert(Unit.KilometersPerHour);
		final Speed _11MPS = Speed.createSpeedInMetersPerSecond(Length.createLengthInMeters(110), _10Seconds);
		final Speed _12MPS = Speed.createSpeedInMetersPerSecond(Length.createLengthInMeters(120), _10Seconds);
		
		assertTrue("10 m/s == 36 km/h", _10MPS.compareTo(_36KmH) == 0);
		assertTrue("10 m/s < 11 m/s",_10MPS.compareTo(_11MPS) < 0);
		assertTrue("11 m/s < 12 m/s",_11MPS.compareTo(_12MPS) < 0);
		assertTrue("12 m/s > 10 m/s",_12MPS.compareTo(_10MPS) > 0);
	}
	
	@Test(expected=NullPointerException.class)
	public void compareToThrowsNPEOnNullArgument() {
		final Speed _10MPS = Speed.createSpeedInMetersPerSecond(_100Meters, _10Seconds);
		_10MPS.compareTo(null);
	}
}
