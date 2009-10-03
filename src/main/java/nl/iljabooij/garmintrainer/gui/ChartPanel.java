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
import java.awt.Graphics;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;

import nl.iljabooij.garmintrainer.model.Activity;
import nl.iljabooij.garmintrainer.model.ApplicationState;
import nl.iljabooij.garmintrainer.util.InjectLogger;

import com.google.inject.Inject;

public class ChartPanel extends JPanel {
	@InjectLogger
	private Logger logger;
	
	private static final long serialVersionUID = 1L;

	private transient final ApplicationState applicationState;

	@Inject
	public ChartPanel(final ApplicationState applicationState) {
		super(new BorderLayout());
		setName("Chart");

		this.applicationState = applicationState;
		applicationState.addPropertyChangeListener(
				ApplicationState.Property.CurrentActivity,
				new ActivityChangedListener());
	}
	
	private int getLeft() { 
		return getInsets().left;
	}
	
	private int getRight() {
		return getWidth() - getLeft() - getInsets().right;
	}
	
	
	@Override
	protected void paintComponent(final Graphics graphics) {
		if (logger.isDebugEnabled()) {
			logger.debug("painting chart {}x{}", new Object[] {getWidth(), getHeight()});
		}
		super.paintComponent(graphics);
		
		graphics.setColor(Color.white);
		
		Insets insets = getInsets();
		graphics.fillRect(getLeft(), insets.top, getRight(), 
				getHeight() - insets.top - insets.bottom);
		
	}
		


	/** 
	 * Listener that will react to changes of the currently active {@link Activity}.
	 * @author ilja
	 *
	 */
	private class ActivityChangedListener implements PropertyChangeListener,
			Runnable {
		/**
		 * Call redraw chart.
		 */
		@Override
		public void run() {
			repaint();
		}

		/**
		 * React to change of current Activity
		 * @param evt {@link PropertyChangeEvent} that indicates change of current {@link Activity}.
		 */
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			SwingUtilities.invokeLater(this);

		}
	}

}
