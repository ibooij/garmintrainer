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
import nl.iljabooij.garmintrainer.model.Length.Unit;

import org.junit.Test;

public class LengthTest {
	private static final double DELTA = 0.00001;
	private Length _5km = Length.createLength(5.000,
			Unit.Kilometer);
	private Length _2km = Length.createLength(2.0,
			Unit.Kilometer);
	private Length _200m = Length.createLength(200.0,
			Unit.Meter);

	@Test
	public void testCreateLengthInMeters() {
		final Length length = Length.createLengthInMeters(10);

		assertEquals("length should be 10", 10, length
				.getValueInMeters(), DELTA);
		assertEquals("unit should be Meter", Unit.Meter, length.getUnit());
	}

	@Test
	public void testCreateLengthInMetersWithInteger() {
		final int value = 123;
		final Length length = Length.createLengthInMeters(value);

		assertEquals("length should be " + value, value, length.getValueInMeters(), DELTA);
		assertEquals("unit should be Meter", Unit.Meter, length.getUnit());
	}
	
	@Test
	public void testCreateLengthInMetersWithDouble() {
		final double value = 123;
		final Length length = Length.createLengthInMeters(value);

		assertEquals("length should be " + value, value, length
				.getValueInMeters(), 0.0001);
		assertEquals("unit should be Meter", Unit.Meter, length.getUnit());
	}
	
	@Test
	public void testCreateLength() {
		final Length lengthMeters = Length.createLength(10,
				Unit.Meter);

		assertEquals("length should be 10", 10, lengthMeters
				.getValueInMeters(), 0.00001);
		assertEquals("unit should be Meter", Unit.Meter, lengthMeters.getUnit());

		final Length lengthKM = Length.createLength(10,
				Unit.Kilometer);

		assertEquals("length should be 10000", 10000, lengthKM
				.getValueInMeters(), 0.0001);
		assertEquals("unit should be Kilometer", Unit.Kilometer, lengthKM.getUnit());
	}

	@Test
	public void testGetValueWithArgument() {
		assertEquals("standard length is 5 kilometers", 5.000,
				_5km.getValue(Unit.Kilometer), DELTA);
		assertEquals("standard length is 5000 meters", 5000.0,
				_5km.getValue(Unit.Meter), DELTA);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetValueThrowsIAEOnNullUnit() {
		_5km.getValue(null);
	}
	
	@Test
	public void testGetValue() {
		assertEquals("value of _5km is 5", 5,
				_5km.getValue(), DELTA);
		assertEquals("value of _200m is 200", 200,
				_200m.getValue(), DELTA);
	}
	
	@Test
	public void convert() {
		Length _200mInKm = _200m.convert(Unit.Kilometer);
		assertEquals("value of _200m in kilometers is 0.200", 0.200,
				_200mInKm.getValue(), DELTA);
		assertEquals("value of _200m in meters is 200.0", 200.0,
				_200mInKm.convert(Unit.Meter).getValue(), DELTA);
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void convertThrowsIAEOnNullUnit() {
		_200m.convert(null);
	}
	
	
	@Test
	public void testSubtraction() {
		final Length afterFirst = _5km.minus(_2km);
		assertEquals("result is 3km", 3.0, afterFirst.getValue(), DELTA);
		assertEquals("result is 3000 meters", 3000.0,
				afterFirst.getValue(Unit.Meter), DELTA);
		final Length afterSecond = afterFirst.minus(_200m);
		assertEquals("result is 2.8km", 2.8, afterSecond.getValue(), DELTA);
		assertEquals("result is 2800 meters", 2800.0,
				afterSecond.getValue(Unit.Meter), DELTA);
		
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSubtractThrowIAEOnNullSubtractor() {
		_5km.minus(null);
	}
	
	/**
	 * Test the {@link Length#plus(Length)} method.
	 */
	@Test
	public void testAddition() {
		final Length afterFirst = _5km.plus(_2km);
		assertEquals("result is 7km", 7.0, afterFirst.getValue(), DELTA);
		assertEquals("unit is Km", Unit.Kilometer, afterFirst.getUnit());
		final Length afterSecond = _200m.plus(afterFirst);
		assertEquals("result is 7200m", 7200.0, afterSecond.getValue(), DELTA);
		assertEquals("unit is Km", Unit.Meter, afterSecond.getUnit());	
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAdditionThrowIAEOnNullSubtractor() {
		_5km.plus(null);
	}
	
	@Test
	public void testMultiply() {
		Length _15km = Length.createLength(15.0, Length.Unit.Kilometer);
		assertEquals(_15km, _5km.times(3.0));
	}
	
	@Test
	public void testDivide() {
		Length _15km = Length.createLength(15.0, Length.Unit.Kilometer);
		assertEquals(_5km, _15km.divide(3.0));
	}
	
	@Test
	public void testEquals() {
		assertEquals("1 km and 1000 m should be equal", Length.createLength(
				1, Unit.Kilometer), Length.createLength(
				1000, Unit.Meter));
		
		assertEquals("1.35 km and 1350 m should be equal", Length.createLength(
				1.35, Unit.Kilometer), Length.createLength(
				1350, Unit.Meter));
	}
	
	@Test
	public void testHashcode() {
		assertEquals("1 km and 1000 m should be equal", Length.createLength(
				1, Unit.Kilometer).hashCode(), Length.createLength(
				1000, Unit.Meter).hashCode());
		
		assertEquals("1.35 km and 1350 m should be equal", Length.createLength(
				1.35, Unit.Kilometer).hashCode(), Length.createLength(
				1350, Unit.Meter).hashCode());
	}
	
	@Test
	public void testToString() {
		assertEquals("1.000 km", Length.createLength(
				1, Unit.Kilometer).toString());
		assertEquals("1.0 m", Length.createLength(
				1, Unit.Meter).toString());
		assertEquals("3121.0 m", Length.createLength(
				3121, Unit.Meter).toString());
	}
	
	@Test
	public void testCompareTo() {
		assertTrue("5km > 2km", _5km.compareTo(_2km) > 0);
		assertTrue("200m < 2km", _200m.compareTo(_2km) < 0);
		
		Length _0Point2Km = Length.createLength(0.2, Length.Unit.Kilometer);
		assertTrue("200m = 0.2km", _200m.compareTo(_0Point2Km) == 0);
		assertEquals("200m = 0.2km", _200m, _0Point2Km);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCompareToWithNullThrowsNPE() {
		_5km.compareTo(null);
	}
	
}
