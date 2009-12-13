package nl.iljabooij.garmintrainer.model;

import java.beans.PropertyChangeListener;

import com.google.inject.ImplementedBy;

/**
 * Interface for objects that can play a race.
 * @author ilja
 *
 */
@ImplementedBy(PlayerImpl.class)
public interface Player {
	public enum Property {
		PLAYING;
	}
	
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
	
	/**
	 * Add a listener that listens for property changes on the Player.
	 * @param property the property to listen for
	 * @param listener the listener to add
	 */
	public void addPropertyChangeListener(Property property, PropertyChangeListener listener);
}
