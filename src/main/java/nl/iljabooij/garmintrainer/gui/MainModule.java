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
/**
 * 
 */
package nl.iljabooij.garmintrainer.gui;

import nl.iljabooij.garmintrainer.importer.ActivityStorage;
import nl.iljabooij.garmintrainer.importer.EmptyActivityStorage;
import nl.iljabooij.garmintrainer.importer.TcxImporter;
import nl.iljabooij.garmintrainer.importer.TcxImporterImpl;
import nl.iljabooij.garmintrainer.model.ApplicationState;
import nl.iljabooij.garmintrainer.model.ApplicationStateImpl;
import nl.iljabooij.garmintrainer.parser.TcxParser;
import nl.iljabooij.garmintrainer.parser.digester.CommonsDigesterTcxParser;
import nl.iljabooij.garmintrainer.util.Slf4jTypeListener;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;

/**
 * MainModule, acts as the Guice configuration.
 * @author ilja
 *
 */
class MainModule implements Module {
	/**
	 * Configure the module.
	 */
	@Override
	public void configure(final Binder binder) {
		binder.bindListener(Matchers.any(), new Slf4jTypeListener());
		
		binder.bind(ApplicationState.class).to(ApplicationStateImpl.class);
		binder.bind(TcxImporter.class).to(TcxImporterImpl.class);
		binder.bind(ActivityStorage.class).to(EmptyActivityStorage.class);
		binder.bind(TcxParser.class).to(CommonsDigesterTcxParser.class);
	}
}