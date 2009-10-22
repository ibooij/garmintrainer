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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import nl.iljabooij.garmintrainer.model.Activity;
import nl.iljabooij.garmintrainer.util.InjectLogger;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;

public class ActivityStorageImpl implements ActivityStorage {
    @InjectLogger private Logger logger;
    
    private static final String MAIN_DIR_NAME = ".garminTrainer";
    private static final String CACHE_DIR_NAME = "cache";
    
    private final File mainDir;
    private final File cacheDir;
    
    public ActivityStorageImpl() {
        mainDir = new File(System.getProperty("user.home"), MAIN_DIR_NAME);
        cacheDir = new File(mainDir, CACHE_DIR_NAME);
    }

	/**
	 * Create a directory.
	 * @param directory directory to create.
	 */
	private void createDirectory(final File directory) {
		boolean directoryCreated = directory.mkdir();
        if (!directoryCreated) {
        	logger.info("main directory [{}] not created. Probably already exists", directory);
        }
	}
    
    @Override
    public Activity getActivity(final File tcxFile) {
        File cacheFile = getCacheFileForTcxFile(tcxFile);
        Activity activity = null;
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(new FileInputStream(cacheFile));
            
            try {
                activity = (Activity) in.readObject();
            } catch (IOException e ){
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                in.close();
            }
        } catch (FileNotFoundException e) {
        	throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        return activity;
    }

    private File createDirectoryForActivity(final Activity activity) {
        int year = activity.getStartTime().getYear();
        final String directoryName = Integer.valueOf(year).toString();
        
        final File directory = new File(cacheDir, directoryName);
        createDirectory(directory);
        
        return directory;
    }
    
    private String createFileName(final Activity activity) {
    	final DateTime startTimeInZulu = activity.getStartTime().withZone(DateTimeZone.UTC);
        return  startTimeInZulu.toString("yyyy-MM-dd-HH-mm-ss") + ".obj";
    }
    
    @Override
    public void saveActivity(final Activity activity) {
    	createDirectory(mainDir);
    	createDirectory(cacheDir);
    	
        final String filename = createFileName(activity);
        
        final File directory = createDirectoryForActivity(activity);
        final File activityFile = new File(directory, filename);
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(activityFile));
            try {
                out.writeObject(activity);
            } finally {
                out.close();
            }
        } catch (FileNotFoundException e) {
        	throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } 
    }

    private File getCacheFileForTcxFile(final File tcxFile) {
        String filename = tcxFile.getName();
        // drop the extension
        filename = filename.substring(0, filename.length() - 4) + ".obj";
        final String yearString = filename.substring(0, 4);
        final File directory = new File(cacheDir, yearString);
        return new File(directory, filename);
    }
    
    @Override
    public boolean hasActivity(File activityFile) {
        final File cacheFile = getCacheFileForTcxFile(activityFile);
        return (cacheFile.exists() && cacheFile.isFile() && FileUtils.isFileNewer(cacheFile, activityFile));
    }
}
