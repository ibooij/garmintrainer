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
import nl.iljabooij.garmintrainer.model.Speed.Unit;

import org.junit.Test;

public class SpeedUnitTest {
	@Test
	public void conversionValueForMPerSIs1() {
		assertEquals("conversion value for m/s should be one", 1,
				Unit.MetersPerSecond.getConversionValue(), 0.00001);
	}

	@Test
	public void conversionValueForKmPHerSIs3Point6() {
		assertEquals("conversion value for km/h should be 3.6", 3.6,
				Unit.KilometersPerHour.getConversionValue(), 0.00001);
	}
}
