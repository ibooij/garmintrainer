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

import org.apache.commons.digester.AbstractObjectCreationFactory
import org.joda.time.format.ISODateTimeFormat
import org.xml.sax.Attributes

class LapBuilderFactory extends AbstractObjectCreationFactory {
  private val dateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis()
  
  override def createObject(attributes: Attributes): LapType = {
    val dateTimeString = attributes.getValue(LapBuilderFactory.startTimeAttribute)
	val startTime = dateTimeFormatter.parseDateTime(dateTimeString)
	val lapBuilder = new LapType
	lapBuilder.startTime = startTime
	
	return lapBuilder
  }
}

object LapBuilderFactory {
  val startTimeAttribute = "StartTime"
}
