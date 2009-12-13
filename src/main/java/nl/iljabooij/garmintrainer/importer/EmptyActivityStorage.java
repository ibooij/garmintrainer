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
package nl.iljabooij.garmintrainer.importer;

import java.io.File;

import nl.iljabooij.garmintrainer.model.Activity;

public class EmptyActivityStorage implements ActivityStorage {

	/**
	 * Always returns null.
	 * @param tcxFile ignored
	 * @return null, always
	 */
	@Override
	public Activity getActivity(File tcxFile) {
		return null;
	}

	/**
	 * Always return false.
	 * @param activityFile ignored.
	 * @return false, always.
	 */
	@Override
	public boolean hasActivity(File activityFile) {
		return false;
	}

	/**
	 * Does nothing.
	 * @param activity to do nothing with.
	 */
	@Override
	public void saveActivity(Activity activity) {
		// do nothing.
	}

}
