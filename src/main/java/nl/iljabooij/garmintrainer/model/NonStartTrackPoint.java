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

import org.joda.time.Duration;

import com.google.common.base.Preconditions;

@Immutable
public class NonStartTrackPoint extends TrackPointImpl implements Serializable {
	private static final long serialVersionUID = 2L;
	private final MeasuredTrackPoint previousMeasuredTrackPoint;
	
	public NonStartTrackPoint(final MeasuredTrackPoint previousMeasuredTrackPoint,
			final MeasuredTrackPoint measuredTrackPoint) {
		super(measuredTrackPoint);
		this.previousMeasuredTrackPoint = Preconditions.checkNotNull(previousMeasuredTrackPoint);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Speed getSpeed() {
		Length distanceTravelled = getDistance().minus(previousMeasuredTrackPoint.getDistance());
		Duration timeTravelled = new Duration(previousMeasuredTrackPoint.getTime(), getTime());
		
		return Speed.createSpeedInMetersPerSecond(distanceTravelled, timeTravelled);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Length getAltitudeDelta() {
		return getAltitude().minus(previousMeasuredTrackPoint.getAltitude());
	}
}
