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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * Represents a single track point sample.
 * @author "Ilja Booij <ibooij@gmail.com>"
 *
 */
@Immutable
public abstract class TrackPointImpl implements Serializable, TrackPoint {
	private static final long serialVersionUID = 1L;
	
	private final DateTime time;
	private final int heartRate;
	
	private final Length altitude; 
	private final Length distance;
	private final double latitude;
	private final double longitude;

	public TrackPointImpl(final DateTime time, final int heartRate, final Length altitude,
			final Length distance, final double latitude, final double longitude) {
		this.time = time;
		this.heartRate = heartRate;
		this.altitude = (altitude == null) ? Length.createLengthInMeters(0.0) : altitude;
		this.distance = (distance == null) ? Length.createLengthInMeters(0.0) : distance;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("time", time)
			.append("distance", distance)
			.append("altitude", altitude)
			.append("heart rate", heartRate)
			.append("latitude", latitude)
			.append("longitude", longitude)
			.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getHeartRate() {
		return heartRate;
	}

	/**
	 * {@inheritDoc}
	 */
	public Length getAltitude() {
		return altitude;
	}

	/**
	 * {@inheritDoc}
	 */
	public Length getDistance() {
		return distance;
	}

	/**
	 * {@inheritDoc}
	 */
	public DateTime getTime() {
		return time;
	}

	/**
	 * {@inheritDoc}
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * {@inheritDoc}
	 */
	public double getLongitude() {
		return longitude;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasPosition() {
		return (latitude != 0.0 && longitude != 0.0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public abstract Speed getSpeed();
	
	/**
	 * {@inheritDoc}
	 */
	public abstract Length getAltitudeDelta();
}
