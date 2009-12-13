package nl.iljabooij.garmintrainer.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import net.jcip.annotations.Immutable;

import com.google.common.collect.ImmutableSet;

/**
 * This memoizer works a bit different than a normal one. It only caches return
 * values for methods that do not have an argument. We do this to keep things
 * simple. We really only want to use it in the getters of Activity, which all
 * (of course) have no arguments.
 * 
 * Also, this Memoizer only caches the functions that are used from a certain
 * interface, which the wrapped object should implement.
 * 
 * "Stolen" from here:
 * http://onjava.com/pub/a/onjava/2003/08/20/memoization.htmlx
 * 
 * @author ilja
 * 
 * @param <U>
 */
public final class Memoizer<U> implements InvocationHandler {
	static final int RETURN_VALUES_PER_METHOD = 10;

	/**
	 * Returns an object that keeps a cache of all values that have been
	 * returned from any method of the object
	 * 
	 * @param <T>
	 *            Type of object to use as delegate
	 * @param delegate
	 *            the delegate
	 * @param interfaceToCache
	 *            only cache calls to this methods in this interface.
	 * @return a caching wrapper for the delegate.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T memoize(T delegate, Class<? super T> interfaceToCache) {
		return (T) Proxy.newProxyInstance(delegate.getClass().getClassLoader(),
				delegate.getClass().getInterfaces(), new Memoizer<T>(delegate,
						interfaceToCache));
	}

	/** the delegate */
	private U delegate;
	/** only these methods should be cached */
	private ImmutableSet<Method> methodsToCache;

	/**
	 * Only hold one value for a method
	 */
	private ConcurrentHashMap<Method, Holder> cache = new ConcurrentHashMap<Method, Holder>();

	private Memoizer(U object, Class<? super U> interfaceToCache) {
		this.delegate = object;
		this.methodsToCache = ImmutableSet.copyOf(Arrays
				.asList(interfaceToCache.getMethods()));
	}

	/**
	 * Invoke a method. This method is called "under-water" by {@link Proxy}. It
	 * should not be called directly.
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

		if (!methodsToCache.contains(method)
				|| method.getReturnType().equals(Void.TYPE) || args != null) {
			return invoke(method, args);
		} else {
			Holder holder = cache.get(method);
			if (holder == null) {
				Object value = invoke(method, null);
				holder = new Holder(value);
				cache.putIfAbsent(method, new Holder(value));
			}
			return holder.getHeld();
		}
	}
	
	@Immutable
	private class Holder {
		private final Object held;
		
		public Object getHeld() {
			return this.held;
		}
		private Holder(final Object held) {
			this.held = held;
		}
	}

	/**
	 * Invoke the actual method on the delegate.
	 * 
	 * @param method
	 *            the method to invoke
	 * @param args
	 *            arguments for the method
	 * @return return value of the method (if any)
	 * @throws Throwable
	 *             If any exception has been thrown by the invocation, it is
	 *             rethrown by this method.
	 */
	private Object invoke(Method method, Object[] args) throws Throwable {
		try {
			return method.invoke(delegate, args);
		} catch (InvocationTargetException e) {
			throw e.getCause();
		}
	}
}
