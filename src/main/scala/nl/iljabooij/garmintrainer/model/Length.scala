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

import java.text.{DecimalFormat,NumberFormat}

import org.apache.commons.lang.builder.HashCodeBuilder

/**
 * Implements a measurement of Length. A Length object is immutable.
 * @author ilja booij <ibooij@gmail.com>
 */

object Length {
  val METER = 1.0
  val KILOMETER = 1000.0
  
  val ZERO:Length = new Meter(0)
  
  case class Meter(val meters:Double) extends Length(meters) {
    override val conversionValue = Length.METER
    override val suffix = "m"
    override val format = new DecimalFormat("###.0")
  }
  
  case class Kilometer(val kilometers:Double) extends Length(kilometers) {
    override val conversionValue = Length.KILOMETER
    override val suffix = "km"
    override val format = new DecimalFormat("###.000")
  }
}

abstract class Length(value:Double) {
  // we consider lengths that differ by 1mm to be the same.
  private val EQUALITY_DELTA = 0.001 
  def conversionValue:Double
  def suffix:String
  def format:NumberFormat
  
  def siValue = value * conversionValue
  
  def toMeters = new Length.Meter(siValue)
  def toKilometers = new Length.Kilometer(siValue/Length.KILOMETER)
  
  private def convert(si: Double) = {
    this match {
      case Length.Meter(n) => new Length.Meter(si)
      case Length.Kilometer(n) => new Length.Kilometer(si/Length.KILOMETER)
    }
  }
  def +(that: Length) = convert(siValue + that.siValue)
  def -(that: Length) = convert(siValue - that.siValue)
  def *(that: Double) = convert(siValue * that)
  def /(that:Double) = convert(siValue / that)
  def <(that:Length) = siValue < that.siValue
  def >(that:Length) = siValue > that.siValue
  
  override def toString = format.format(value) + " " + suffix
  
  /** {@inheritDoc} */
  override def equals(o: Any) = {
    if (o == null) false
    else if (!(o.isInstanceOf[Length])) false
    else Math.abs(siValue - o.asInstanceOf[Length].siValue) < EQUALITY_DELTA
  }
  
  override def hashCode = siValue.hashCode
}

