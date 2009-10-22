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

import static com.google.common.base.Preconditions.*;

/**
 * Interpolated Track Point.
 * 
 * This kind of track point is used when we need a track point between to "real"
 * track points. Return values for the methods will be calculated using two
 * track points measured from around the time for the Interpolated Track Point.
 * 
 * @author ilja
 * 
 */
public class InterpolatedTrackPoint implements TrackPoint {
	private final TrackPoint begin;
	private final TrackPoint end;
	private final DateTime time;

	/**
	 * Create a new {@link InterpolatedTrackPoint} from a time, a begin track
	 * point and an end track point.
	 * 
	 * @param time
	 *            the time for the track point.
	 * @param begin
	 *            the track point beginning the interval.
	 * @param end
	 *            the track point ending the interval.
	 * @throws NullPointerException
	 *             if time, begin or end are null
	 * @throws IllegalArgumentException
	 *             if time is before begin.getTime() or after end.getTime()
	 */
	InterpolatedTrackPoint(final DateTime time, final TrackPoint begin,
			final TrackPoint end) {
		checkNotNull(time);
		checkNotNull(begin);
		checkNotNull(end);
		checkArgument((!time.isBefore(begin.getTime()) && time.isBefore(end
				.getTime())), "Time should be between %s and %s, but is %s",
				begin.getTime(), end.getTime(), time);

		this.time = time;
		this.begin = begin;
		this.end = end;
	}

	public TrackPoint getBegin() {
		return begin;
	}

	public TrackPoint getEnd() {
		return end;
	}

	private double calculateTimeRatio() {
		final double completeInterval = (double) calculateIntervalDuration()
				.getMillis();
		final double partInterval = (double) calculateDurationFromBegin()
				.getMillis();

		return partInterval / completeInterval;
	}

	private Duration calculateIntervalDuration() {
		return new Duration(begin.getTime(), end.getTime());
	}

	private Duration calculateDurationFromBegin() {
		return new Duration(begin.getTime(), time);
	}

	@Override
	public Length getAltitude() {
		return begin.getAltitude().plus(
				(end.getAltitude().minus(begin.getAltitude())
						.times(calculateTimeRatio())));
	}

	@Override
	public Length getAltitudeDelta() {
		return getAltitude().minus(begin.getAltitude());
	}

	@Override
	public Length getDistance() {
		return begin.getDistance().plus(
				(end.getDistance().minus(begin.getDistance())
						.times(calculateTimeRatio())));
	}

	@Override
	public int getHeartRate() {
		return (int) (begin.getHeartRate() + (end.getHeartRate() - begin
				.getHeartRate())
				* calculateTimeRatio());
	}

	@Override
	public double getLatitude() {
		if (!begin.hasPosition()) {
			return end.getLatitude();
		} else if (!end.hasPosition()) {
			return begin.getLatitude();
		} else {
			return begin.getLatitude()
					+ (end.getLatitude() - begin.getLatitude())
					* calculateTimeRatio();
		}
	}

	@Override
	public double getLongitude() {
		if (!begin.hasPosition()) {
			return end.getLongitude();
		} else if (!end.hasPosition()) {
			return begin.getLongitude();
		} else {
			return begin.getLongitude()
					+ (end.getLongitude() - begin.getLongitude())
					* calculateTimeRatio();
		}
	}

	@Override
	public Speed getSpeed() {
		return Speed.createSpeedInMetersPerSecond(getDistance().minus(
				begin.getDistance()), calculateDurationFromBegin());
	}

	@Override
	public DateTime getTime() {
		return time;
	}

	@Override
	public boolean hasPosition() {
		return (begin.hasPosition() || end.hasPosition());

	}

}
