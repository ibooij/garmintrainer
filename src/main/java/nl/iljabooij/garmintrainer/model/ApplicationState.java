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

import java.beans.PropertyChangeListener;

public interface ApplicationState {
	/**
	 * Enum holding all properties in {@link ApplicationState} that can be
	 * listened to. See {@link ApplicationState#addPropertyChangeListener(Property, PropertyChangeListener)}.
	 * @author ilja
	 */
	public enum Property {
		CurrentActivity("currentActivity"),
		ErrorMessage("errorMessage");
		
		private String propertyName;
		
		private Property(final String propertyName) {
			this.propertyName = propertyName;
		}
		
		public String getName() {
			return propertyName;
		}
		
		/**
		 * Get the {@link Property} for a certain propertyName.
		 * @param propertyName the property name to look for
		 * @return the property or null if no such property.
		 */
		public static Property valueFor(final String propertyName) {
			for (Property property: values()) {
				if (property.propertyName.equals(propertyName)) {
					return property;
				}
			}
			return null;
		}
	}
	
    /**
     * Get the {@link Activity} that should is currently active.
     * @return the {@link Activity} that is currently active
     */
	Activity getCurrentActivity();

	/**
	 * set the {@link Activity} that is currently active
	 * @param currentActivity the activity that should be active
	 */
	void setCurrentActivity(Activity currentActivity);
		
	String getErrorMessage();
	
	void setErrorMessage(final String message);
	
	/**
	 * Add a {@link PropertyChangeListener} for a specific property.
	 * @param property property to listen for
	 * @param propertyChangeListener the listener to add
	 */
	void addPropertyChangeListener(Property property,
			PropertyChangeListener propertyChangeListener);
}