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

import com.google.common.base.Preconditions;

/**
 * Represents a single track point sample.
 * @author "Ilja Booij <ibooij@gmail.com>"
 *
 */
@Immutable
public abstract class TrackPointImpl implements Serializable, TrackPoint {
	private static final long serialVersionUID = 2L;
	
	private final MeasuredTrackPoint measuredTrackPoint;
	
	public TrackPointImpl(final MeasuredTrackPoint measuredTrackPoint) {
		this.measuredTrackPoint = Preconditions.checkNotNull(measuredTrackPoint);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return measuredTrackPoint.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getHeartRate() {
		return measuredTrackPoint.getHeartRate();
	}

	/**
	 * {@inheritDoc}
	 */
	public Length getAltitude() {
		return measuredTrackPoint.getAltitude();
	}

	/**
	 * {@inheritDoc}
	 */
	public Length getDistance() {
		return measuredTrackPoint.getDistance();
	}

	/**
	 * {@inheritDoc}
	 */
	public DateTime getTime() {
		return measuredTrackPoint.getTime();
	}

	/**
	 * {@inheritDoc}
	 */
	public double getLatitude() {
		return measuredTrackPoint.getLatitude();
	}

	/**
	 * {@inheritDoc}
	 */
	public double getLongitude() {
		return measuredTrackPoint.getLongitude();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean hasPosition() {
		return (getLatitude() != 0.0 && getLongitude() != 0.0);
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
