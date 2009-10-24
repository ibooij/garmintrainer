package org.openstreetmap.gui.jmapviewer;

//License: GPL. Copyright 2009 by Stefan Zeller

import java.awt.geom.Point2D;
import java.util.Collection;

/**
 * This class encapsulates a Point2D.Double and provide access
 * via <tt>lat</tt> and <tt>lon</tt>.
 * 
 * @author Jan Peter Stotz
 * 
 */
public class Coordinate {
	private static final double MINIMUM_LATITUDE = -90.0;
	private static final double MAXIMUM_LATITUDE = 90.0;
	private static final double MINIMUM_LONGITUDE = -180.0;
	private static final double MAXIMUM_LONGITUDE = 180.0;
	
	private final Point2D.Double data;

	/**
	 * Create a new Coordinate with 
	 * @param lat
	 * @param lon
	 */
    public Coordinate(double lat, double lon) {
        data = new Point2D.Double(lon, lat);
    }

    public double getLat() {
        return data.y;
    }

    public double getLon() {
        return data.x;
    }

    public String toString() {
        return "Coordinate[" + data.y + ", " + data.x + "]";
    }
    
    /**
     * Get the point between this point and the other one
     * @param other the other point.
     * @return point right between this and the other coordinate
     */
    public Coordinate getPointBetween(final Coordinate other) {
    	// use simple linear interpolation. This is not completely accurate,
    	// but it will do for now.
    	double lat = (getLat() + other.getLat()) / 2.0;
    	double lon = (getLon() + other.getLon()) / 2.0;
    	
    	return new Coordinate(lat, lon);
    }
    
    public static Coordinate[] getBoundingBox(final Collection<Coordinate> coordinates) {
    	double northWestLatitude = MINIMUM_LATITUDE;
    	double southEastLatitude = MAXIMUM_LATITUDE;
    	double northWestLongitude = MAXIMUM_LONGITUDE;
    	double southEastLongitude = MINIMUM_LONGITUDE;
    	
    	for (Coordinate coordinate: coordinates) {
    		northWestLatitude = Math.max(northWestLatitude, coordinate.getLat());
    		northWestLongitude = Math.min(northWestLongitude, coordinate.getLon());
    		southEastLatitude = Math.min(southEastLatitude, coordinate.getLat());
    		southEastLongitude = Math.max(southEastLongitude, coordinate.getLon());
    	}
  
    	final Coordinate northWest = new Coordinate(northWestLatitude, northWestLongitude);
    	final Coordinate southEast = new Coordinate(southEastLatitude, southEastLongitude);
    	
    	return new Coordinate[] {northWest, southEast}; 
    }
}
