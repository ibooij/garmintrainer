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

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import nl.iljabooij.garmintrainer.model.Activity;
import nl.iljabooij.garmintrainer.model.ApplicationState;
import nl.iljabooij.garmintrainer.model.Length;
import nl.iljabooij.garmintrainer.model.Length.Unit;

import org.joda.time.Duration;
import org.joda.time.Period;

import com.google.inject.Inject;

public class SummaryPanel extends JPanel implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;

	private final String[] labelNames = { "Date", "Distance", "Total Time",
			"Riding Time", "Nr of Laps", "Meters Climbed" };
	private final JLabel[] labels = new JLabel[labelNames.length];
	private final JLabel dateValueLabel = new JLabel("........");
	private final JLabel distanceValueLabel = new JLabel("........");
	private final JLabel totalDurationLabel = new JLabel("........");
	private final JLabel netDurationLabel = new JLabel("........");
	private final JLabel nrOfLapsValueLabel = new JLabel("........");
	private final JLabel metersClimbedLabel = new JLabel("........");

	@Inject
	SummaryPanel(final ApplicationState applicationState) {
		super(new SpringLayout());
		setName("Summary");
		setPreferredSize(new Dimension(800, 600));
		applicationState.addPropertyChangeListener(
				ApplicationState.Property.CurrentActivity, this);

		for (int i = 0; i < labels.length; i++) {
			labels[i] = new JLabel(labelNames[i]);
		}
		labels[0].setLabelFor(dateValueLabel);
		labels[1].setLabelFor(distanceValueLabel);
		labels[2].setLabelFor(totalDurationLabel);
		labels[3].setLabelFor(netDurationLabel);
		labels[4].setLabelFor(nrOfLapsValueLabel);
		labels[5].setLabelFor(metersClimbedLabel);

		add(labels[0]);
		add(dateValueLabel);
		add(labels[1]);
		add(distanceValueLabel);
		add(labels[2]);
		add(totalDurationLabel);
		add(labels[3]);
		add(netDurationLabel);
		add(labels[4]);
		add(nrOfLapsValueLabel);
		add(labels[5]);
		add(metersClimbedLabel);

		SpringUtilities.makeGrid(this, labels.length, 2, 5, 5, 5, 5);
	}

	/**
	 * Construct a string holding a duration
	 * 
	 * @param duration
	 *            the duration
	 * @return a string with the duration.
	 */
	private String constructDurationString(final Duration duration) {
		final Period period = duration.toPeriod();

		return String.format("%d:%02d:%02d", period.getHours(), period
				.getMinutes(), period.getSeconds());
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		final Activity currentActivity = (Activity) evt.getNewValue();
		if (currentActivity == null) {
			return;
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				dateValueLabel.setText(currentActivity.getStartTime().toString(
						"yyyy-MM-dd"));
				Length distance = currentActivity.getDistance().convert(
						Length.Unit.Kilometer);
				distanceValueLabel.setText(distance.toString());
				Integer nrOfLaps = currentActivity.getLaps().size();
				nrOfLapsValueLabel.setText(nrOfLaps.toString());
				totalDurationLabel
						.setText(constructDurationString(currentActivity
								.getGrossDuration()));
				netDurationLabel
						.setText(constructDurationString(currentActivity
								.getNetDuration()));
				metersClimbedLabel.setText(currentActivity.getAltitudeGain().convert(Unit.Meter).toString());
			}
		});
	}
}
