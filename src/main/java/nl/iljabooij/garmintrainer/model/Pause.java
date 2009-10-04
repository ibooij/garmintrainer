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

import net.jcip.annotations.Immutable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import com.google.common.base.Preconditions;

/**
 * Identifies a pause
 * @author ilja
 *
 */
@Immutable
public class Pause {
	private final DateTime startTime;
	private final DateTime endTime;
	
	public Pause(final DateTime startTime, final DateTime endTime) {
		Preconditions.checkNotNull(startTime, "startTime");
		Preconditions.checkNotNull(endTime, "endTime");
		Preconditions.checkArgument(startTime.isBefore(endTime), 
				"startTime %s should be before endTime %s", 
				new Object[] {startTime, endTime});
		
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public DateTime getStartTime() {
		return startTime;
	}
	
	public DateTime getEndTime() {
		return endTime;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (getClass() != o.getClass()) {
			return false;
		}
		Pause other = (Pause) o;
		
		return new EqualsBuilder()
			.append(other.startTime, startTime)
			.append(other.endTime, endTime)
			.isEquals();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(23, 41)
			.append(startTime)
			.append(endTime)
			.toHashCode();
	}
}
