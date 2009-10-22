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
package nl.iljabooij.garmintrainer.util;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.MembersInjector;

/**
 * MembersInjector which injects a {@link Logger} into an object.
 * Code taken from {@link http://glauche.de/2009/08/24/logging-with-slf4j-and-guice/} 
 * @author ilja
 *
 * @param <T>
 */
public class Slf4jMembersInjector<T> implements MembersInjector<T> {
	private final Field field;
	private final Logger logger;

    Slf4jMembersInjector(Field aField) {
    	field = aField;
    	logger = LoggerFactory.getLogger(field.getDeclaringClass());
    	field.setAccessible(true);
    }


	@Override
	public void injectMembers(T arg0) {
		try {
			field.set(arg0, logger);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Cannot set field.", e);
		}
		
	}
	

}
