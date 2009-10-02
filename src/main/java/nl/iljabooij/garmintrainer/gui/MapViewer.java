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

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
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
	 * Paint the track of the {@link Activity} on the map. The map itself is 
	 * painted in {@link JMapViewer}, the superclass of this class.
	 * @param g the {@link Graphics} object used for drawing.
	 * @see JMapViewer#paintComponents(Graphics)
	 */
	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		
		// if there is no current Activity, there is no sense in drawing 
		// anything.
		if (applicationState.getCurrentActivity() == null) {
			return;
		}
		
		final Path2D path = new Path2D.Double();
		for (TrackPoint trackPoint: applicationState.getCurrentActivity().getTrackPoints()) {
			if (trackPoint.hasPosition()) {
				Point p = getMapPosition(trackPoint.getLatitude(), trackPoint.getLongitude(), false);
				if (path.getCurrentPoint() == null) {
					path.moveTo(p.x, p.y);
				} else {
					path.lineTo(p.x, p.y);
				}
			}
		}
		
		final Graphics2D g2d = (Graphics2D) g;
		Composite originalComposite = g2d.getComposite();
	
		// use Alpha factor for drawing.
		int type = AlphaComposite.SRC_OVER;
	    g2d.setComposite(AlphaComposite.getInstance(type, 0.5f));
	    
		g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		
		g2d.setColor(Color.RED);
		g2d.draw(path);
		
		// return to original composite
		g2d.setComposite(originalComposite);
	}
	
	/**
	 * Update the map using a new list of markers.
	 * @param markers markers to use in the map.
	 */ 
	private void updateMap(final ArrayList<MapMarker> markers) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// we don't want to show the markers, just use them to set the
				// region of the map that we want to show.
				setMapMarkerVisible(false);
				setMapMarkerList(markers);
				setDisplayToFitMapMarkers();
				repaint();
			}
		});
	}
}
