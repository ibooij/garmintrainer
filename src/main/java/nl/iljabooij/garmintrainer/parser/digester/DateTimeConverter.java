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

import org.apache.commons.beanutils.Converter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class DateTimeConverter implements Converter {
	private static DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis();
	
	
	@SuppressWarnings("unchecked")
    @Override
	public Object convert(final Class type, final Object value) {
		if (type == null) 
			throw new NullPointerException("type cannot be null");
		if (value == null)
			throw new NullPointerException("value cannot be null");
		if (type != DateTime.class)
			throw new IllegalArgumentException("wrong type class");
		if (!String.class.isAssignableFrom(value.getClass())) 
			throw new IllegalArgumentException("wrong value class");
		
		return dateTimeFormatter.parseDateTime((String) value);
	}

}
