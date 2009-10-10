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
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import nl.iljabooij.garmintrainer.model.Activity;

import com.google.inject.Inject;

/**
 * Panel that shows an overview of all information in the {@link Activity} file.
 * @author ilja
 *
 */
public class OverviewPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor, normally called by Guice.
	 * @param summaryPanel panel holding the summary
	 * @param mapViewer panel holding the map
	 * @param chartComponent panel holding the chart.
	 */
	@Inject
	public OverviewPanel(final SummaryPanel summaryPanel,
			final MapPanel mapPanel,
			final ChartPanel chartComponent) {
			
		super();
		setLayout(new GridLayout(2, 2, 5, 5));
		setName("Overview");

		summaryPanel.setBorder(new TitledBorder("Summary"));
		chartComponent.setBorder(new TitledBorder("Chart"));
		add(summaryPanel);
		add(mapPanel);
		add(new JPanel()); // empty block
		add(chartComponent);
	}
}
