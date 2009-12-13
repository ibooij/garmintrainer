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

import nl.iljabooij.garmintrainer.Preconditions._
import nl.iljabooij.garmintrainer.model.Length
import nl.iljabooij.garmintrainer.model.Length.{Meter}

import org.apache.commons.beanutils.Converter

/**
 * Convertor for {@link Length}.
 * 
 * Converts from String to {@link Length}.
 * @see Converter.
 * @author ilja
 *
 */
final class LengthConverter extends Converter {
  /**
   * Convert a value to a {@link Length}.
   * 
   * @param type type (class) to convert to.
   * @param value value to convert
   * @throws NullPointerException if type or value is null
   * @throws IllegalArgumentException if type is not class {@link Length}, 
   * value is not a {@link String} or value cannot be parsed to a {@link Double}.
   */
  override def convert(clazz: Class[_], value: Object): Object = {
    checkNotNull(clazz)
    checkNotNull(value)
    checkArgument(clazz == classOf[Length])
    checkArgument(classOf[String].isAssignableFrom(value.getClass()))
	// don't catch NumberFormatException because that's just a special case
	// of an IllegalArgumentException.
    val d = value.asInstanceOf[String].toDouble
    
    new Meter(d)
  }
}