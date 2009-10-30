package nl.iljabooij.garmintrainer.gui.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import nl.iljabooij.garmintrainer.model.Activity;
import nl.iljabooij.garmintrainer.model.TrackPoint;
import nl.iljabooij.garmintrainer.util.InjectLogger;

import org.joda.time.Duration;
import org.slf4j.Logger;

import com.google.common.collect.Iterables;
import com.google.inject.internal.Lists;

public class AltitudeDiagramPainter {
	@InjectLogger
	private Logger logger;
	
	private static final Color COLOR = new Color(0xc5af06);
	private static final Color GUIDE_COLOR = Color.black;
	private static final int BOTTOM_OFFSET = 10;
	private static final int TOP_OFFSET = 10;
	private static final int LEFT_OFFSET = 30;
	private static final int RIGHT_OFFSET = 30;
	private static final int MINIMUM_NR_OF_PIXELS_PER_TICK = 100;
	private static final int MAXIMUM_TICKS = 11;
	
	private final int tickLength = 5;
	
	public void paintDiagram(final Activity activity, final BufferedImage targetImage) {
		Graphics2D g2d = targetImage.createGraphics();
		
		int width = targetImage.getWidth();
		int height = targetImage.getHeight();
		
		paintDiagramLine(activity, g2d, width, height);
		paintScaleLine(activity, g2d, width, height);
		paintHorizontalLine(activity, g2d, width, height);
	}
	
	private void paintScaleLine(final Activity activity, final Graphics2D g2d,
			final int width, final int height) {
		g2d.setColor(GUIDE_COLOR);
		g2d.drawLine(LEFT_OFFSET, TOP_OFFSET, LEFT_OFFSET, height - BOTTOM_OFFSET);
		
		drawAltitudeTicks(activity, g2d, width, height);
	}
	
	
	private void drawAltitudeTicks(final Activity activity, final Graphics2D g2d,
			final int width, final int height) {
		List<Integer> ys = determineYsOfTicks(height);
		
		// draw ticks on the left
		for (int y: ys) {
			g2d.drawLine(LEFT_OFFSET, y, LEFT_OFFSET - tickLength, y);
		}
		
		// draw lines through graph
		float[] dashPattern = new float[] {1,10};
		g2d.setColor(Color.gray);
		Stroke currentStroke = g2d.getStroke(); 
		g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 
				10, dashPattern, 0));
		for (int y: ys.subList(1, ys.size())) {
			g2d.drawLine(LEFT_OFFSET, y, width - RIGHT_OFFSET, y);
		}
		g2d.setStroke(currentStroke);
//		
//		
//		
//		AltitudeClass altitudeClass = activity.getAltitudeClass();
//		Length high = altitudeClass.getHigh().convert(Unit.Meter);
//		
//		g2d.drawString(high.toString(), 0, TOP_OFFSET);		
		
	}
	
	private List<Integer> determineYsOfTicks(final int height) {
		int heightWithoutOffsets = height - TOP_OFFSET - BOTTOM_OFFSET;
		int nrTicks =  Math.max(MAXIMUM_TICKS, heightWithoutOffsets / MINIMUM_NR_OF_PIXELS_PER_TICK);
		
		// determine place of ticks
		double pixelsPerTick = ((double) heightWithoutOffsets) / (nrTicks - 1);
		
		List<Integer> ys = Lists.newArrayList();
		for(int tickNr = 0; tickNr < nrTicks; tickNr++) {
			ys.add((int) (heightWithoutOffsets + TOP_OFFSET - tickNr * pixelsPerTick));
		}
		return ys;
	}
	
	
	private void paintHorizontalLine(final Activity activity, final Graphics2D g2d,
			final int width, final int height) {
		g2d.setColor(GUIDE_COLOR);
		g2d.drawLine(LEFT_OFFSET, height- BOTTOM_OFFSET, width - RIGHT_OFFSET, height - BOTTOM_OFFSET);
	}
	private void paintDiagramLine(Activity activity, final Graphics2D g2d,
			final int width, final int height) {
		double minAltitude = Math.min(0.0, activity.getMinimumAltitude().getValueInMeters());
		double maxAltitude = activity.getAltitudeClass().getHigh().getValueInMeters();
		double altitudeRange = maxAltitude - minAltitude;
		
		double durationInSeconds = (double) activity.getGrossDuration().toStandardSeconds().getSeconds();
		
		Path2D path = new Path2D.Double();
		
		// lines from bottom left to first track point.
		path.moveTo(LEFT_OFFSET, height - BOTTOM_OFFSET);
		double x, y;
		for (TrackPoint trackPoint: activity.getTrackPoints()) {
			// calculate x
			x = getXForTrackPoint(activity, width, durationInSeconds,
					trackPoint);
			// and y
			y = getYForTrackPoint(height, minAltitude, altitudeRange,
					trackPoint);
			
			path.lineTo(x, y);
		
		}
		
		// line from last point to bottom
		TrackPoint lastTrackPoint = Iterables.getLast(activity.getTrackPoints());
		x = getXForTrackPoint(activity, width, durationInSeconds,
				lastTrackPoint);
		y = height - BOTTOM_OFFSET;
		path.lineTo(x, y);
		
		// line back to bottom left
		path.closePath();
	
		
		logger.debug("max altitude = {}, min altitude = {}", activity.getMaximumAltitude(),
				activity.getMinimumAltitude());
		
		g2d.setColor(COLOR);
		g2d.fill(path);
	}

	/**
	 * @param height
	 * @param minAltitude
	 * @param altitudeRange
	 * @param trackPoint
	 * @return
	 */
	private double getYForTrackPoint(int height, double minAltitude,
			double altitudeRange, TrackPoint trackPoint) {
		double altitude = trackPoint.getAltitude().getValueInMeters();
		double altitudeNormalized = altitude - minAltitude;
		double relativeAltitude = altitudeNormalized / altitudeRange;
		return height - relativeAltitude * (height - BOTTOM_OFFSET - TOP_OFFSET) - BOTTOM_OFFSET;
	}

	/**
	 * @param activity
	 * @param width
	 * @param durationInSeconds
	 * @param trackPoint
	 * @return
	 */
	private double getXForTrackPoint(Activity activity, int width,
			double durationInSeconds, TrackPoint trackPoint) {
		double fromStartInSeconds = new Duration(activity.getStartTime(), trackPoint.getTime()).toStandardSeconds().getSeconds();
		return ((double) width - LEFT_OFFSET - RIGHT_OFFSET) * (fromStartInSeconds / durationInSeconds) + LEFT_OFFSET;
	}
	
	
}
