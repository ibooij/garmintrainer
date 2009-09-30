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

import org.joda.time.DateTime;
import org.joda.time.Duration;

import static com.google.common.base.Preconditions.checkNotNull;

public class NonStartTrackPoint extends TrackPoint implements Serializable {
	private static final long serialVersionUID = 1L;
	private final TrackPoint previousTrackPoint;
	
	public NonStartTrackPoint(final TrackPoint previousTrackPoint,DateTime time, int heartRate, Length altitude,
			Length distance, double latitude, double longitude) {
		super(time, heartRate, altitude, distance, latitude, longitude);
		this.previousTrackPoint = checkNotNull(previousTrackPoint);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Speed getSpeed() {
		Length distanceTravelled = getDistance().substract(previousTrackPoint.getDistance());
		Duration timeTravelled = new Duration(previousTrackPoint.getTime(), getTime());
		
		return Speed.createSpeedInMetersPerSecond(distanceTravelled, timeTravelled);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Length getAltitudeDelta() {
		return getAltitude().substract(previousTrackPoint.getAltitude());
	}
}
