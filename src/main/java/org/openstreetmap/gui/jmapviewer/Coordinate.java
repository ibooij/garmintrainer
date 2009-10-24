package org.openstreetmap.gui.jmapviewer;

//License: GPL. Copyright 2009 by Stefan Zeller

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class encapsulates a Point2D.Double and provide access
 * via <tt>lat</tt> and <tt>lon</tt>.
 * 
 * @author Jan Peter Stotz
 * 
 */
public class Coordinate {
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
    	final Comparator<Coordinate> latitudeComparator = new Comparator<Coordinate>() {
			@Override
			public int compare(Coordinate o1, Coordinate o2) {
				// higher latitudes are further north, so we need to negate the value of compare
				return - Double.compare(o1.getLat(), o2.getLat());
			}
		};
		final Comparator<Coordinate> longitudeComparator = new Comparator<Coordinate>() {
			@Override
			public int compare(Coordinate o1, Coordinate o2) {
				return Double.compare(o1.getLon(), o2.getLon());
			}
		};

		// this completely non-optimal, but easy.. (lazy me)
    	double northWestLatitude = Collections.min(coordinates, latitudeComparator).getLat();
    	double southEastLatitude = Collections.max(coordinates, latitudeComparator).getLat();
    	double northWestLongitude = Collections.min(coordinates, longitudeComparator).getLon();
    	double southEastLongitude = Collections.max(coordinates, longitudeComparator).getLon();
 
    	final Coordinate northWest = new Coordinate(northWestLatitude, northWestLongitude);
    	final Coordinate southEast = new Coordinate(southEastLatitude, southEastLongitude);
    	
    	return new Coordinate[] {northWest, southEast}; 
    }
}
