package nl.iljabooij.garmintrainer.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * from here: http://onjava.com/pub/a/onjava/2003/08/20/memoization.htmlx
 * @author ilja
 *
 * @param <U>
 */
public final class Memoizer<U> implements InvocationHandler {
	/**
	 * Returns an object that keeps a cache of all values that have been 
	 * returned from any method of the object 
	 * @param <T> Type of object to use as delegate
	 * @param delegate the delegate
	 * @param interfaceToCache only cache calls to this methods in this interface.
	 * @return a caching wrapper for the delegate.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T memoize(T delegate, Class<? super T> interfaceToCache) {
		return (T) Proxy.newProxyInstance(delegate.getClass().getClassLoader(),
				delegate.getClass().getInterfaces(), new Memoizer<T>(delegate, interfaceToCache));
	}

	/** the delegate */
	private U delegate;
	/** only these methods should be cached */
	private ImmutableSet<Method> methodsToCache;
	
	/** ConcurrentMap from Method to another Map. The Map value holds the
	 * returned values for all different arguments lists. Note that if an
	 * object is Memoized that gets calls for many different argument lists, the
	 * cache can become very big!
	 */
	private ConcurrentMap<Method, Map<List<Object>, Object>> caches = new ConcurrentHashMap<Method, Map<List<Object>, Object>>();

	private Memoizer(U object, Class<? super U> interfaceToCache) {
		this.delegate = object;
		this.methodsToCache = ImmutableSet.copyOf(Arrays.asList(interfaceToCache.getMethods()));
	}

	/**
	 * Invoke a method. This method is called "under-water" by {@link Proxy}. It
	 * should not be called directly. 
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		if (!methodsToCache.contains(method) || method.getReturnType().equals(Void.TYPE)) {
			// Don't cache void methods
			return invoke(method, args);
		} else {
			Map<List<Object>, Object> cache = getCache(method);
			List<Object> key;
			if (args == null) {
				key = ImmutableList.of();
			} else {
				key = Arrays.asList(args);
			}
			Object value = cache.get(key);

			if (value == null && !cache.containsKey(key)) {
				value = invoke(method, args);
				cache.put(key, value);
			}
			return value;
		}
	}

	/**
	 * Invoke the actual method on the delegate.
	 * @param method the method to invoke
	 * @param args arguments for the method
	 * @return return value of the method (if any)
	 * @throws Throwable If any exception has been thrown by the invocation, it is
	 * rethrown by this method.
	 */
	private Object invoke(Method method, Object[] args) throws Throwable {
		try {
			return method.invoke(delegate, args);
		} catch (InvocationTargetException e) {
			throw e.getCause();
		}
	}

	/**
	 * Get the cache for a certain method.
	 * @param m method to get cache for
	 * @return the Map holding the cache for the method.
	 */
	private Map<List<Object>, Object> getCache(Method m) {
		Map<List<Object>, Object> cache = caches.get(m);
		if (cache == null) {
			cache = Collections.synchronizedMap(new HashMap<List<Object>, Object>());
			caches.put(m, cache);
		}
		return cache;
	}
}
