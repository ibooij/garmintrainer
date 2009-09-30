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
package nl.iljabooij.garmintrainer.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import nl.iljabooij.garmintrainer.model.Activity;
import nl.iljabooij.garmintrainer.model.ApplicationState;
import nl.iljabooij.garmintrainer.parser.TcxParser;
import nl.iljabooij.garmintrainer.parser.digester.ParseException;
import nl.iljabooij.garmintrainer.util.InjectLogger;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class TcxImporterImpl implements TcxImporter {
	@InjectLogger private Logger logger;
	
	@Inject
	private ApplicationState applicationState;

	@Inject
	private ActivityStorage activityStorage;
	
	/**
	 * tcxParserProvider acts as a factory for {@link TcxParser}s. It's completely
	 * created by Guice, nothing we have to do about it (yes, I'm lazy).
	 */
	@Inject
	private Provider<TcxParser> tcxParserProvider;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void importTcx(final File tcxFile) throws TcxImportException {
		Activity activity;
		if (activityStorage.hasActivity(tcxFile)) {
			logger.debug("file is already in cache, loading from cache");
			activity = activityStorage.getActivity(tcxFile);
		} else {
			logger.debug("not cached!");
			activity = loadAndParse(tcxFile);
		}

		if (activity != null) {
			applicationState.setCurrentActivity(activity);
			
			if (!activityStorage.hasActivity(tcxFile)) {
				logger.debug("saving activity to disk");
				activityStorage.saveActivity(activity);
			}
		}

	}

	private Activity loadAndParse(final File tcxFile) throws TcxImportException {
		List<Activity> activities = null;
		try {
			final FileInputStream fileInputStream = new FileInputStream(tcxFile);
			try {
				activities = tcxParserProvider.get().parse(fileInputStream);
			} catch (ParseException e) {
				logger.error("Error parsing tcx file", e);
				throw new TcxImportException("Exception parsing tcx file", e);
			} finally {
				fileInputStream.close();
			}
		} catch (FileNotFoundException ex) {
			logger.error("Could not find tcx file: {}", tcxFile);
			throw new TcxImportException("File not found", ex);
		} catch (IOException ex) {
			logger.error("IOException opening and parsing tcx file", ex);
			throw new TcxImportException("IOException opening and parsing tcx file", ex);
		}
		if (activities == null || activities.isEmpty()) {
			return null;
		} else {
			return activities.get(0);
		}
	}

}
