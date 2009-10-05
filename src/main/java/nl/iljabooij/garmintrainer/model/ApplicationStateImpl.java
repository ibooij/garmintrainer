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
import java.beans.PropertyChangeSupport;

import net.jcip.annotations.GuardedBy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

@Singleton
public class ApplicationStateImpl implements ApplicationState {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ApplicationStateImpl.class);

    /**
     * Use {@link PropertyChangeSupport} for propagating changes in properties.
     */
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
            this);

    @GuardedBy("this")
    private Activity currentActivity = null;

    @GuardedBy("this")
    private String errorMessage;

    /*
     * (non-Javadoc)
     * 
     * @see
     * nl.iljabooij.garmintrainer.model.ApplicationState#getCurrentActivity()
     */
    public Activity getCurrentActivity() {
        synchronized (this) {
            return currentActivity;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * nl.iljabooij.garmintrainer.model.ApplicationState#setCurrentActivity(
     * nl.iljabooij.garmintrainer.model.Activity)
     */
    public void setCurrentActivity(final Activity currentActivity) {
    	if (LOGGER.isTraceEnabled()) {
    		LOGGER.trace("Setting current activity to {}", currentActivity);
    	}
        Activity oldActivity;
        synchronized (this) {
            oldActivity = this.currentActivity;
            // take the short route out if nothing has changed!
            if (oldActivity != null && oldActivity.equals(currentActivity)) {
                return;
            }
            this.currentActivity = currentActivity;
        }

        propertyChangeSupport.firePropertyChange(Property.CurrentActivity.getName(), oldActivity,
                currentActivity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * nl.iljabooij.garmintrainer.model.ApplicationState#addPropertyChangeListener
     * (java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(
            final PropertyChangeListener propertyChangeListener) {
        propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    /*
     * (non-Javadoc)
     * 
     * @seenl.iljabooij.garmintrainer.model.ApplicationState#
     * removePropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void removePropertyChangeListener(
            final PropertyChangeListener propertyChangeListener) {
        propertyChangeSupport
                .removePropertyChangeListener(propertyChangeListener);
    }

    @Override
    public String getErrorMessage() {
        synchronized (this) {
            return errorMessage;
        }
    }

    @Override
    public void setErrorMessage(final String message) {
        String oldMessage;
        synchronized (this) {
            oldMessage = this.errorMessage;
            this.errorMessage = message;
        }
        propertyChangeSupport.firePropertyChange(Property.ErrorMessage.getName(), oldMessage,
                message);
    }

	@Override
	public void addPropertyChangeListener(Property property,
			PropertyChangeListener propertyChangeListener) {
		propertyChangeSupport.addPropertyChangeListener(property.getName(), 
				propertyChangeListener);
	}

}
