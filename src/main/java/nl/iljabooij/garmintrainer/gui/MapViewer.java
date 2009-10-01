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
package nl.iljabooij.garmintrainer.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import nl.iljabooij.garmintrainer.model.Activity;
import nl.iljabooij.garmintrainer.model.ApplicationState;
import nl.iljabooij.garmintrainer.model.TrackPoint;
import nl.iljabooij.garmintrainer.model.ApplicationState.Property;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.OsmFileCacheTileLoader;
import org.openstreetmap.gui.jmapviewer.OsmTileSource;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * Subclass of {@link JMapViewer} that shows the route of an {@link Activity} as
 * a collection of {@link SmallMapMarker} objects.
 * @author ilja
 */
public class MapViewer extends JMapViewer implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	private transient final ApplicationState applicationState;
	
	private static final File CACHE_DIR = new File(new File(System.getProperty("user.home"), ".garminTrainer")
		, "tileCache");

	/**
	 * Create a new {@link MapViewer}. Normally, this constructor is called
	 * from Guice.
	 * @param applicationState the global application state.
	 */
	@Inject
	public MapViewer(final ApplicationState applicationState) {
		super();
		setName("Map");

		setTileSource(new OsmTileSource.Mapnik());
		this.applicationState = applicationState;
		this.applicationState.addPropertyChangeListener(
				Property.CurrentActivity, this);
		
		setTileLoader(new OsmFileCacheTileLoader(this, CACHE_DIR));
	}

	/**
	 * Called when the the current activity is changed.
	 * @param evt the {@link PropertyChangeEvent}.
	 */
	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		changeMarkers();
	}
	
	/**
	 * Change the map markers using the track points in the current activity.
	 */
	private void changeMarkers() {
		final ArrayList<MapMarker> markers = Lists.newArrayList();
		for (TrackPoint trackPoint : applicationState.getCurrentActivity()
				.getTrackPoints()) {
			if (trackPoint.hasPosition())
				markers.add(new SmallMapMarker(trackPoint));
		}

		updateMap(markers);
	}

	/**
	 * Update the map using a new list of markers.
	 * @param markers markers to use in the map.
	 */
	private void updateMap(final ArrayList<MapMarker> markers) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setMapMarkerVisible(false);
				setMapMarkerList(markers);
				setMapMarkerVisible(true);
				setDisplayToFitMapMarkers();
			}
		});
	}
}
