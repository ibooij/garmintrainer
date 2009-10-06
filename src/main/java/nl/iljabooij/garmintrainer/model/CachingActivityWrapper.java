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
package nl.iljabooij.garmintrainer.model;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.google.common.collect.ImmutableList;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Wrapper around an activity that caches accesses to an {@link Activity}. 
 * Because {@link Activity} is supposed to be immutable, we can easily cache
 * the results in this object. The reason for this class is that some
 * method calls, like {@link #getTrackPoints()} can be pretty expensive. 
 * Fortunately, because Activity implementations should be stateless, we 
 * can easily cache the results and used the cached results from thereon.
 * 
 * @author ilja
 */
public class CachingActivityWrapper implements Activity {
	// the wrapped activity
	private final Activity delegate;
	
	// by using volatiles, we indicate that we're ok with the fact that
	// two threads might be calculating and setting values at the same time.
	// we do not care about this as this is pretty rare anyway.
	private volatile Length altitudeGain = null;
	private volatile Length maxAltitude = null;
	private volatile Length minAltitude = null;
	private volatile Speed maxSpeed = null;
	private volatile ImmutableList<TrackPoint> trackPoints = null;

	/**
	 * Create a new {@link CachingActivityWrapper} for an Activity.
	 * @param activity the {@link Activity} to wrap
	 */
	public CachingActivityWrapper(final Activity activity) {
		this.delegate = checkNotNull(activity);
	}

	/**
	 * This method returns the result from {@link Activity#getAltitudeGain()}, but
	 * because that is an expensive call, it cache the result and return the
	 * cached result if available.
	 * {@inheritDoc}
	 */
	@Override
	public Length getAltitudeGain() {
		if (altitudeGain == null) {
			altitudeGain = delegate.getAltitudeGain();
		}
		assert(altitudeGain != null);
		
		return altitudeGain;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Length getDistance() {
		// is quite a cheap call, don't cache. It uses getTrackPoints, which we
		// do cache.
		return delegate.getDistance();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DateTime getEndTime() {
		// is quite a cheap call, don't cache. It uses getTrackPoints, which we
		// do cache.
		return delegate.getEndTime();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Duration getGrossDuration() {
		// is quite a cheap call, don't cache. It uses getTrackPoints, which we
		// do cache.
		return delegate.getGrossDuration();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ImmutableList<Lap> getLaps() {
		return delegate.getLaps();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Length getMaximumAltitude() {
		if (maxAltitude == null) {
			maxAltitude = delegate.getMaximumAltitude();
		}
		assert(maxAltitude != null);
		return maxAltitude;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Speed getMaximumSpeed() {
		if (maxSpeed == null) {
			maxSpeed = delegate.getMaximumSpeed();
		}
		assert(maxSpeed != null);
		return maxSpeed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Length getMinimumAltitude() {
		if (minAltitude == null) {
			minAltitude = delegate.getMinimumAltitude();
		}
		assert(minAltitude != null);
		return minAltitude;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Duration getNetDuration() {
		return delegate.getNetDuration();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DateTime getStartTime() {
		return delegate.getStartTime();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ImmutableList<TrackPoint> getTrackPoints() {
		if (trackPoints == null) {
			trackPoints = delegate.getTrackPoints();
		}
		assert(trackPoints != null);
		return trackPoints;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Activity o) {
		return delegate.compareTo(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return delegate.equals(obj);
	}
	/**
	 * {@inheritDoc}
	 * @see ActivityImpl#toString()
	 */
	@Override
	public String toString() {
		return "CachingActivityWrapper[" + delegate.toString() + "]";
	}
}
