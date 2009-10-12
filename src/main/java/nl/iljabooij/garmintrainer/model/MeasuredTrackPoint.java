package nl.iljabooij.garmintrainer.model;

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
import org.joda.time.DateTime;

/**
 * Track point as measured by the GPS device.
 * @author ilja
 *
 */
public interface MeasuredTrackPoint {
	/**
	 * Get measured heart rate.
	 * @return the hear rate
	 */
	int getHeartRate();

	/**
	 * Get measured altitude.
	 * @return the altitude
	 */
	Length getAltitude();

	/**
	 * Get measured distance.
	 * @return the distance
	 */
	Length getDistance();

	/**
	 * Get the time at which the sample was taken.
	 * @return the sample time.
	 */
	DateTime getTime();

	/**
	 * Get the measured latitude.
	 * @return the latitude.
	 */
	double getLatitude();

	/**
	 * Get the measured longitude.
	 * @return the longitude.
	 */
	double getLongitude();
}
