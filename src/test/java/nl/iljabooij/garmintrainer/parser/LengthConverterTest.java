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
package nl.iljabooij.garmintrainer.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import nl.iljabooij.garmintrainer.model.Length;
import nl.iljabooij.garmintrainer.model.Speed;
import nl.iljabooij.garmintrainer.parser.digester.LengthConverter;

import org.junit.Before;
import org.junit.Test;

public class LengthConverterTest {
	LengthConverter lengthConverter;
	
	@Before
	public void setUp() throws Exception {
		lengthConverter = new LengthConverter();
	}

	@Test
	public void testHappyFlow() {
		Object o = lengthConverter.convert(Length.class, "1.0");
		assertTrue("o should be a Length object", o instanceof Length);
		Length length = (Length) o;
		assertEquals("length should be 1 meter", Length.createLengthInMeters(1.0), length);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWrongValue() {
		lengthConverter.convert(Length.class, "abc");
	}
	
	
	@Test(expected=NullPointerException.class)
	public void testWithNullClass() {
		lengthConverter.convert(null, "1.0");
	}
	
	@Test(expected=NullPointerException.class)
	public void testWithNullValue() {
		lengthConverter.convert(Length.class, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWithWrongClass() {
		lengthConverter.convert(Speed.class, "1.0");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWithWithWrongValueClass() {
		lengthConverter.convert(Length.class, Integer.valueOf(1));
	}

}
