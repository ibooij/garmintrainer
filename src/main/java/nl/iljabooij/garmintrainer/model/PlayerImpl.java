package nl.iljabooij.garmintrainer.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.joda.time.Duration;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PlayerImpl implements Player {
	private final ApplicationState applicationState;
	private boolean isPlaying = false;
	
	private final ScheduledExecutorService scheduledExecutorService =
		Executors.newScheduledThreadPool(1);
	
	private final PropertyChangeSupport propertyChangeSupport =
		new PropertyChangeSupport(this);
	
	@Inject
	PlayerImpl(final ApplicationState applicationState) {
		this.applicationState = applicationState;
		
		scheduledExecutorService.scheduleAtFixedRate(new PlayTicker(), 50, 50, TimeUnit.MILLISECONDS);
	}

	@Override
	public void pause() {
		setPlaying(false);
	}

	@Override
	public void play() {
		setPlaying(true);
	}

	private void setPlaying(final boolean playing) {
		boolean oldIsPlaying;
		synchronized (this) {
			oldIsPlaying = isPlaying;
			isPlaying = playing;
		}
		
		propertyChangeSupport.firePropertyChange(Property.PLAYING.name(), oldIsPlaying, playing);
	}
	
	@Override
	public boolean isPlaying() {
		synchronized (this) {
			return isPlaying;
		}
	}

	private class PlayTicker implements Runnable {
		@Override
		public void run() {
			if (isPlaying()) {
				applicationState.increasePlayingTime(Duration.standardSeconds(2));
			}
		}
	}

	@Override
	public void addPropertyChangeListener(Property property,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(property.name(), listener);
	}
}
