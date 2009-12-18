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
import org.joda.time.Duration


/** Some "static functions in here" */
object Speed {
  def speed(length:Length, duration:Duration) = {
    val seconds = duration.getMillis * 0.001
    
    new MetersPerSecond(length.siValue/ seconds)
  }
  
  val ZERO:Speed = new MetersPerSecond(0)
  
  val MPS_CONVERSION = 1.0
  val KMPH_CONVERSION = 1.0/3.6
  
  case class MetersPerSecond(val mps:Double) extends Speed(mps) {
    override val conversionValue = Speed.MPS_CONVERSION
    override val suffix = "m/s"
    override val format = new DecimalFormat("##0.0")
  }

  case class KilometersPerHour(val kmph:Double) extends Speed(kmph) {
    override val conversionValue = Speed.KMPH_CONVERSION
    override val suffix = "km/h"
    override val format = new DecimalFormat("##0.0")
  }
}

abstract class Speed(value:Double) {
  // we consider speeds with a difference of less than 1mm/s to be the same.
  private val EQUALITY_DELTA = 0.001
  
  def conversionValue:Double
  def suffix:String
  def format:NumberFormat
  
  def siValue = value * conversionValue
  
  def toMetersPerSecond = new Speed.MetersPerSecond(siValue)
  def toKilometersPerHour = new Speed.KilometersPerHour(siValue / Speed.KMPH_CONVERSION)
  
  override def equals(that:Any) = {
    if (that == null) false
    else if (!that.isInstanceOf[Speed]) false
    else Math.abs(siValue - (that.asInstanceOf[Speed]).siValue) < EQUALITY_DELTA
  }
  
  override def hashCode = siValue.hashCode
  override def toString = format.format(value) + " " + suffix
  
  def >(that:Speed) = siValue > that.siValue
  def <(that:Speed) = siValue < that.siValue
  
  def max(that:Speed) = if (this > that) this else that
  def min(that:Speed) = if (this < that) this else that
  
  /** Convert an si value to a new Speed object, which has the
      same class as this Speed object */
  private def convert(si: Double) = {
    this match {
      case Speed.MetersPerSecond(n) => new Speed.MetersPerSecond(si)
      case Speed.KilometersPerHour(n) => new Speed.MetersPerSecond(si/Speed.KMPH_CONVERSION)
    }
  }
  
  
}
