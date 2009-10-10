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

import java.util.ArrayList;
import java.util.List;

import nl.iljabooij.garmintrainer.model.Activity;
import nl.iljabooij.garmintrainer.model.ActivityImpl;
import nl.iljabooij.garmintrainer.model.Lap;
import nl.iljabooij.garmintrainer.model.Track;
import nl.iljabooij.garmintrainer.model.TrackPointImpl;
import nl.iljabooij.garmintrainer.util.Memoizer;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.google.common.collect.Lists;

/**
 * Builder for {@link Activity}.
 * 
 * @author "Ilja Booij"
 */
public class ActivityType {
    /** id of activity. */
    private String id;
    /** list of lap builder to build Laps from. */
    private final List<LapType> lapBuilders = Lists.newArrayList();

    private static DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis();
    
    /**
     * Build the activity.
     * @return a new Activity
     */
    public Activity build() {
    	final DateTime dateTimeForId = dateTimeFormatter.parseDateTime(id);
    	
    	TrackPointImpl previousTrackPoint = null;
    	ArrayList<Lap> laps = Lists.newArrayList();
        for (LapType lapType: lapBuilders) {
        	final ArrayList<Track> tracks = Lists.newArrayList();
        	for (TrackType trackType: lapType.getTracks()) {
        		final ArrayList<TrackPointImpl> trackPoints = Lists.newArrayList();
        		for (TrackPointType trackPointType: trackType.getTrackPointTypes()) {
        			TrackPointImpl newTrackPoint;
        			if (laps.isEmpty() && tracks.isEmpty() && trackPoints.isEmpty()) {
        				newTrackPoint = trackPointType.buildStartTrackPoint(dateTimeForId);				
        			} else {
        				newTrackPoint = trackPointType.buildNonStartTrackPoint(previousTrackPoint);
        			}
        			trackPoints.add(newTrackPoint);
        			previousTrackPoint = newTrackPoint;
        		}
        		tracks.add(new Track(trackPoints));
        	}
        	laps.add(new Lap(lapType.getStartTime(), tracks));
        }
         
        return Memoizer.memoize(new ActivityImpl(dateTimeForId, laps), Activity.class);
        
//        return (Activity) memoizer;
//        return new CachingActivityWrapper(new ActivityImpl(dateTimeForId, laps));
    }

    /**
     * Add a {@link LapType} to the {@link ActivityType}.
     * @param lapBuilder the {@link LapType} to add.
     * @return the {@link ActivityType}.
     */
    public ActivityType addLap(final LapType lapBuilder) {
        lapBuilders.add(lapBuilder);
        return this;
    }

    /** Get the id which as set.
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Set the id of the activity builder. This will become the id of the
     * {@link Activity} which will be built by the {@link #build()} method.
     * @param id the id of the {@link ActivityType}.
     */
    public void setId(final String id) {
        this.id = id;
    }
}
