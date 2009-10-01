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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import nl.iljabooij.garmintrainer.model.Speed;
import nl.iljabooij.garmintrainer.model.TrackPoint;
import nl.iljabooij.garmintrainer.model.Speed.Unit;

import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

public class SmallMapMarker implements MapMarker {
	private final TrackPoint trackPoint;
	
	public SmallMapMarker(final TrackPoint trackPoint) {
		this.trackPoint = trackPoint;
	}
	@Override
	public double getLat() {
		return trackPoint.getLatitude();
	}

	@Override
	public double getLon() {
		return trackPoint.getLongitude();
	}

	private static final Speed _10_KM_H = Speed.createExactSpeed(10.0, Unit.KilometersPerHour);
	private static final Speed _20_KM_H = Speed.createExactSpeed(20.0, Unit.KilometersPerHour);
	private static final Speed _30_KM_H = Speed.createExactSpeed(30.0, Unit.KilometersPerHour);
	private static final Speed _40_KM_H = Speed.createExactSpeed(40.0, Unit.KilometersPerHour);
	
	private Color computeColor() {
		final Speed speed = trackPoint.getSpeed();
		if (speed.compareTo(_10_KM_H) < 0) 
			return Color.BLACK;
		if (speed.compareTo(_20_KM_H) < 0)
			return Color.BLUE;
		if (speed.compareTo(_30_KM_H) < 0)
			return Color.GREEN;
		if (speed.compareTo(_40_KM_H) < 0) 
			return Color.YELLOW;
		else
			return Color.RED;
	}
	
	@Override
	public void paint(final Graphics g, final Point position) {
		final int size_h = 3;
        final int size = size_h * 2;
        g.setColor(computeColor());
        
        g.fillOval(position.x - size_h, position.y - size_h, size, size);
        g.setColor(Color.BLACK);
        g.drawOval(position.x - size_h, position.y - size_h, size, size);
	}

}
