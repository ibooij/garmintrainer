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

import javax.swing.table.AbstractTableModel;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

import nl.iljabooij.garmintrainer.model.Activity;
import nl.iljabooij.garmintrainer.model.Speed;
import nl.iljabooij.garmintrainer.model.TrackPoint;
import nl.iljabooij.garmintrainer.model.Length.Unit;

public class SamplesTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;

    /**
     * Columns in the table
     * @author ilja
     */
    private enum Column {
    	Time,
    	Distance,
    	Altitude,
    	AltitudeDelta,
    	Speed;
    }

    /** The Activity used for the data */ 
    private Activity activity;
    
    public void setActivity(final Activity activity) {
    	this.activity = activity;
    	fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public int getRowCount() {
    	if (activity == null) {
    		return 0;
    	} else {
    		return activity.getTrackPoints().size();
    	}
    }

    @Override
    public String getColumnName(final int index) {
        return Column.values()[index].name();
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
    	if (activity == null) {
    		return null;
    	}
        final TrackPoint trackPoint = activity.getTrackPoints().get(rowIndex);

        Column column = Column.values()[columnIndex];
        switch (column) {
        case Time:
        	DateTime dateTime = new DateTime(0, DateTimeZone.UTC);
        	Duration fromStart = new Duration(activity.getStartTime(), trackPoint.getTime());
        	DateTime timeFromStart = dateTime.plus(fromStart);
            return timeFromStart.toString("HH:mm:ss");
        case Distance:
            return trackPoint.getDistance().convert(Unit.Kilometer);
        case Speed: // Speed
        	return speedForRow(rowIndex);
        case Altitude:
        	return trackPoint.getAltitude().convert(Unit.Meter);
        case AltitudeDelta:
        	return trackPoint.getAltitudeDelta().convert(Unit.Meter);
        default:
            return null;
        }
    }

    /**
     * Calculate the speed for a row.
     * @param rowIndex index of the row
     * @returns speed for the row 
     */
	private Speed speedForRow(int rowIndex) {
		final TrackPoint trackPoint = activity.getTrackPoints().get(rowIndex);
		final Speed speed = trackPoint.getSpeed();
		return speed.convert(nl.iljabooij.garmintrainer.model.Speed.Unit.KilometersPerHour);
	}
}
