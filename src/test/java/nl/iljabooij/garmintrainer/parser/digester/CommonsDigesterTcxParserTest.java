package nl.iljabooij.garmintrainer.parser.digester;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import nl.iljabooij.garmintrainer.model.Activity;
import nl.iljabooij.garmintrainer.parser.ParseException;
import nl.iljabooij.garmintrainer.testutils.JUnitBaseGuice;

import org.apache.commons.digester.Digester;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.xml.sax.SAXException;

import com.google.common.collect.ImmutableList;
import com.google.inject.Provider;
import com.google.inject.internal.Lists;

public class CommonsDigesterTcxParserTest extends JUnitBaseGuice {
	private CommonsDigesterTcxParser parser;
	private Digester digester;
	Provider<Digester> digesterProvider;
	
	private List<ActivityType> activityTypes;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		digester = mock(Digester.class);
		digesterProvider = mock(Provider.class);
		when(digesterProvider.get()).thenReturn(digester);
		parser = new CommonsDigesterTcxParser(digesterProvider);
		
		setupLogger(parser);
	}

	private void doSetActivityTypes(List<ActivityType> activityTypes) {
		this.activityTypes = activityTypes;
	}
	
	private void doAddActivityTypes(List<ActivityType> activityTypes) {
		this.activityTypes.addAll(activityTypes);
	}
	
	/**
	 * Test normal flow for parse.
	 * @throws Exception
	 */
	@Test
	public void testParse() throws Exception {
		InputStream inputStream = mock(InputStream.class);
		
		// list of activityTypes that will be "parsed" from the input stream 
		final ImmutableList<ActivityType> activityTypes = ImmutableList.of(
				mock(ActivityType.class), mock(ActivityType.class));
		final List<Activity> activities = Lists.newArrayList();
		for(ActivityType activityType: activityTypes) {
			Activity activity = mock(Activity.class);
			activities.add(activity);
			when(activityType.build()).thenReturn(activity);
		}
		
		// when a list is pushed into the digester, register that list into
		// this Test class, so it can be filled later on. We need to do this
		// because of the way Digester works. An object (the list) is pushed
		// into the digester. This object is later referred to in the code
		// calling the digester, but is manipulated by the digester. To be able 
		// to do something with the object, we have to have a reference to it
		// here.
		doAnswer(new Answer<Object>() {
			@SuppressWarnings("unchecked")
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				List<ActivityType> activityTypes = (List<ActivityType>) args[0];
				doSetActivityTypes(activityTypes);
				
				return null;
			}
		}).when(digester).push(any(List.class));
		
		// When the code calling the digester calls Digester.parse(inputStream)
		// we will fill the list that was previously set with some objects of
		// type ActivityType. These are actually mocks that will return
		// a mocked Activity when their build() method is called
		doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				doAddActivityTypes(activityTypes);
				return null;
			}
		}).when(digester).parse(inputStream);
		
		// Do the actual call and verify that we get the correct list of Activity
		// objects returned.
		assertEquals(activities, parser.parse(inputStream));
		
		// verify calls to the mocks we provided
		verify(digesterProvider, times(1)).get();
		verify(digester, times(1)).push(this.activityTypes);
		verify(digester, times(1)).parse(inputStream);
		for (ActivityType activityType: activityTypes) {
			verify(activityType, times(1)).build();
		}
	}
	
	/**
	 * Verify that a NullPointerException is thrown if the provided InputStream
	 * is null.
	 * @throws Exception
	 */
	@Test
	public void testWithNullInputStream() throws Exception {
		try { 
			parser.parse(null);
			fail("NullPointerException should have been thrown");
		} catch (NullPointerException e) {
			// make sure digester was not called. An Exception should have been
			// thrown earlier.
			verify(digester, never()).push(anyObject());
			verify(digester, never()).parse(any(InputStream.class));
		}
	}
	
	/**
	 * Test what happens when the digester throws an IOException. A {@link ParseException}
	 * should be thrown by the parser.
	 * @throws Exception hopefully a {@link ParseException}.
	 */
	@Test(expected=ParseException.class)
	public void testWithDigesterThrowingIOException() throws Exception {
		when(digester.parse(any(InputStream.class))).thenThrow(new IOException());
		
		InputStream inputStream = mock(InputStream.class);
		parser.parse(inputStream);
	}
	
	/**
	 * Test what happens when the digester throws an IOException. A {@link ParseException}
	 * should be thrown by the parser.
	 * @throws Exception hopefully a {@link ParseException}.
	 */
	@Test(expected=ParseException.class)
	public void testWithDigesterThrowingSAXException() throws Exception {
		when(digester.parse(any(InputStream.class))).thenThrow(new SAXException());
		
		InputStream inputStream = mock(InputStream.class);
		parser.parse(inputStream);
	}
}
