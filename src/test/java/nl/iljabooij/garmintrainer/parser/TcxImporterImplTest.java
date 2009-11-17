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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

import nl.iljabooij.garmintrainer.importer.TcxImportException;
import nl.iljabooij.garmintrainer.importer.TcxImporterImpl;
import nl.iljabooij.garmintrainer.model.Activity;
import nl.iljabooij.garmintrainer.model.ApplicationState;
import nl.iljabooij.garmintrainer.testutils.JUnitBaseGuice;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.inject.Provider;

public class TcxImporterImplTest extends JUnitBaseGuice {
	TcxImporterImpl tcxImporterImpl;
	ApplicationState applicationState;
	TcxParser tcxParser;
	Provider<TcxParser> tcxParserProvider;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		applicationState = mock(ApplicationState.class);
		tcxParser = mock(TcxParser.class);
		tcxParserProvider = mock(Provider.class);
		when(tcxParserProvider.get()).thenReturn(tcxParser);

		tcxImporterImpl = new TcxImporterImpl(applicationState, tcxParserProvider);
		setupLogger(tcxImporterImpl);
	}

	@Test
	public void testImportActvity() throws Exception {
		URI uri = getClass().getResource("/sample.tcx").toURI();
		File file = new File(uri);

		ImmutableList<Activity> activities = ImmutableList
				.of(mock(Activity.class));

		when(tcxParser.parse(any(InputStream.class))).thenReturn(activities);
		
		tcxImporterImpl.importTcx(file);

		verify(applicationState, times(1))
				.setCurrentActivity(activities.get(0));
		verify(tcxParser, times(1)).parse(any(InputStream.class));
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

		when(tcxParser.parse(any(InputStream.class))).thenReturn(null);

		tcxImporterImpl.importTcx(file);

		verify(applicationState, times(0)).setCurrentActivity(
				any(Activity.class));
		verify(tcxParser, times(1)).parse(any(InputStream.class));
	}

}
