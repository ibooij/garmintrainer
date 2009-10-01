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

/**
 * Storage for {@link Activity} objects.
 * @author "Ilja Booij <ibooij@gmail.com>"
 */
public interface ActivityStorage {
    /**
     * Get the activity for an id.
     * @param id the id of the activity
     * @return the {@link Activity} with the id, or null if not found.
     */
    Activity getActivity(File tcxFile);

    /**
     * Save the {@link Activity}.
     * @param activity the {@link Activity} to save.
     */
    void saveActivity(Activity activity);
    
    /**
     * Check if an activity is already in the storage. If the file is already
     * in the storage, it is checked wether or not the stored file is newer
     * or of the same age as activityFile. If it is, true is returned.
     * @param activityFile file for the activity
     * @return if the storage has an up to date version of the Activity File.
     */
    boolean hasActivity(File activityFile);
}
