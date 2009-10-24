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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;

import javax.swing.SwingUtilities;

import nl.iljabooij.garmintrainer.model.Activity;
import nl.iljabooij.garmintrainer.model.ApplicationState;
import nl.iljabooij.garmintrainer.model.TrackPoint;
import nl.iljabooij.garmintrainer.model.ApplicationState.Property;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.OsmFileCacheTileLoader;
import org.openstreetmap.gui.jmapviewer.OsmTileSource;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;

/**
 * Subclass of {@link JMapViewer} that shows the route of an {@link Activity} as
 * a collection of {@link SmallMapMarker} objects.
 * 
 * @author ilja
 */
public class MapViewer extends JMapViewer {
	private static final long serialVersionUID = 1L;
	private transient final ApplicationState applicationState;

	private static final File CACHE_DIR = new File(new File(System
			.getProperty("user.home"), ".garminTrainer"), "tileCache");

	/**
	 * {@link PropertyChangeListener} for activity changes.
	 */
	private PropertyChangeListener activityChangeListener = new PropertyChangeListener() {
		@Override
		public void propertyChange(final PropertyChangeEvent evt) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					zoomMapToActivity(evt.getNewValue());
					repaint();
				}
			});
		}
	};

	/**
	 * Create a new {@link MapViewer}. Normally, this constructor is called from
	 * Guice.
	 * 
	 * @param applicationState
	 *            the global application state.
	 */
	@Inject
	public MapViewer(final ApplicationState applicationState) {
		super();
		setName("Map");

		setTileSource(new OsmTileSource.Mapnik());
		this.applicationState = applicationState;
		this.applicationState.addPropertyChangeListener(
				Property.CurrentActivity, activityChangeListener);
		setTileLoader(new OsmFileCacheTileLoader(this, CACHE_DIR));
	}

	/**
	 * Paint the track of the {@link Activity} on the map. The map itself is
	 * painted in {@link JMapViewer}, the superclass of this class.
	 * 
	 * @param g
	 *            the {@link Graphics} object used for drawing.
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
		for (TrackPoint trackPoint : applicationState.getCurrentActivity()
				.getTrackPoints()) {
			if (trackPoint.hasPosition()) {
				Point p = getMapPosition(trackPoint.getLatitude(), trackPoint
						.getLongitude(), false);
				if (path.getCurrentPoint() == null) {
					path.moveTo(p.x, p.y);
				} else {
					path.lineTo(p.x, p.y);
				}
			}
		}
		final Graphics2D g2d = (Graphics2D) g;
		g.setColor(new Color(1.0f, 0, 0, 0.5f));
		g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND));
		g2d.draw(path);
	}

	private void zoomMapToActivity(final Object activityObject) {
		assert(SwingUtilities.isEventDispatchThread());
		
		Coordinate northWest = new Coordinate(-180.0, 90.0);
		Coordinate southEast = new Coordinate(180.0, -90.0);
		if (activityObject != null) {
			Activity activity = (Activity) activityObject;
			Predicate<TrackPoint> trackPointHasPosition = new Predicate<TrackPoint>() {
				@Override
				public boolean apply(TrackPoint input) {
					return input.hasPosition();
				}
				
			};
			Function<TrackPoint, Coordinate> trackPointToCoordinate = new Function<TrackPoint, Coordinate>() {
				@Override
				public Coordinate apply(TrackPoint from) {
					return new Coordinate(from.getLatitude(), from
							.getLongitude());
				}
			};
			List<Coordinate> coordinates = ImmutableList.copyOf(Iterables
					.transform(Iterables.filter(activity.getTrackPoints(),
							trackPointHasPosition), trackPointToCoordinate));
			Coordinate[] bounds = Coordinate.getBoundingBox(coordinates);
			northWest = bounds[0];
			southEast = bounds[1];
		}
		zoomToBoundingBox(northWest, southEast);
	}
}
