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

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * Type listener that listens for types that want to have a {@link Logger}.
 * Code taken from {@link http://glauche.de/2009/08/24/logging-with-slf4j-and-guice/}
 * @author ilja
 *
 */
public class Slf4jTypeListener implements TypeListener {

	@Override
	public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
		for (Field field: typeLiteral.getRawType().getDeclaredFields()) {
			if (field.getType() == Logger.class  
					& field.isAnnotationPresent(InjectLogger.class)) {
				typeEncounter.register(new Slf4jMembersInjector<I>(field));
			}
		}
	}
}
