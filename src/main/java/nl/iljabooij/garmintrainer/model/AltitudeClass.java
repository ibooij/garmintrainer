package nl.iljabooij.garmintrainer.model;


/**
 * AltitudeClass. 
 * 
 * @author ilja
 *
 */
public enum AltitudeClass {
	LOW(50),
	HILLS_LOW(250),
	HILLS_HIGH(500),
	MOUNTAINS_LOW(1000),
	MOUNTAINS_MEDIUM(2500),
	MOUNTAINS_HIGH(4000),
	MOUNTAINS_EXTREME(6000);
	
	private final Length high;
	
	private AltitudeClass(final int high) {
		this.high = Length.createLengthInMeters(high);
	}
	
	public static AltitudeClass forMaximumAltitude(final Length maximumAltitude) {
		for (AltitudeClass altitudeClass: values()) {
			if (maximumAltitude.compareTo(altitudeClass.high) < 0) {
				return altitudeClass;
			}
		}
		return MOUNTAINS_EXTREME;
	}
	
	/**
	 * Use this method to get high :-)
	 * 
	 * @return highest altitude for this class.
	 */
	public Length getHigh() {
		return high;
	}
}
