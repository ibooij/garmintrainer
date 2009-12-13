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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import nl.iljabooij.garmintrainer.model.Activity;
import nl.iljabooij.garmintrainer.model.ApplicationState;
import nl.iljabooij.garmintrainer.model.ApplicationState.Property;
import nl.iljabooij.garmintrainer.parser.digester.ParseException;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * @author "Ilja Booij <ibooij@gmail.com>"
 * 
 */
public class MainGui implements PropertyChangeListener {
	/**
	 * serial version, start at 1L
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String TITLE_PREFIX = "Garmin Trainer";

	@Inject
	private OverviewPanel overviewPanel;
	@Inject
	private MapViewer mapViewer;
	@Inject
	private ChartPanel chartPanel;
	@Inject
	private SampleTablePanel sampleTablePanel;
	@Inject
	private ApplicationState applicationState;
	@Inject 
	private FileTransferHandler fileTransferHandler;
	
	private JFrame mainFrame;

	public void init(final JFrame jFrame) {
		applicationState.addPropertyChangeListener(Property.CurrentActivity, this);
		applicationState.addPropertyChangeListener(Property.ErrorMessage, this);

		mainFrame = jFrame;
		final Container pane = jFrame.getContentPane();

		pane.setLayout(new BorderLayout());
		
		JTabbedPane tabs = new JTabbedPane();
		tabs.add(overviewPanel);
		tabs.add(mapViewer);
		tabs.setMnemonicAt(0, KeyEvent.VK_M);
		tabs.add(chartPanel);
		tabs.setMnemonicAt(1, KeyEvent.VK_C);
		tabs.add(sampleTablePanel);
		tabs.setMnemonicAt(2, KeyEvent.VK_S);
		
		pane.add(tabs, BorderLayout.CENTER);
		
		mainFrame.setTransferHandler(fileTransferHandler);
	}

	private static void createAndShowGui() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedLookAndFeelException e) {
			throw new RuntimeException(e);
		}
		
		final Module module = new MainModule();
		final Injector injector = Guice.createInjector(module);

		// Create and set up the window.
		final JFrame frame = new JFrame(TITLE_PREFIX);
		frame.setLocationByPlatform(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final MainGui mainGui = injector.getInstance(MainGui.class);
		mainGui.init(frame);
		frame.setSize(new Dimension(800, 600));
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(final String[] args) throws ParseException, IOException {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGui();
			}
		});
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		if (evt.getSource() == applicationState) {
			Property property = Property.valueFor(evt.getPropertyName());
			if (Property.ErrorMessage.equals(property)) {
				if (evt.getNewValue() != null) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							JOptionPane.showMessageDialog(mainFrame, evt
									.getNewValue());
							applicationState.setErrorMessage(null);
						}
					});
				}
			} else if (Property.CurrentActivity.equals(property)) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						final String id = (evt.getNewValue() == null) ? "": ((Activity) evt.getNewValue())
								.getStartTime().toString("yyyy-MM-dd");
						mainFrame.setTitle(TITLE_PREFIX + " " + id);
					}
				});
			}
		}
	}

}
