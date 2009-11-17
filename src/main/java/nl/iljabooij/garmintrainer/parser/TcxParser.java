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
package nl.iljabooij.garmintrainer.parser;

import java.io.InputStream;
import java.util.List;

import nl.iljabooij.garmintrainer.model.Activity;

/**
 * A {@link TcxParser} object will parse a TCX file from an {@link InputStream} to
 * a list of {@link Activity} objects.
 * @author ilja
 */
public interface TcxParser {

	/**
	 * Parse the {@link InputStream} and return the resulting list of Activities.
	 * @param inputStream the input stream to take input from. Note that the TcxParser
	 * does not have the responsibility of closing the input stream.
	 * @return a list of Activities.
	 * @throws ParseException if an error occured during parsing.
	 */
	List<Activity> parse(final InputStream inputStream) throws ParseException;
}