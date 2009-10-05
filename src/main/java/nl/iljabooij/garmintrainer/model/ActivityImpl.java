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
import java.util.LinkedList;
import java.util.List;

import net.jcip.annotations.Immutable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Immutable
public final class ActivityImpl implements Comparable<Activity>, Serializable, Activity {
    private static final long serialVersionUID = 1L;
	private final DateTime startTime;
	private final ImmutableList<Lap> laps;
	
	/**
	 * Create a new {@link ActivityImpl}. 
	 * @param startTime start time of the activity
	 * @param laps laps in the activity, cannot be empty
	 * @throws NullPointerException if any of the arguments are null
	 * @throws IllegalArgumentException if laps is empty.
	 */
	public ActivityImpl(final DateTime startTime, final List<Lap> laps) {
		this.startTime = Preconditions.checkNotNull(startTime);
		Preconditions.checkNotNull(laps);
		Preconditions.checkArgument(!laps.isEmpty(), "laps must not be empty.");
		this.laps = ImmutableList.copyOf(laps);
	}

	/**
	 * {@inheritDoc}
	 */
	public DateTime getStartTime() {
		return startTime;
	}
	
	/** {@inheritDoc} */
	public DateTime getEndTime() {
		return getLastTrackPoint().getTime();	
	}
	
	
	/** {@inheritDoc} */
	public Length getMaximumAltitude() {
		return new TrackPoint.AltitudeOrdering().max(getTrackPoints()).getAltitude();
	}
	
	/** {@inheritDoc} */
	public Length getMinimumAltitude() {
		return new TrackPoint.AltitudeOrdering().min(getTrackPoints()).getAltitude();
	}
	
	/** {@inheritDoc} */
	public Speed getMaximumSpeed() {
		return new TrackPoint.SpeedOrdering().max(getTrackPoints()).getSpeed();
	}
	
	/** {@inheritDoc} */
	public Duration getGrossDuration() {
		return new Duration(startTime, getLastTrackPoint().getTime());
	}
	
	/** {@inheritDoc} */
	public Duration getNetDuration() {
		// start with the difference between start time and first lap start time.
		Duration netDuration = new Duration(startTime, laps.get(0).getStartTime());
		for (Lap lap: laps) {
			netDuration = netDuration.plus(lap.getNetDuration());
		}
		return netDuration;
	}
	
	/** {@inheritDoc} */
	public ImmutableList<TrackPoint> getTrackPoints() {
		ImmutableList.Builder<TrackPoint> builder = ImmutableList.builder();
		for (Lap lap: laps) {
			builder.addAll(lap.getTrackPoints());
		}
		return builder.build();
	}

	/** {@inheritDoc} */
	private TrackPoint getLastTrackPoint() {
		Lap lastLap = Iterables.getLast(laps);
		return Iterables.getLast(lastLap.getTrackPoints());
	}
	
	/** {@inheritDoc} */
	public ImmutableList<Lap> getLaps() {
		return laps;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Activity [start time=" + startTime + ", #trackPoints=" + laps.size() + "]";
	}

	/** {@inheritDoc} */
	public Length getDistance() {
		return getLastTrackPoint().getDistance();
	}
	
	/** {@inheritDoc} */
	public Length getAltitudeGain() {
		final Length minimumGain = Length.createLengthInMeters(5.0);
		Length totalGain = Length.createLengthInMeters(0.0);
		
		// only count as climb if 5 meters are gained without a drop in between.
		// This is a crude way  to filter out any noise.
		LinkedList<TrackPoint> climbingStretch = Lists.newLinkedList();
		for (TrackPoint trackPoint: getTrackPoints()) {
			if (trackPoint.getAltitudeDelta().getValueInMeters() > 0.0) {
				climbingStretch.add(trackPoint);
			} else {
				Length gain = Length.createLengthInMeters(0.0);
				for (TrackPoint climbingTrackPoint: climbingStretch) {
					gain = gain.plus(climbingTrackPoint.getAltitudeDelta());
				}
				if (gain.compareTo(minimumGain) > 0) {
					totalGain = totalGain.plus(gain);
				}
				climbingStretch.clear();
			}
		}
		return totalGain;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof Activity)) {
			return false;
		}
		// compare with interface Activity, not with ActivityImpl!
		Activity other = (Activity) o;
		
		return new EqualsBuilder()
			.append(other.getStartTime(), startTime)
			.append(other.getLaps(), laps)
			.isEquals();	
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(31, 43)
			.append(startTime)
			.append(true)
			.toHashCode();
	}
	
	/** {@inheritDoc} */
    @Override
    public int compareTo(final Activity o) {
        if (o == null) {
            return 1;
        }
        return startTime.compareTo(o.getStartTime());
    }
}
