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

import com.google.inject.Singleton

@Singleton
class ApplicationStateImpl extends ApplicationState with LoggerHelper {
  /**
   * Use {@link PropertyChangeSupport} for propagating changes in properties.
   */
  private val lock: AnyRef = new Object
  
  private var currentActivityField: Option[Activity] = None
  private var errorMessageField:Option[String] = None
  private val activityListenersLock = new Object 
  private var activityListeners = List[Option[Activity] => Unit]()
  private val errorListenersLock = new Object
  private var errorListeners = List[Option[String] => Unit]()
  
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
    val listeners = activityListenersLock.synchronized {activityListeners}
    listeners.foreach(_(newCurrentActivity))
  }  

  def errorMessage_=(message: Option[String]): Unit = {
    var oldMessage:Option[String] = None
    lock.synchronized {
      oldMessage = errorMessageField
      errorMessageField = message
    }
    val listeners = errorListenersLock.synchronized {errorListeners}
    listeners.foreach(_(message))
  }
  
  //def addPropertyChangeListener(property: Property.Property, listener: PropertyChangeListener) {
  //  propertyChangeSupport.addPropertyChangeListener(property.toString, listener)
  //}
  
  override def addActivityChangeListener(listener: Option[Activity] => Unit):Unit = {
    activityListenersLock.synchronized {
      activityListeners ::= listener
    }
  }
  
  override def addErrorChangeListener(listener: Option[String] => Unit) {
    errorListenersLock.synchronized {
      errorListeners ::= listener
    } 
  }
}
