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
import java.text.DecimalFormat;
import java.text.NumberFormat;

import net.jcip.annotations.Immutable;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.joda.time.Duration;

@Immutable
public final class Speed implements Comparable<Speed>, Serializable {
    private static final long serialVersionUID = 1L;

    public enum Unit {
		// m/s
		MetersPerSecond(Length.Unit.Meter.conversionValue, 1, 1, "m/s"),
		// km/h
		KilometersPerHour(Length.Unit.Kilometer.conversionValue, 3600, 2,
				"km/h");

		public final double conversionValue;
		public final int scale;
		public final String suffix;
		public final NumberFormat format;
		
		private Unit(final double lengthConversion,
				final int toSecondConversion, final int scale,
				final String suffix) {
			conversionValue = toSecondConversion / lengthConversion;
			this.scale = scale;
			this.suffix = suffix;
			
			final StringBuffer sb = new StringBuffer("#0.");
			for(int i = 0; i < scale; i++) {
				sb.append('0');
			}
			format = new DecimalFormat(sb.toString());
		}
	}

	// stores the speed in m/s
	private final double value;
	private final Unit unit;

	private Speed(final double value) {
		this.value = value;
		this.unit = Unit.MetersPerSecond;
	}

	private Speed(final double value, final Unit unit) {
		this.value = value / unit.conversionValue;
		this.unit = unit;
	}

	private Speed(final Length length, final Duration duration, final Unit unit) {
		final double seconds = duration.getMillis() * 0.001;
		
		value = length.getValueInMeters() / seconds;

		this.unit = unit;
	}

	/**
	 * Create a new Speed object with an exact value in meters per second
	 * 
	 * @param metersPerSecond
	 *            the value of the speed
	 * @return the Speed object.
	 */
	public static Speed createExactSpeedInMetersPerSecond(
			final double metersPerSecond) {
		return new Speed(metersPerSecond);
	}

	/**
	 * Create new Speed object with an exact value in the specified unit
	 * 
	 * @param value
	 *            the value of the speed
	 * @param unit
	 *            the unit
	 * @return the Speed object.
	 */
	public static Speed createExactSpeed(final double value,
			final Unit unit) {
		return new Speed(value, unit);
	}

	/**
	 * Create a new Speed object from a length and a duration. The {@link Speed}
	 * will be given in Meters per second
	 * 
	 * @param length
	 *            distance traveled
	 * @param duration
	 *            distance traveled
	 * @return the Speed object.
	 */
	public static Speed createSpeedInMetersPerSecond(final Length length,
			final Duration duration) {
		if (length == null)
			throw new NullPointerException("length value cannot be null");
		if (duration == null)
			throw new NullPointerException("duration cannot be null");

		return new Speed(length, duration, Unit.MetersPerSecond);
	}

	/**
	 * Create a new Speed object from a length and a duration. The {@link Speed}
	 * will be given in the {@link Unit}.
	 * 
	 * @param length
	 *            distance traveled
	 * @param duration
	 *            distance traveled
	 * @param unit
	 *            unit to give Speed in.
	 * @return the Speed object
	 */
	public static Speed createSpeed(final Length length,
			final Duration duration, final Unit unit) {
		if (length == null)
			throw new NullPointerException("length value cannot be null");
		if (duration == null)
			throw new NullPointerException("duration cannot be null");
		if (unit == null)
			throw new NullPointerException("unit cannot be null");

		return new Speed(length, duration, unit);
	}

	public Speed convert(final Unit unit) {
		return Speed.createExactSpeed(value * unit.conversionValue, unit);
	}

	/**
	 * Get the value of the Speed in m/s
	 * 
	 * @return the speed in m/s
	 */
	public double getValueInMetersPerSecond() {
		return value;
	}

	/**
	 * Get the value of the Speed in the specified unit
	 * 
	 * @param unit
	 *            the unit to specify the speed in
	 * @return the speed in m/s
	 */
	public double getValue(final Unit unit) {
		if (unit == null)
			throw new NullPointerException("unit cannot be null");
		return value * unit.conversionValue;
	}

	/**
	 * Get the value of the Speed in it's own {@link Unit}.
	 * 
	 * @return the value of speed
	 */
	public double getValue() {
		return getValue(unit);
	}

	/**
	 * Get the unit of this {@link Speed} object.
	 * 
	 * @return the {@link Unit}.
	 */
	public Unit getUnit() {
		return unit;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Speed other = (Speed) obj;

		return Double.doubleToLongBits(value) == Double
				.doubleToLongBits(other.value);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(5, 23).append(value).toHashCode();
	}

	@Override
	public String toString() {
		return unit.format.format(getValue()) + " " + unit.suffix;
	}

	@Override
	public int compareTo(final Speed other) {
		if (other == null) 
			throw new NullPointerException("cannot compare to null");
		if (equals(other))
			return 0;
		if (value > other.value) 
			return 1;
		else 
			return -1;
	}
}
