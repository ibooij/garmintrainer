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

import org.joda.time.DateTime;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test for {@link Pause}.
 * @author ilja
 */
public class PauseTest {
	private static final DateTime START_TIME = new DateTime();
	private static final DateTime END_TIME = START_TIME.plusMinutes(10);
 
	
	@Test(expected=NullPointerException.class)
	public void testConstructorDoesNotTakeNullStartTime() {
		new Pause(null, new DateTime());
	}
	
	@Test(expected=NullPointerException.class)
	public void testConstructorDoesNotTakeNullEndTime() {
		new Pause(new DateTime(), null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testStartTimeMustBeBeforeEndTime() {
		new Pause(END_TIME, START_TIME);
	}
	
	@Test
	public void testEquals() {
		Pause pause1 = new Pause(START_TIME, END_TIME);
		Pause pause2 = new Pause(START_TIME, END_TIME);
		Pause pause3 = new Pause(START_TIME.plusSeconds(1), END_TIME);
		Pause pause4 = new Pause(START_TIME, END_TIME.plusMillis(1));
		
		// identity
		assertEquals(pause1, pause1);
		// same start and end time
		assertEquals(pause1, pause2);
		// different start time
		assertFalse(pause1.equals(pause3));
		// different end time
		assertFalse(pause1.equals(pause4));
		
		// equals(null) should always be false
		assertFalse(pause1.equals(null));
		
		// equals(some other class) should always be false
		assertFalse(pause1.equals(new Object()));
	}
	
	@Test
	public void testHashCode() {
		Pause pause1 = new Pause(START_TIME, END_TIME);
		Pause pause2 = new Pause(START_TIME, END_TIME);
		
		assertEquals(pause1.hashCode(), pause2.hashCode());
		assertEquals(pause1.hashCode(), pause1.hashCode());
	}
}
