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
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import nl.iljabooij.garmintrainer.model.Activity;
import nl.iljabooij.garmintrainer.model.ApplicationState;
import nl.iljabooij.garmintrainer.model.TrackPoint;
import nl.iljabooij.garmintrainer.model.ApplicationState.Property;
import nl.iljabooij.garmintrainer.util.InjectLogger;

import org.joda.time.Duration;
import org.slf4j.Logger;

import com.google.inject.Inject;

public class ChartComponent extends JComponent implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;

	@InjectLogger
	private Logger logger;
	
	private Activity currentActivity = null;
	
	@Inject
	ChartComponent(final ApplicationState applicationState) {
		applicationState.addPropertyChangeListener(Property.CurrentActivity, this);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getNewValue() == null) {
			currentActivity = null;
		} else {
			currentActivity = (Activity) evt.getNewValue();
		}
		repaint();
	}
	
	@Override
	protected void paintComponent(final Graphics graphics) {
		if(logger.isDebugEnabled()) {
			logger.debug("painting Chart component {} x {}", getWidth(), getHeight());
		}
		Graphics2D g2d = (Graphics2D) graphics.create();
		
		g2d.setBackground(Color.white);
		g2d.clearRect(0, 0, getWidth(), getHeight());
		
		if (currentActivity != null) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2d.translate(0, getHeight());
			g2d.scale(1.0, -1.0);
			drawAltitudeLine(g2d);
			drawSpeed(g2d);
		}
		g2d.dispose();
	}

	private void drawAltitudeLine(Graphics2D g2d) {
		assert (currentActivity != null);
		
		final double minAltitude = currentActivity.getMinimumAltitude().getValueInMeters();
		final double maxAltitude = currentActivity.getMaximumAltitude().getValueInMeters();
		
		final double altitudeOnScreen = maxAltitude - minAltitude;
		
		int durationInSeconds = currentActivity.getGrossDuration().toStandardSeconds().getSeconds();
		
		Path2D path = new Path2D.Double();
		for (TrackPoint trackPoint: currentActivity.getTrackPoints()) {
			// calculate x-value
			Duration fromStart = new Duration(currentActivity.getStartTime(), trackPoint.getTime());
			int fromStartInSeconds = fromStart.toStandardSeconds().getSeconds();
			double x = getWidth() * ((double) fromStartInSeconds / (double) durationInSeconds);
			double relativeAltitude = trackPoint.getAltitude().getValueInMeters() - minAltitude;
			double y = getHeight() * (relativeAltitude / altitudeOnScreen);
			
			if (path.getCurrentPoint() == null) {
				path.moveTo(x, y);
			} else {
				path.lineTo(x, y);
			}
		}
		g2d.setColor(Color.black);
		g2d.draw(path);
	}
	
	private void drawSpeed(final Graphics2D g2d) {
		assert(currentActivity != null);
		
		final double minSpeed = 0.0;
		final double maxSpeed = 30.0;
		
		final double speedRange = maxSpeed - minSpeed;
		
		int durationInSeconds = currentActivity.getGrossDuration().toStandardSeconds().getSeconds();
		
		Path2D path = new Path2D.Double();
		for (TrackPoint trackPoint: currentActivity.getTrackPoints()) {
			// calculate x-value
			Duration fromStart = new Duration(currentActivity.getStartTime(), trackPoint.getTime());
			int fromStartInSeconds = fromStart.toStandardSeconds().getSeconds();
			double x = getWidth() * ((double) fromStartInSeconds / (double) durationInSeconds);
			double relativeSpeed = trackPoint.getSpeed().getValueInMetersPerSecond() - minSpeed;
			double y = getHeight() * (relativeSpeed / speedRange);
			
			if (path.getCurrentPoint() == null) {
				path.moveTo(x, y);
			} else {
				path.lineTo(x, y);
			}
		}
		g2d.setStroke(new BasicStroke(1.0f));
		g2d.setColor(Color.red);
		g2d.draw(path);
	}
}
