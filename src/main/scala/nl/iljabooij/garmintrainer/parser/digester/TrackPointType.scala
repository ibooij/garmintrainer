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
package nl.iljabooij.garmintrainer.parser.digester

import nl.iljabooij.garmintrainer.model.Length
import nl.iljabooij.garmintrainer.model.TrackPointImpl
import org.joda.time.DateTime
import scala.reflect.BeanProperty
/**
 * Builder for {@link TrackPointImpl}. Because {@link TrackPointImpl} is an unmodifiable
 * object, we need a builder if we want to build a {@link TrackPointImpl}
 * incrementally, as with commons-digester.
 * 
 * @author "Ilja Booij <ibooij@gmail.com>"
 * 
 */
class TrackPointType {
  @BeanProperty var time:DateTime = null
	
  @BeanProperty var heartRate: Int = 0
  @BeanProperty var altitude: Length = null
  @BeanProperty var distance: Length = null
  @BeanProperty var latitude: Double = 0.0
  @BeanProperty var longitude: Double = 0.0
}