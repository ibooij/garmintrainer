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
package nl.iljabooij.garmintrainer.parser.digester;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import nl.iljabooij.garmintrainer.model.Activity;
import nl.iljabooij.garmintrainer.model.Length;
import nl.iljabooij.garmintrainer.parser.TcxParser;
import nl.iljabooij.garmintrainer.util.InjectLogger;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.digester.Digester;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Implementation of {@link TcxParser} that uses Jakarta Commons-Digester to 
 * implement parsing of the tcx file.
 * 
 * @author ilja
 *
 */
public class CommonsDigesterTcxParser implements TcxParser {
	@InjectLogger private Logger logger;
	
	private final static String ACTIVITY = "TrainingCenterDatabase/Activities/Activity";
	private final static String LAP = ACTIVITY + "/Lap";
	private final static String TRACK = LAP + "/Track";
	private final static String TRACK_POINT = TRACK + "/Trackpoint";
	
	@Inject
	private Provider<Digester> digesterProvider;
	
	/**
	 * Register our own Converter implementations with ConvertUtils of commons-beanutils.
	 */
	static {
		ConvertUtils.register(new DateTimeConverter(), DateTime.class);
		ConvertUtils.register(new LengthConverter(), Length.class);
	}
	
	private Digester setUpDigester() {
		Digester digester = digesterProvider.get();
		
		digester.addObjectCreate(ACTIVITY, ActivityType.class);
		
		digester.addSetNext(ACTIVITY, "add", "java.lang.Object");
		digester.addBeanPropertySetter(ACTIVITY + "/Id", "id");
		
		digester.addFactoryCreate(LAP, LapBuilderFactory.class);
		digester.addSetNext(LAP, "addLap");
		
		digester.addObjectCreate(TRACK, TrackType.class);
		digester.addSetNext(TRACK, "addTrack");
		
		digester.addObjectCreate(TRACK_POINT, TrackPointType.class);
		digester.addBeanPropertySetter(TRACK_POINT + "/Time", "time");
		digester.addBeanPropertySetter(TRACK_POINT + "/AltitudeMeters", "altitude");
		digester.addBeanPropertySetter(TRACK_POINT + "/Position/LatitudeDegrees", "latitude");
		digester.addBeanPropertySetter(TRACK_POINT + "/Position/LongitudeDegrees", "longitude");
		digester.addBeanPropertySetter(TRACK_POINT + "/HeartRateBpm/Value", "heartRate");
		digester.addBeanPropertySetter(TRACK_POINT + "/DistanceMeters", "distance");
		digester.addSetNext(TRACK_POINT, "addTrackPoint");
		
		return digester;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Activity> parse(final InputStream inputStream) throws ParseException {
		if (logger.isDebugEnabled()) {
			logger.debug("starting parsing inputStream");
		}

		Digester digester = setUpDigester();
		final ArrayList<ActivityType> activityBuilders = Lists.newArrayList();
		digester.push(activityBuilders);
		
		try {
			digester.parse(inputStream);
		} catch (IOException e) {
			throw new ParseException("IOException parsing TCX file", e);
		} catch (SAXException e) {
			throw new ParseException("SAXException parsing TCX file", e);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("finished parsing inputStream");
		}
		
		final ArrayList<Activity> activities = Lists.newArrayList();
		for (ActivityType activityBuilder: activityBuilders) {
			activities.add(activityBuilder.build());
		}
		return activities;
	}
}
