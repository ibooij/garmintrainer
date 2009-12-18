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
import org.junit.Assert._
import org.mockito.Mockito._
import org.joda.time.{DateTime,Duration}
import org.scalatest.junit.{JUnit3Suite,AssertionsForJUnit}
import org.scalatest.mock.MockitoSugar

class LengthTest extends JUnit3Suite with AssertionsForJUnit with MockitoSugar {
  val DELTA = 0.00001
  val _5km = new Kilometer(5)
  val _2km = new Kilometer(2)
  val _200m = new Meter(200)
  
  def testMeters {
    // raw value should be 10.0 meters.
    assertEquals(10.0, new Meter(10).siValue, DELTA)
  }
  
  def testKilometers {
    assertEquals(10000.0, new Kilometer(10).siValue, DELTA)
  }
  
  def testConvert {
    val _200m = new Meter(200)
    assertEquals(0.2, _200m.toKilometers.kilometers, DELTA)
    
    val _15km = new Kilometer(15)
    assertEquals(15000.0, _15km.toMeters.meters, DELTA)
  }
  
  def testAddition {
    val _first = _5km + _2km
    assertEquals(new Kilometer(7), _first)
    val _second = _first + _200m
    assertEquals(new Kilometer(7.2), _second)
  }
  
  def testSubtraction {
    val _first = _5km - _2km
    assertEquals(new Kilometer(3), _first)
    val _second = _first - _200m
    assertEquals(new Kilometer(2.8), _second)
  }
  
  def testMultiply {
    assertEquals(new Kilometer(15.9), new Kilometer(5.3) * 3.0)
  }
  
  def testDivide {
    assertEquals(new Kilometer(5.3), new Kilometer(15.9) / 3.0)
    assertEquals(5.0, new Kilometer(1.5) / new Meter(300), DELTA)
    assertEquals(0.2, new Kilometer(0.3) / new Meter(1500), DELTA)
  }
  
  def testEquals {
    assertEquals(new Kilometer(1.0), new Meter(1000.0))
    assertEquals(new Kilometer(1.0015), new Meter(1001.5))
  }
  
  def testHashCode {
    assertEquals(new Kilometer(1.0).hashCode, new Meter(1000.0).hashCode)
    assertEquals(new Kilometer(1.0015).hashCode, new Meter(1001.5).hashCode)
  }
  
  def testToString {
    assertEquals("1.0 m", new Meter(1).toString)
    assertEquals("0.2 m", new Meter(0.2).toString)
    assertEquals("3121.0 m", new Meter(3121).toString)
    assertEquals("1.000 km", new Kilometer(1).toString)
    assertEquals("0.200 km", new Kilometer(0.2).toString)
  }
  
  def testToStringNegative {
    assertEquals("-1.0 m", new Meter(-1).toString)
    assertEquals("-0.2 m", new Meter(-0.2).toString)
    assertEquals("-3121.0 m", new Meter(-3121).toString)
    assertEquals("-1.000 km", new Kilometer(-1).toString)
    assertEquals("-0.200 km", new Kilometer(-0.2).toString)
  }
  
  def testCompare {
    assertTrue(new Kilometer(1) > new Meter(1))
    assertTrue(new Meter(1.1) > new Meter(1))
    assertTrue(new Kilometer(1) < new Meter(1001))
    assertTrue(new Meter(1.1) < new Meter(1.2))
  }
  
  def testMax {
    val _1001m = new Meter(1001)
    val _1km = new Kilometer(1)
    assertEquals(_1001m, _1001m max _1km)
    assertEquals(_1001m, _1km max _1001m)
  }
  
  def testMin {
    val _1001m = new Meter(1001)
    val _1km = new Kilometer(1)
    assertEquals(_1km, _1001m min _1km)
    assertEquals(_1km, _1km min _1001m)
  }
}
