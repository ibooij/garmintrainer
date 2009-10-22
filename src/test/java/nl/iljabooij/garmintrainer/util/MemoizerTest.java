package nl.iljabooij.garmintrainer.util;


import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link Memoizer}. 
 * @author ilja
 */
public class MemoizerTest {
	private interface BaseInterface {
		int methodWithReturnValue();
		int methodWithArgument(int i);
		Object methodReturningNull();
	}
	
	private interface ExtendedInterface extends BaseInterface {
		void methodWithoutReturnValue();
		int anotherMethod();
	}
	
	private ExtendedInterface impl;
	private ExtendedInterface memoizedImpl;

	@Before
	public void setUp() throws Exception {
		impl = mock(ExtendedInterface.class);
		memoizedImpl = Memoizer.memoize(impl, BaseInterface.class);
	}

	/**
	 * Methods from the {@link ExtendedInterface} should not be cached.
	 */
	@Test
	public void testFromExtendInterface() {
		memoizedImpl.methodWithoutReturnValue();
		memoizedImpl.methodWithoutReturnValue();
		verify(impl, times(2)).methodWithoutReturnValue();
		
		when(impl.anotherMethod()).thenReturn(1, 2);
		assertEquals(1, memoizedImpl.anotherMethod());
		assertEquals(2, memoizedImpl.anotherMethod());
		
		verify(impl, times(2)).anotherMethod();
	}
	
	@Test
	public void testFromBaseInterfaceWithReturnValue() {
		when(impl.methodWithReturnValue()).thenReturn(1,2);
		assertEquals(1, memoizedImpl.methodWithReturnValue());
		// should still return 1, even though impl would return 2 on invocation.
		assertEquals(1, memoizedImpl.methodWithReturnValue());
		verify(impl, times(1)).methodWithReturnValue();
	}
	
	@Test
	public void testFromBaseInterfaceReturningNull() {
		when(impl.methodReturningNull()).thenReturn(null);
		assertEquals(null, memoizedImpl.methodReturningNull());
		assertEquals(null, memoizedImpl.methodReturningNull());
		verify(impl, times(1)).methodReturningNull();
	}
	
	@Test 
	public void testIfExceptionThrown() {
		Throwable t = new RuntimeException("just a runtime exception");
		when(impl.methodWithReturnValue()).thenThrow(t);
		
		boolean runtimeExceptionThrown = false;
		try {
			memoizedImpl.methodWithReturnValue();
			fail("Exception should have been thrown");
		} catch (RuntimeException e) {
			assertEquals(t, e);
			runtimeExceptionThrown = true;
		} 
		assertTrue("runtime exception should have been thrown", runtimeExceptionThrown);
		verify(impl, times(1)).methodWithReturnValue();
	}
	
	/**
	 * Memoizer should not cache methods with arguments.
	 */
	@Test
	public void testFromBaseInterfaceWithArgument() {
		when(impl.methodWithArgument(1)).thenReturn(1);
		when(impl.methodWithArgument(2)).thenReturn(2);
		assertEquals(1, memoizedImpl.methodWithArgument(1));
		assertEquals(2, memoizedImpl.methodWithArgument(2));
		assertEquals(2, memoizedImpl.methodWithArgument(2));
	
		verify(impl, times(3)).methodWithArgument(anyInt());
		verify(impl, times(1)).methodWithArgument(1);
		verify(impl, times(2)).methodWithArgument(2);
	}
}
