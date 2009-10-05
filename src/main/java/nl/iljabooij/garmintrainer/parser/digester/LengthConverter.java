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
package nl.iljabooij.garmintrainer.parser.digester;

import nl.iljabooij.garmintrainer.model.Length;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.commons.beanutils.Converter;

public class LengthConverter implements Converter {
	/**
	 * Convert a value to a {@link Length}.
	 * 
	 * @param type type (class) to convert to.
	 * @param value value to convert
	 * @throws NullPointerException if type or value is null
	 * @throws IllegalArgumentException if type is not class {@link Length}, 
	 * value is not a {@link String} or value cannot be parsed to a {@link Double}.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object convert(final Class type, final Object value) {
		checkNotNull(type, "type");
		checkNotNull(value, "value");
		checkArgument(type == Length.class, "type should be Length.class");
		checkArgument(String.class.isAssignableFrom(value.getClass()),
				"value should be a string");
		
		// don't catch NumberFormatException because that's just a special case
		// of an IllegalArgumentException.
		return Length.createLengthInMeters(Double.parseDouble((String) value));
	}
}
