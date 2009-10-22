package nl.iljabooij.garmintrainer.model;

import org.joda.time.DateTime;

/**
 * A {@link TrackPoint} implemented as a bean, so we can set values when needed.
 * Currently, only the time is needed..
 * @author ilja
 *
 */
public class TrackPointBean implements TrackPoint {
	private DateTime time;
	
	/**
	 * Set the time of the {@link TrackPointBean}.
	 * @param time the time
	 */
	public void setTime(DateTime time) {
		this.time = time;
	}

	@Override
	public Length getAltitude() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getAltitudeDelta() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Length getDistance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHeartRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getLatitude() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getLongitude() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Speed getSpeed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DateTime getTime() {
		return time;
	}

	@Override
	public boolean hasPosition() {
		// TODO Auto-generated method stub
		return false;
	}

}
