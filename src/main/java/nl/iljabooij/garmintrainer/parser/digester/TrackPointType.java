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
package nl.iljabooij.garmintrainer.parser.digester;

import nl.iljabooij.garmintrainer.model.Length;
import nl.iljabooij.garmintrainer.model.NonStartTrackPoint;
import nl.iljabooij.garmintrainer.model.StartTrackPoint;
import nl.iljabooij.garmintrainer.model.TrackPoint;

import org.joda.time.DateTime;

/**
 * Builder for {@link TrackPoint}. Because {@link TrackPoint} is an unmodifiable
 * object, we need a builder if we want to build a {@link TrackPoint}
 * incrementally, as with commons-digester.
 * 
 * @author "Ilja Booij <ibooij@gmail.com>"
 * 
 */
public class TrackPointType {
	private DateTime time;
	private int heartRate;
	private Length altitude;
	private Length distance;
	private double latitude;
	private double longitude;

	/**
	 * Build a TrackPoint
	 * 
	 * @param previous the previous track point
	 * @return the {@link TrackPoint} with all fields filled out.
	 */
	public NonStartTrackPoint buildNonStartTrackPoint(final TrackPoint previous) {
		return new NonStartTrackPoint(previous, time, heartRate,
				altitude, distance, latitude, longitude);
	}
	
	/**
	 * Build a start track point.
	 * 
	 * @param startTime time of the start of the Activity
	 * @return the StartTrackPoint.
	 */
	public StartTrackPoint buildStartTrackPoint(final DateTime startTime) {
		return new StartTrackPoint(startTime, time, heartRate,
				altitude, distance, latitude, longitude);
	}

	/**
	 * Set the time at which the measurement took place.
	 * 
	 * @param time
	 *            the time
	 */
	public void setTime(final DateTime time) {
		this.time = time;
	}

	/**
	 * Set the heart rate that was measured.
	 * 
	 * @param heartRate
	 *            the measured heart rate in beats per minute
	 */
	public void setHeartRate(final int heartRate) {
		this.heartRate = heartRate;
	}

	/**
	 * Set the measured altitude.
	 * 
	 * @param altitude
	 *            the altitude that was measured.
	 */
	public void setAltitude(final Length altitude) {
		this.altitude = altitude;
	}

	/**
	 * Set the measured distance.
	 * 
	 * @param distance
	 *            the distance that was measured.
	 */
	public void setDistance(final Length distance) {
		this.distance = distance;
	}

	/**
	 * Set the measured latitude.
	 * 
	 * @param latitude
	 *            in degrees
	 */
	public void setLatitude(final double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Set the measured longitude.
	 * 
	 * @param longitude
	 *            in degrees
	 */
	public void setLongitude(final double longitude) {
		this.longitude = longitude;
	}

	// We need these getters because commons-digester needs to build real beans.

	/**
	 * Get measurement time of the track point.
	 * 
	 * @return the time at which the track point was measured.
	 */
	public DateTime getTime() {
		return time;
	}

	/**
	 * Get the heart rate at the track point.
	 * 
	 * @return the heart rate at the track point
	 */
	public int getHeartRate() {
		return heartRate;
	}

	/**
	 * Get the altitude at the track point.
	 * 
	 * @return the altitude at the track point
	 */
	public Length getAltitude() {
		return altitude;
	}

	/**
	 * Get the distance at the track point.
	 * 
	 * @return the distance at the track point
	 */
	public Length getDistance() {
		return distance;
	}

	/**
	 * Get the latitude at the track point.
	 * 
	 * @return the latitude at the track point
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Get the longitude at the track point.
	 * 
	 * @return the longitude at the track point
	 */
	public double getLongitude() {
		return longitude;
	}
}