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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.commons.beanutils.Converter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public final class DateTimeConverter implements Converter {
	private static DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis();
	
	@SuppressWarnings("unchecked")
    @Override
	public Object convert(final Class type, final Object value) {
		checkNotNull(type, "type cannot be null");
		checkNotNull(value, "Value cannot be null");
		checkArgument(type == DateTime.class, "Conversion target should be org.joda.time.DateTime, but is %s", type.getClass());
		checkArgument(String.class.isAssignableFrom(value.getClass()),
				"Value should be a string, but is a %s", value.getClass());
		
		return dateTimeFormatter.parseDateTime((String) value);
	}

}
