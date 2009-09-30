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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ExecutionException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import nl.iljabooij.garmintrainer.model.ApplicationState;
import nl.iljabooij.garmintrainer.model.TrackPoint;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

import com.google.inject.Inject;

public class ChartComponent extends JPanel {
	private static final long serialVersionUID = 1L;

	private transient final ApplicationState applicationState;

	private final ChartPanel chartPanel;

	@Inject
	public ChartComponent(final ApplicationState applicationState) {
		super(new BorderLayout());
		setName("Chart");

		this.applicationState = applicationState;
		applicationState.addPropertyChangeListener(
				ApplicationState.Property.CurrentActivity,
				new ActivityChangedListener());

		chartPanel = createChartPanel();
		chartPanel.setPreferredSize(new Dimension(400, 400));
		add(chartPanel, BorderLayout.CENTER);
	}
		
	private ChartPanel createChartPanel() {
		final TimeSeriesCollection dataset = createDataset();
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);

		chartPanel.setPreferredSize(new Dimension(500, 270));

		return chartPanel;
	}

	/**
	 * Redraw the chart. The Chart is created in the background. It updates the
	 * {@link ChartPanel} on the EDT.
	 */
	private void redrawChart() {
		new SwingWorker<JFreeChart, Void>() {
			@Override
			protected JFreeChart doInBackground() {
				return createChart(createDataset());
			}

			@Override
			protected void done() {
				try {
					chartPanel.setChart(get());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.execute();
	}

	private JFreeChart createChart(final TimeSeriesCollection dataset) {
		// create the chart...

		final JFreeChart chart = ChartFactory.createTimeSeriesChart("Altitude", // chart
				// title
				"Time", // domain axis label
				"Altitude", // range axis label
				dataset, // data
				true, // include legend
				true, // tooltips
				false // urls
				);

		chart.setBackgroundPaint(Color.white);

		final XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);

		return chart;
	}

	private Second extractSecondFromTrackPoint(final TrackPoint trackPoint) {
		return new Second(trackPoint.getTime().toDate());
	}

	private TimeSeriesDataItem createAltitudeSample(final TrackPoint trackPoint) {
		return new TimeSeriesDataItem(extractSecondFromTrackPoint(trackPoint),
				trackPoint.getAltitude().getValueInMeters());
	}

	private TimeSeriesDataItem createHeartRateSample(final TrackPoint trackPoint) {
		return new TimeSeriesDataItem(extractSecondFromTrackPoint(trackPoint),
				trackPoint.getHeartRate());
	}

	//
	private TimeSeriesCollection createDataset() {
		final TimeSeriesCollection dataset = new TimeSeriesCollection();

		final TimeSeries altitudePerTime = new TimeSeries("Altitude",
				Second.class);

		if (applicationState.getCurrentActivity() != null) {
			for (TrackPoint trackPoint : applicationState.getCurrentActivity()
					.getTrackPoints()) {
				altitudePerTime.add(createAltitudeSample(trackPoint));
			}
		}
		dataset.addSeries(altitudePerTime);

		final TimeSeries heartRatePerTime = new TimeSeries("Heart Rate",
				Second.class);

		if (applicationState.getCurrentActivity() != null) {
			for (TrackPoint trackPoint : applicationState.getCurrentActivity()
					.getTrackPoints()) {
				heartRatePerTime.add(createHeartRateSample(trackPoint));
			}
		}
		dataset.addSeries(heartRatePerTime);

		return dataset;
	}

	private class ActivityChangedListener implements PropertyChangeListener,
			Runnable {
		@Override
		public void run() {
			redrawChart();
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			SwingUtilities.invokeLater(this);

		}
	}

}
