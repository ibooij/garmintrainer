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
package nl.iljabooij.garmintrainer.parser.digester

import org.junit.Assert._
import org.scalatest.junit.{JUnit3Suite,AssertionsForJUnit}
import nl.iljabooij.garmintrainer.model.{Length,Speed}


class LengthConverterTest extends JUnit3Suite {
	var lengthConverter:LengthConverter = null
	
	override def setUp {
		lengthConverter = new LengthConverter
	}

	def testHappyFlow {
	  val o = lengthConverter.convert(classOf[Length], "1.0")
	  assertTrue("o should be a Length object", o.isInstanceOf[Length])
	  val length = o.asInstanceOf[Length]
	  assertEquals("length should be 1 meter", Length.createLengthInMeters(1.0), length);
	}
	
	def testWrongValue {
	  intercept[IllegalArgumentException] {
		lengthConverter.convert(classOf[Length], "abc")
      }
	}
	
	def testWithNullClass {
	  intercept[NullPointerException] {
		lengthConverter.convert(null, "1.0")
      }
	}
	
	def testWithNullValue {
	  intercept[NullPointerException] {
		lengthConverter.convert(classOf[Length], null);
      }
	}
	
	def testWithWrongClass {
	  intercept[IllegalArgumentException] {
		lengthConverter.convert(classOf[Speed], "1.0");
      }
	}
	
	def testWithWithWrongValueClass {
	  intercept[IllegalArgumentException] {
		lengthConverter.convert(classOf[Length], Integer.valueOf(1));
      }
	}

}
