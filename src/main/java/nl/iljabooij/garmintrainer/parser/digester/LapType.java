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

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * Builder for Lap Objects.
 * 
 * @author "Ilja Booij <ibooij@gmail.com>"
 * 
 */
public final class LapType {
	private static final Logger LOGGER = LoggerFactory.getLogger(LapType.class);

	private LinkedList<TrackType> tracks = Lists.newLinkedList();
	private DateTime startTime;

	/**
	 * Get the start time.
	 * 
	 * @return the start time
	 */
	public DateTime getStartTime() {
		return startTime;
	}
	
	public DateTime getEndTime() {
		return tracks.getLast().getEndTime();
	}

	/**
	 * Set the start time.
	 * 
	 * @param startTime
	 *            start time to set.
	 */
	public void setStartTime(final DateTime startTime) {
		this.startTime = startTime;
	}

	/**
	 * Add a {@link TrackType} to the lap. the {@link TrackType} will be added
	 * as the last track in the lap.
	 * 
	 * @param track
	 *            {@link TrackPointType} to add.
	 */
	public void addTrack(final TrackType track) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Adding Track with {} track points", track
					.getTrackPointTypes().size());
		}
		tracks.add(track);
	}

	/**
	 * Get all tracks for the lap.
	 * @return the laps
	 */
	public List<TrackType> getTracks() {
		return tracks;
	}
	
	public List<TrackPointType> getTrackPoints() {
		List<TrackPointType> trackPoints = Lists.newArrayList();
		for (TrackType track : tracks) {
			trackPoints.addAll(track.getTrackPointTypes());
		}
		return trackPoints;
	}
}