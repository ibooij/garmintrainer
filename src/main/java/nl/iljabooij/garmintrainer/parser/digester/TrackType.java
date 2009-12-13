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

import com.google.common.collect.Lists;

public class TrackType {
	private final LinkedList<TrackPointType> trackPointTypes = Lists.newLinkedList();
	
	/**
	 *  Get start time of track
	 *  @return start time of track
	 */
	public DateTime getStartTime() {
		return trackPointTypes.getFirst().getTime();
	}
	
	/**
	 * Get end time of track
	 * @return end time of track
	 */
	public DateTime getEndTime() {
		return trackPointTypes.getLast().getTime();
	}
	
	
	/**
     * Add a {@link TrackPointType} to the lap. the trackPoint will be added
     * as the last track point in the lap.
     * @param trackPoint {@link TrackPointType} to add.
     */
    public void addTrackPoint(final TrackPointType trackPoint) {
        trackPointTypes.add(trackPoint);
    }
    
    public List<TrackPointType> getTrackPointTypes() {
    	return trackPointTypes;
    }
}
