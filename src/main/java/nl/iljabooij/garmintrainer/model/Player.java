package nl.iljabooij.garmintrainer.model;

import com.google.inject.ImplementedBy;

/**
 * Interface for objects that can play a race.
 * @author ilja
 *
 */
@ImplementedBy(PlayerImpl.class)
public interface Player {
	/**
	 * Start playing.
	 */
	void play();
	/**
	 * Pause playing.
	 */
	void pause();
	
	/**
	 * check if player is playing
	 * @return true if player is playing, false otherwise
	 */
	boolean isPlaying();
}
