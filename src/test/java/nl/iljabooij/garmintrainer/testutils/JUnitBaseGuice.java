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
package nl.iljabooij.garmintrainer.testutils;

import java.lang.reflect.Field;

import nl.iljabooij.garmintrainer.util.InjectLogger;

import org.junit.runners.model.InitializationError;

/**
 * Base class for Test classes using Guice.
 */
public class JUnitBaseGuice {
	/**
	 * This method will make sure that all fields in an object that are annotated
	 * with the @InjectLogger annotation will be provided with a simple, non-working
	 * logger, so they will not get null pointer exceptions.
	 * @param o the object to provide with logger(s).
	 * @throws InitializationError if setting the logger fails.
	 */
	protected void setupLogger(Object o) throws InitializationError {
		Field[] fields = o.getClass().getDeclaredFields();
		for (Field field: fields) {
			if (field.isAnnotationPresent(InjectLogger.class)) {
				try {
					field.setAccessible(true);
					field.set(o, new FakeLogger());
				} catch (IllegalArgumentException e) {
					throw new InitializationError(e.getMessage());
				} catch (IllegalAccessException e) {
					throw new InitializationError(e.getMessage());
				}
			}
		}
	}
}
