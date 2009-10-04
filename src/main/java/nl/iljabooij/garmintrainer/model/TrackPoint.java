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
public abstract class TrackPoint implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final DateTime time;
	private final int heartRate;
	
	private final Length altitude; 
	private final Length distance;
	private final double latitude;
	private final double longitude;

	public TrackPoint(final DateTime time, final int heartRate, final Length altitude,
			final Length distance, final double latitude, final double longitude) {
		this.time = time;
		this.heartRate = heartRate;
		this.altitude = (altitude == null) ? Length.createLengthInMeters(0.0) : altitude;
		this.distance = (distance == null) ? Length.createLengthInMeters(0.0) : distance;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
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

	public int getHeartRate() {
		return heartRate;
	}

	public Length getAltitude() {
		return altitude;
	}

	public Length getDistance() {
		return distance;
	}

	/**
	 * Get the time at which the sample was taken.
	 * @return the sample time.
	 */
	public DateTime getTime() {
		return time;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	public boolean hasPosition() {
		return (latitude != 0.0 && longitude != 0.0);
	}
	
	/**
	 * Get the speed from the last track point to this one.
	 * @return the Speed.
	 */
	public abstract Speed getSpeed();
	
	/**
	 * Get altitude gained or lost in this track point compared to the last
	 * @return the altitude delta
	 */
	public abstract Length getAltitudeDelta();
}
