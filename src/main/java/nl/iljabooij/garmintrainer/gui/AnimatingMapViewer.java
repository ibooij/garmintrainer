package nl.iljabooij.garmintrainer.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.SwingUtilities;

import nl.iljabooij.garmintrainer.model.Activity;
import nl.iljabooij.garmintrainer.model.ApplicationState;
import nl.iljabooij.garmintrainer.model.TrackPoint;
import nl.iljabooij.garmintrainer.model.ApplicationState.Property;

import org.joda.time.DateTime;

import com.google.inject.Inject;

public class AnimatingMapViewer extends MapViewer {
	private static final long serialVersionUID = 1L;
	private ApplicationState applicationState;

	@Inject
	AnimatingMapViewer(final ApplicationState applicationState) {
		super(applicationState);
		this.applicationState = applicationState;
		
		applicationState.addPropertyChangeListener(Property.CurrentPlayingTime, 
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								final TrackPoint currentTrackPoint = 
									calculateCurrentTrackPoint();
								if (currentTrackPoint != null) {
									setDisplayPositionByLatLon(
											currentTrackPoint.getLatitude(),
											currentTrackPoint.getLongitude(),
											getZoom());
								}
								
							}
						});
						repaint();
					}
				});
	}

	private TrackPoint calculateCurrentTrackPoint() {
		final Activity currentActivity = applicationState.getCurrentActivity();
		if (currentActivity != null) {

			final DateTime currentDateTime = applicationState
					.getCurrentPlayingTime();
			return currentActivity.getTrackPointForTime(currentDateTime);
		} else {
			return null;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		final TrackPoint currentTrackPoint = calculateCurrentTrackPoint();
		if (currentTrackPoint != null) {
			final Point p = getMapPosition(currentTrackPoint.getLatitude(),
					currentTrackPoint.getLongitude(), false);
			g.setColor(Color.BLUE);
			g.drawOval(p.x - 5, p.y - 5, 10, 10);
		}
	}
}
