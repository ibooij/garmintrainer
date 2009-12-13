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

import java.beans.PropertyChangeListener;

trait ApplicationState {
    
  def currentActivity: Option[Activity]
  def errorMessage: String
  
  def currentActivity_=(newCurrentActivity: Option[Activity]): Unit
  def errorMessage_=(message: String): Unit
	
  /**
   * Add a function as a listener for changed activities
   */
  def addActivityChangeListener(listener: Option[Activity] => Unit):Unit
  
  /**
   * Add a {@link PropertyChangeListener} for a specific property.
   * @param property property to listen for
   * @param propertyChangeListener the listener to add
   */
   def addPropertyChangeListener(property: Property.Property,
			listener: PropertyChangeListener): Unit
}