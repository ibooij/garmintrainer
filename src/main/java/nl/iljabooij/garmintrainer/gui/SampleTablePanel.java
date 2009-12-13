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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import nl.iljabooij.garmintrainer.model.Activity;
import nl.iljabooij.garmintrainer.model.ApplicationState;
import nl.iljabooij.garmintrainer.model.ApplicationState.Property;

import com.google.inject.Inject;

public class SampleTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final String NAME = "Samples";
	private final SamplesTableModel model;

	@Inject
	SampleTablePanel(final ApplicationState applicationState,
			final JTable table, final SamplesTableModel model) {
		super(new BorderLayout());
		setName(NAME);
		this.model = model;
		table.setModel(model);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		table.setFillsViewportHeight(true);

		add(scrollPane, BorderLayout.CENTER);

		applicationState.addPropertyChangeListener(Property.CurrentActivity,
				new TableUpdater());
	}

	private class TableUpdater implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getNewValue() != null) {
				final Activity activity = (Activity) evt.getNewValue();
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						model.setActivity(activity);
					}
				});
			}
		}
	}

}
