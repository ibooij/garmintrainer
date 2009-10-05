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

import java.util.Date;

import nl.iljabooij.garmintrainer.parser.digester.DateTimeConverter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Test;

public class DateTimeConverterTest {
	private DateTimeConverter dateTimeConverter;
	private DateTime dateTime;
	
	private static final String TEST_STRING = "2009-02-21T12:57:25Z";
	
	@Before
	public void setUp() throws Exception {
		dateTimeConverter = new DateTimeConverter();
		DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis();
		dateTime = dateTimeFormatter.parseDateTime(TEST_STRING); 
	}

	@Test
	public void testHappyFlow() {
		assertEquals("correct datetime", dateTime, dateTimeConverter.convert(DateTime.class, TEST_STRING));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testUnparsableDate() {
		String unparseableString = "No date";
		dateTimeConverter.convert(DateTime.class, unparseableString);
	}
	
	@Test(expected=NullPointerException.class)
	public void testNullClass() {
		dateTimeConverter.convert(null, TEST_STRING);
	}
	
	@Test(expected=NullPointerException.class)
	public void testNullValue() {
		dateTimeConverter.convert(DateTime.class, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWrongClass() {
		dateTimeConverter.convert(Date.class, TEST_STRING);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWrongValueClass() {
		dateTimeConverter.convert(DateTime.class, Integer.valueOf(1));
	}
}
