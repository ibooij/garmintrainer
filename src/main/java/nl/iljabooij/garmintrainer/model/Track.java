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
import java.util.List;

import net.jcip.annotations.Immutable;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

/**
 * a Track is a stretch of a Lap that is recorded without interruption.
 * @author ilja
 */
@Immutable
public class Track implements Serializable {
	private static final long serialVersionUID = 1L;
	private ImmutableList<TrackPoint> trackPoints;
	
	/**
	 * Create a new Track with a list of track points.
	 * @param trackPoints trackPoints for the Track
	 * @throws NullPointerException if trackPoints is null
	 * @throws IllegalArgumentException if trackPoints is empty
	 */
	public Track(final List<TrackPoint> trackPoints) {
		Preconditions.checkNotNull(trackPoints);
		Preconditions.checkArgument(!trackPoints.isEmpty());
		
		this.trackPoints = ImmutableList.copyOf(trackPoints);
	}
	
	/**
	 * Get all track points
	 * @return the track points.
	 */
	public ImmutableList<TrackPoint> getTrackPoints() {
		return trackPoints;
	}

	/**
	 * Get start time of track.
	 * @return the start time
	 */
	public DateTime getStartTime() {
		return trackPoints.get(0).getTime();
	}

	/** 
	 * Get end time of track.
	 * @return the end time.
	 */
	public DateTime getEndTime() {
		return Iterables.getLast(trackPoints).getTime();
	}

	/**
	 * Get duration of track.
	 * @return the duration
	 */
	public Duration getDuration() {
		return new Duration(getStartTime(), getEndTime());
	}
}
