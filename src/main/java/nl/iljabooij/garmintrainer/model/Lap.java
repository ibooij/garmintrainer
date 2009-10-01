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

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

/**
 * Represents a Lap. A Lap is defined by it's start and end time.
 * @author ilja
 *
 */
public class Lap implements Serializable {
	private static final long serialVersionUID = 1L;
	private final DateTime startTime;
	private final ImmutableList<Track> tracks;
	
	public Lap(final DateTime startTime, final List<Track> tracks) {
		this.startTime = startTime;
		this.tracks = ImmutableList.copyOf(tracks);
	}
	
	/**
	 * Get Start time of the lap.
	 * @return start time
	 */
	public DateTime getStartTime() {
		return startTime;
	}
	
	/**
	 * Get End time of the lap
	 */
	public DateTime getEndTime() {
		Track lastTrack = Iterables.getLast(tracks);
		return lastTrack.getEndTime();
	}
	
	/**
	 * Get all track points in the lap
	 * @return list of all track points.
	 */
	public ImmutableList<TrackPoint> getTrackPoints() {
		ImmutableList.Builder<TrackPoint> builder = ImmutableList.builder();
		for (Track track: tracks) {
			builder.addAll(track.getTrackPoints());
		}
		return builder.build();
	}

	/**
	 * Get a list of all pauses in the lap.
	 * @return list of all pauses.
	 */
	public ImmutableList<Pause> getPauses() {
		ImmutableList.Builder<Pause> pausesBuilder = ImmutableList.builder();
		PeekingIterator<Track> it = Iterators.peekingIterator(tracks.iterator());
		while (it.hasNext()) {
			Track former = it.next();
			Track latter;
			if (it.hasNext()) {
				latter = it.peek();
			} else {
				break;
			}
			Duration gap = new Duration(former.getEndTime(), latter.getStartTime());
			if (gap.isLongerThan(Duration.standardSeconds(10))) {
				pausesBuilder.add(new Pause(former.getEndTime(), latter.getStartTime()));
			}
		}
		return pausesBuilder.build();
	}
	
	/**
	 * Get gross duration of Lap. That is, the total time from start of the lap 
	 * to the end.
	 * @return gross duration of the lap
	 */
	public Duration getGrossDuration() {
		return new Duration(getStartTime(), getEndTime());
	}
	
	/**
	 * Get net duration of Lap. That is, the time from start to end, minus the
	 * pauses.
	 * @return net duration of lap.
	 */
	public Duration getNetDuration() {
		Duration duration = new Duration(startTime, tracks.get(0).getStartTime());
		for (Track track: tracks) {
			duration = duration.plus(track.getDuration());
		}
		return duration;
	}
}
