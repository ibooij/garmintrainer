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
package nl.iljabooij.garmintrainer.model

import nl.iljabooij.garmintrainer.model.Duration._
import nl.iljabooij.garmintrainer.model.Length.{Kilometer,Meter}
import nl.iljabooij.garmintrainer.model.Speed.{KilometersPerHour,MetersPerSecond}

import org.junit.Assert._
import org.mockito.Mockito._
import org.scalatest.junit.{JUnit3Suite,AssertionsForJUnit}
import org.scalatest.mock.MockitoSugar


class SpeedTest extends JUnit3Suite with AssertionsForJUnit with MockitoSugar {
  val DELTA = 0.00001
  
  def testSiValues {
    val speedInKmph = 30.22
    val kmph = new KilometersPerHour(speedInKmph)
    assertEquals(speedInKmph/3.6, kmph.siValue, DELTA)
    
    val speedInMps = 10.3
    assertEquals(speedInMps, new MetersPerSecond(speedInMps).siValue, DELTA)
  }
  
  def testCalculateSpeed {
    val speed = Speed.speed(new Meter(10), second)
    assertEquals(new MetersPerSecond(10), speed)
    assertEquals(new MetersPerSecond(10), Speed.
    	speed(new Kilometer(36), hour))
  }
  
  def testHashCode {
    assertEquals(new MetersPerSecond(10).hashCode, Speed.
    	speed(new Kilometer(36), hour).hashCode)
  }
  
  def testToMetersPerSecond {
    val kmph = new KilometersPerHour(36)
    val mps = kmph.toMetersPerSecond
    assertEquals(10.0, mps.mps, DELTA)
  }
  
  def testToKilometersPerHour {
    val mps = new MetersPerSecond(10)
    val kmph = mps.toKilometersPerHour
    assertEquals(36.0, kmph.kmph, DELTA)
  }  
  
  def testToString {
    assertEquals("10.0 m/s", new MetersPerSecond(10.0).toString)
    assertEquals("-10.0 m/s", new MetersPerSecond(-10.0).toString)
    assertEquals("0.2 m/s", new MetersPerSecond(0.23).toString)
    assertEquals("10.0 km/h", new KilometersPerHour(10.0).toString)
    assertEquals("-10.0 km/h", new KilometersPerHour(-10.0).toString)
    assertEquals("0.2 km/h", new KilometersPerHour(0.23).toString)
  }
  
  def testSmallerThan {
    val _10_1mps = new MetersPerSecond(10.1)
    val _36kmph = new KilometersPerHour(36.0)
    assertFalse(_10_1mps < _36kmph)
    assertTrue(_36kmph < _10_1mps)	
  }
  
  def testGreaterThan {
    val _10_1mps = new MetersPerSecond(10.1)
    val _36kmph = new KilometersPerHour(36.0)
    assertTrue(_10_1mps > _36kmph)
    assertFalse(_36kmph > _10_1mps)	
  }
  
  def testMax {
    val _10_1mps = new MetersPerSecond(10.1)
    val _36kmph = new KilometersPerHour(36.0)
    assertEquals(_10_1mps, _10_1mps max _36kmph)
    assertEquals(_10_1mps, _36kmph max _10_1mps)
  }
  
  def testMin {
    val _10_1mps = new MetersPerSecond(10.1)
    val _36kmph = new KilometersPerHour(36.0)
    assertEquals(_36kmph, _10_1mps min _36kmph)
    assertEquals(_36kmph, _36kmph min _10_1mps)
  }
}
