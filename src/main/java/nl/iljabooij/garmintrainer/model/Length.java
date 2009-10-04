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

/**
 * Implements a measurement of Length. A Length object is immutable.
 * @author ilja booij <ibooij@gmail.com>
 */
@Immutable
public final class Length implements Comparable<Length>, Serializable {
    private static final long serialVersionUID = 1L;
    
	public enum Unit {
		Meter(1f, "m", 1),
		Kilometer(1000f, "km", 3),
		Foot(0.3048f, "ft", 1),
		Mile(1609.344f, "mi", 3);
		
		public final float conversionValue;
		public final String suffix;
		public final NumberFormat format;
		
		private Unit(final float conversionValue, final String suffix, final int scale) {
			this.conversionValue = conversionValue;
			this.suffix = suffix;
			
			final StringBuffer sb = new StringBuffer("#0.");
			for(int i = 0; i < scale; i++) {
				sb.append('0');
			}
			format = new DecimalFormat(sb.toString());
		}
	}
	
	// holds value in meters (the SI unit)
	private final double value;
	private final Unit unit;
	
	private Length(final double value, final Unit unit) {
		if (unit == null) {
			throw new IllegalArgumentException("unit should be non-null");
		}
	
		this.value = value * unit.conversionValue;
		this.unit = unit;
	}
	
	/**
	 * Create a Length, with as its value given in meters
	 * @param value the length in meters
	 * @return the Length object
	 * @throws NullPointerException if value is null
	 */
	public static Length createLengthInMeters(final double value) {
		return new Length(value, Unit.Meter);
	}
	
	/**
	 * Create a Length, with as its value given in the supplied unit.
	 * @param value the length in the unit
	 * @param unit the {@link Unit} of the length
	 * @return the Length object
	 * @throws IllegalArgumentException if value or unit are null.
	 */
	public static Length createLength(final double value, final Unit unit) {
		return new Length(value, unit);
	}
	
	/**
	 * Get the value of this Length in meters.
	 * @return value in meters.
	 */
	public double getValueInMeters() {
		return getValue(Unit.Meter);
	}
	
	/**
	 * Get the value of this {@link Length} in it's {@link Unit}.
	 * @return
	 */
	public double getValue() {
		return getValue(unit);
	}
	
	/**
	 * Get the value of this Length in the supplied {@link Unit}.
	 * @param unit the {@link Unit} to give the Length in.
	 * @return the value in the supplied {@link Unit}.
	 * @throws IllegalArgumentException if unit is null.
	 */
	public double getValue(final Unit unit) {
		if (unit == null) {
			throw new IllegalArgumentException("unit should not be null");
		}
		return value / unit.conversionValue;
	}


	/**
	 * Get the unit of this Length
	 * @return the unit of the length
	 */
	public Unit getUnit() {
		return unit;
	}
	
	/**
	 * Convert this Length to another Unit. 
	 * @param unit the Unit to convert to.
	 * @return the new converted length.
	 * @throws IllegalArgumentException if unit is null.
	 */
	public Length convert(final Unit unit) {
		if (unit == null) 
			throw new IllegalArgumentException("unit cannot be null");
		
		return createLength(value / unit.conversionValue, unit);
	}
	
	/**
	 * Return a new length that is the sum of this Length and and the Length
	 * added. The new Length has the same Unit as this Length.
	 * @param other Length to add.
	 * @return the sum of the Lengths
	 * @throws IllegalArgumentException if other is null. 
	 */
	public Length add(final Length other) {
		if (other == null) {
			throw new IllegalArgumentException("subtractor should not be null");
		}
		return createLengthInMeters(this.value + other.value).convert(this.unit);
	}
	
	/**
	 * return a new Length with the value of this subtracted by subtractor
	 * @param subtractor the Length to subtract from this {@link Length}.
	 * @return a new {@link Length} which is the result of the subtraction.
	 */
	public Length substract(final Length subtractor) {
		if (subtractor == null) {
			throw new IllegalArgumentException("subtractor should not be null");
		}
		return createLengthInMeters(this.value - subtractor.value).convert(this.unit);
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Length other = (Length) obj;
		
		return Double.doubleToLongBits(value) == Double.doubleToLongBits(other.value);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(7, 17)
			.append(value)
			.toHashCode();
	}
	
	@Override
	public String toString() {
		return unit.format.format(getValue()) + " " + unit.suffix;
	}

	@Override
	public int compareTo(final Length other) {
		if (other == null) {
			throw new IllegalArgumentException("cannot compare to null");
		}
		
		if (equals(other)) {
			return 0;
		}
		if (value > other.value) {
			return 1;
		} else {
			return -1;
		}
	}
}
