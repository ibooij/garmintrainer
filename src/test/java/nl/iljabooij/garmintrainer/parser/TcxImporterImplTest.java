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
package nl.iljabooij.garmintrainer.parser;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

import nl.iljabooij.garmintrainer.importer.ActivityStorage;
import nl.iljabooij.garmintrainer.importer.TcxImportException;
import nl.iljabooij.garmintrainer.importer.TcxImporterImpl;
import nl.iljabooij.garmintrainer.model.Activity;
import nl.iljabooij.garmintrainer.model.ApplicationState;
import nl.iljabooij.garmintrainer.parser.digester.ParseException;
import nl.iljabooij.garmintrainer.testutils.JUnitBaseGuice;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.inject.Provider;

public class TcxImporterImplTest extends JUnitBaseGuice {
	TcxImporterImpl tcxImporterImpl;
	ApplicationState applicationState;
	ActivityStorage activityStorage;
	TcxParser tcxParser;
	Provider<TcxParser> tcxParserProvider;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		applicationState = mock(ApplicationState.class);
		activityStorage = mock(ActivityStorage.class);
		tcxParser = mock(TcxParser.class);
		tcxParserProvider = mock(Provider.class);
		when(tcxParserProvider.get()).thenReturn(tcxParser);

		tcxImporterImpl = new TcxImporterImpl(applicationState,
				activityStorage, tcxParserProvider);
		setupLogger(tcxImporterImpl);
	}

	@Test
	public void testImportTcxIfAlreadyInRepository() {
		File file = mock(File.class);
		Activity activity = mock(Activity.class);

		when(activityStorage.hasActivity(file)).thenReturn(true);
		when(activityStorage.getActivity(file)).thenReturn(activity);

		try {
			tcxImporterImpl.importTcx(file);
		} catch (TcxImportException e) {
			fail("Should not have thrown an exception.");
		}

		verify(applicationState, times(1)).setCurrentActivity(activity);
		verify(activityStorage, times(2)).hasActivity(file);
		verify(activityStorage, times(1)).getActivity(file);
	}

	@Test
	public void testIfNotStorageOneActvity() throws Exception {
		URI uri = getClass().getResource("/sample.tcx").toURI();
		File file = new File(uri);

		ImmutableList<Activity> activities = ImmutableList
				.of(mock(Activity.class));

		when(activityStorage.hasActivity(file)).thenReturn(false);
		when(tcxParser.parse(any(InputStream.class))).thenReturn(activities);
		
		tcxImporterImpl.importTcx(file);

		verify(applicationState, times(1))
				.setCurrentActivity(activities.get(0));
		verify(activityStorage, times(2)).hasActivity(file);
		verify(tcxParser, times(1)).parse(any(InputStream.class));
		verify(activityStorage, times(1)).saveActivity(activities.get(0));
	}

	/**
	 * Test what happens when we're trying to import a non-existent file.
	 * 
	 * @throws TcxImportException
	 *             if everything works as planned.
	 */
	@Test(expected = TcxImportException.class)
	public void testWithNonExistentFile() throws TcxImportException {
		tcxImporterImpl.importTcx(new File("/dhakjdsasa"));
	}

	/**
	 * Test what happens when the parser throws a ParseException
	 * 
	 * @throws TcxImportException
	 *             if everything works as planned.
	 */
	@Test(expected=TcxImportException.class)
	public void testWithParserThrowingException() throws Exception {
		URI uri = getClass().getResource("/sample.tcx").toURI();
		File file = new File(uri);

		when(activityStorage.hasActivity(file)).thenReturn(false);
		when(
				tcxParser.parse((InputStream) argThat(new IsInstanceOf(
						InputStream.class)))).thenThrow(
				new ParseException("test exception"));

		tcxImporterImpl.importTcx(file);
	}

	/**
	 * Test what happens when the parser returns null
	 * 
	 * @throws TcxImportException
	 *             if everything works as planned.
	 */
	@Test
	public void testWithParserReturningNull() throws Exception {
		URI uri = getClass().getResource("/sample.tcx").toURI();
		File file = new File(uri);

		when(activityStorage.hasActivity(file)).thenReturn(false);
		when(tcxParser.parse(any(InputStream.class))).thenReturn(null);

		tcxImporterImpl.importTcx(file);

		verify(applicationState, times(0)).setCurrentActivity(
				any(Activity.class));
		verify(activityStorage, times(1)).hasActivity(file);
		verify(tcxParser, times(1)).parse(any(InputStream.class));

		verify(activityStorage, times(0)).saveActivity(any(Activity.class));
	}

}
