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

import java.io.Serializable;

import net.jcip.annotations.Immutable;

import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * First track point in the Activity
 * 
 * @author ilja
 * 
 */
@Immutable
public class StartTrackPoint extends TrackPoint implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final DateTime startTime; 
	/**
	 * Create a new {@link StartTrackPoint}. A {@link StartTrackPoint} is used
	 * as the first track point in an activity.
	 * @param startTime start time of the {@link Activity}.
	 * @param time time at which the {@link TrackPoint} was measured.
	 * @param heartRate heart rate at the track point
	 * @param altitude altitude at the track point
	 * @param distance distance at the track point
	 * @param latitude latitude at the track point
	 * @param longitude longitude at the track point.
	 */
	public StartTrackPoint(DateTime startTime, DateTime time, int heartRate,
			Length altitude, Length distance, double latitude, double longitude) {
		super(time, heartRate, altitude, distance, latitude, longitude);
		this.startTime = startTime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Speed getSpeed() {
		final Duration timeTravelled = new Duration(startTime, getTime());
		return Speed.createSpeedInMetersPerSecond(getDistance(), timeTravelled);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Length getAltitudeDelta() {
		// there can be no gain in the first point.
		return Length.createLengthInMeters(0.0);
	}

}
