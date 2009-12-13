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
package nl.iljabooij.garmintrainer.model

import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport

import org.joda.time.DateTime
import org.joda.time.Duration

import com.google.inject.Singleton

@Singleton
class ApplicationStateImpl extends ApplicationState with LoggerHelper {
  /**
   * Use {@link PropertyChangeSupport} for propagating changes in properties.
   */
  protected val propertyChangeSupport = new PropertyChangeSupport(this)
  private val lock: AnyRef = new Object
  
  private var currentActivityField: Option[Activity] = None
  private var errorMessageField = ""
  
  override def currentActivity = {
    lock.synchronized {
      currentActivityField
    }
  }
  
  override def errorMessage = {
    lock.synchronized {
      errorMessageField
    }
  }
  
  override def currentActivity_=(newCurrentActivity: Option[Activity]): Unit = {
    var oldActivity: Option[Activity] = None
    lock.synchronized {
      oldActivity = currentActivityField
      // take the short route out if nothing has changed!
      if (oldActivity != null && oldActivity.equals(newCurrentActivity)) {
        return
      }
      currentActivityField = newCurrentActivity
    }
  	propertyChangeSupport.firePropertyChange(Property.CurrentActivity.toString, oldActivity,
  			newCurrentActivity)
  }  

  def errorMessage_=(message: String): Unit = {
    var oldMessage:String = null
    lock.synchronized {
      oldMessage = errorMessageField
      errorMessageField = message
    }
    propertyChangeSupport.firePropertyChange(Property.ErrorMessage.toString, oldMessage,
                message)
  }
  
  def addPropertyChangeListener(property: Property.Property, listener: PropertyChangeListener) {
    propertyChangeSupport.addPropertyChangeListener(property.toString, listener)
  }
}
