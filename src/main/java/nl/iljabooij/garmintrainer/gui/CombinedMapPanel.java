package nl.iljabooij.garmintrainer.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.joda.time.DateTime;

import nl.iljabooij.garmintrainer.model.ApplicationState;
import nl.iljabooij.garmintrainer.model.Player;
import nl.iljabooij.garmintrainer.model.ApplicationState.Property;

import com.google.inject.Inject;

public class CombinedMapPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JButton playPauseButton;
	private final JLabel timeLabel;
	private final Player player;

	@Inject
	CombinedMapPanel(final ApplicationState applicationState, 
			final AnimatingMapViewer animatingMapViewer, final Player player) {
		super(new BorderLayout());
		setName("Player");
		
		this.player = player;
		timeLabel = new JLabel("");
		playPauseButton = new JButton("Play");
		playPauseButton.setEnabled(applicationState.getCurrentActivity() != null);
		playPauseButton.addActionListener(new PlayPauseListener());
		final JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		controlPanel.add(playPauseButton);
		controlPanel.add(timeLabel);
		controlPanel.setBorder(new TitledBorder("Controls"));
		
		add(controlPanel, BorderLayout.NORTH);
		add(animatingMapViewer, BorderLayout.CENTER);
		
		applicationState.addPropertyChangeListener(Property.CurrentActivity, new CurrentActivityListener());
		applicationState.addPropertyChangeListener(Property.CurrentPlayingTime, new CurrentPlayingTimeListener());
	}
	
	private class PlayPauseListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (player.isPlaying()) {
				player.pause();
				playPauseButton.setText("Play");
			} else {
				player.play();
				playPauseButton.setText("Pause");
			}
		}
		
	}
	
	private class CurrentActivityListener implements PropertyChangeListener {
		@Override
		public void propertyChange(final PropertyChangeEvent evt) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					playPauseButton.setEnabled((evt.getNewValue() != null));
				}
			});
		}
	}
	
	private class CurrentPlayingTimeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(final PropertyChangeEvent evt) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (evt.getNewValue() == null) {
						timeLabel.setText("");
					} else {
						DateTime time = (DateTime) evt.getNewValue();
						timeLabel.setText(time.toString("HH:mm:ss"));
					}
				}
			});
		}
	}
}
