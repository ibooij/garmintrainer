package nl.iljabooij.garmintrainer.model;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import nl.iljabooij.garmintrainer.util.InjectLogger;

import org.joda.time.Duration;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PlayerImpl implements Player {
	@InjectLogger
	private Logger logger;
	
	private final ApplicationState applicationState;
	private boolean isPlaying = false;
	
	private final ScheduledExecutorService scheduledExecutorService =
		Executors.newScheduledThreadPool(1);
	
	@Inject
	PlayerImpl(final ApplicationState applicationState) {
		this.applicationState = applicationState;
		
		scheduledExecutorService.scheduleAtFixedRate(new PlayTicker(), 1, 1, TimeUnit.SECONDS);
	}

	@Override
	public void pause() {
		synchronized (this) {
			isPlaying = false;
		}
	}

	@Override
	public void play() {
		synchronized (this) {
			isPlaying = true;
		}
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
				applicationState.increasePlayingTime(Duration.standardSeconds(10));
				logger.debug("playing!, playing time = {}", applicationState.getCurrentPlayingTime());
			} else {
				logger.debug("paused");
			}
		}
	}
}
