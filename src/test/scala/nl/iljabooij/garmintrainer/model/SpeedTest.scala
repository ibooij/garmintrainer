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

import nl.iljabooij.garmintrainer.model.Length.{Kilometer,Meter}
import nl.iljabooij.garmintrainer.model.Speed.{KilometersPerHour,MetersPerSecond}

import org.junit.Assert._
import org.mockito.Mockito._
import org.joda.time.{DateTime,Duration,Hours,Seconds}
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
    val speed = Speed.speed(new Meter(10), Seconds.seconds(1).toStandardDuration)
    assertEquals(new MetersPerSecond(10), speed)
    assertEquals(new MetersPerSecond(10), Speed.
    	speed(new Kilometer(36), Hours.hours(1).toStandardDuration))
  }
  
  def testHashCode {
    assertEquals(new MetersPerSecond(10).hashCode, Speed.
    	speed(new Kilometer(36), Hours.hours(1).toStandardDuration).hashCode)
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
}
