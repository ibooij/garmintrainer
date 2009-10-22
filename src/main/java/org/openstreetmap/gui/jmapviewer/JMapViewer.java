package org.openstreetmap.gui.jmapviewer;

//License: GPL. Copyright 2008 by Jan Peter Stotz

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openstreetmap.gui.jmapviewer.interfaces.TileCache;
import org.openstreetmap.gui.jmapviewer.interfaces.TileLoader;
import org.openstreetmap.gui.jmapviewer.interfaces.TileLoaderListener;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.internal.ToStringBuilder;

/**
 * 
 * Provides a simple panel that displays pre-rendered map tiles loaded from the
 * OpenStreetMap project.
 * 
 * @author Jan Peter Stotz
 * 
 */
public class JMapViewer extends JPanel implements TileLoaderListener {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JMapViewer.class);

	private static final long serialVersionUID = 1L;

	public static final int MAX_ZOOM = 22;
	public static final int MIN_ZOOM = 0;
	protected TileController tileController;

	/**
	 * Coordinate of pixel in the center of the displayed map.
	 */
	private Coordinate center = new Coordinate(0.0, 0.0);

	/**
	 * Current zoom level
	 */
	protected int zoom = 3;

	private Image previousTilesImage;
	private List<TileIdentifier> previouslyVisibleTiles = Lists.newArrayList();
	private boolean newTilesLoaded = false;

	protected JSlider zoomSlider;
	protected JButton zoomInButton;
	protected JButton zoomOutButton;

	/**
	 * Creates a standard {@link JMapViewer} instance that can be controlled via
	 * mouse: hold right mouse button for moving, double click left mouse button
	 * or use mouse wheel for zooming. Loaded tiles are stored the
	 * {@link MemoryTileCache} and the tile loader uses 4 parallel threads for
	 * retrieving the tiles.
	 */
	public JMapViewer() {
		this(new MemoryTileCache(), 4);
		new DefaultMapController(this);
	}

	public JMapViewer(TileCache tileCache, int downloadThreadCount) {
		super();
		tileController = new TileController(new OsmTileSource.Mapnik(),
				tileCache, this);
		setLayout(null);
		initializeZoomSlider();
		setMinimumSize(new Dimension(Tile.SIZE, Tile.SIZE));
		setPreferredSize(new Dimension(400, 400));
		setDisplayPositionByLatLon(52.3, 4.5);
	}

	protected void initializeZoomSlider() {
		zoomSlider = new JSlider(MIN_ZOOM, tileController.getTileSource()
				.getMaxZoom());
		zoomSlider.setValue(zoom);
		zoomSlider.setOrientation(JSlider.VERTICAL);
		zoomSlider.setBounds(11, 10, 30, 145);
		zoomSlider.setOpaque(false);
		zoomSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				setZoom(zoomSlider.getValue());
			}
		});
		add(zoomSlider);
		int size = 18;

		zoomInButton = new JButton("+");
		zoomInButton.setFont(new Font("sansserif", Font.PLAIN, 8));
		zoomInButton.setMargin(new Insets(0, 0, 0, 0));

		zoomInButton.setBounds(4, 155, size, size);
		zoomInButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoomIn();
			}
		});
		add(zoomInButton);

		zoomOutButton = new JButton("-");
		zoomOutButton.setFont(new Font("sansserif", Font.BOLD, 8));
		zoomOutButton.setMargin(new Insets(0, 0, 0, 0));
		zoomOutButton.setBounds(8 + size, 155, size, size);
		zoomOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoomOut();
			}
		});
		add(zoomOutButton);
	}

	/**
	 * Changes the map pane so that it is centered on the specified coordinate
	 * at the given zoom level.
	 * 
	 * @param lat
	 *            latitude of the specified coordinate
	 * @param lon
	 *            longitude of the specified coordinate
	 * @param zoom
	 *            {@link #MIN_ZOOM} <= zoom level <= {@link #MAX_ZOOM}
	 */
	public void setDisplayPositionByLatLon(double lat, double lon) {
		center = new Coordinate(lat, lon);

		repaint();
	}

	private Point getCenterPoint() {
		int x = OsmMercator.LonToX(center.getLon(), zoom);
		int y = OsmMercator.LatToY(center.getLat(), zoom);

		return new Point(x, y);
	}

	/**
	 * Calculates the position on the map of a given coordinate
	 * 
	 * @param lat
	 * @param lon
	 * @param checkOutside
	 * @return point on the map or <code>null</code> if the point is not visible
	 *         and checkOutside set to <code>true</code>
	 */
	public Point getMapPosition(double lat, double lon, boolean checkOutside) {
		int x = OsmMercator.LonToX(lon, zoom);
		int y = OsmMercator.LatToY(lat, zoom);

		Point center = getCenterPoint();
		x -= center.x - getWidth() / 2;
		y -= center.y - getHeight() / 2;
		if (checkOutside) {
			if (x < 0 || y < 0 || x > getWidth() || y > getHeight()) {
				return null;
			}
		}
		return new Point(x, y);
	}

	/**
	 * Calculates the position on the map of a given coordinate
	 * 
	 * @param coord
	 * @return point on the map or <code>null</code> if the point is not visible
	 *         and checkOutside set to <code>true</code>
	 */
	public Point getMapPosition(Coordinate coord, boolean checkOutside) {
		if (coord != null) {
			return getMapPosition(coord.getLat(), coord.getLon(), checkOutside);
		} else {
			return null;
		}
	}

	private static class TileIdentifier {
		private final Point tileCoordinate;
		private final int zoom;

		public TileIdentifier(final Point tileCoordinate, final int zoom) {
			this.tileCoordinate = tileCoordinate;
			this.zoom = zoom;
		}

		public Point getTileCoordinate() {
			return tileCoordinate;
		}

		public int getZoom() {
			return zoom;
		}

		public String toString() {
			return new ToStringBuilder(TileIdentifier.class).add(
					"tileCoordinate", tileCoordinate).add("zoom", zoom)
					.toString();
		}

		@Override
		public boolean equals(Object other) {
			if (other == this) {
				return true;
			}
			if (other == null) {
				return false;
			}
			if (!(other instanceof TileIdentifier)) {
				return false;
			}
			final TileIdentifier otherTileIdentifier = (TileIdentifier) other;

			return new EqualsBuilder().append(zoom, otherTileIdentifier.zoom)
					.append(tileCoordinate, otherTileIdentifier.tileCoordinate)
					.isEquals();
		}

		/** Seeds for {@link HashCodeBuilder}. */
		private static final int[] HASH_CODE_SEEDS = new int[] { 31, 43 };

		/** {@inheritDoc} */
		@Override
		public int hashCode() {
			return new HashCodeBuilder(HASH_CODE_SEEDS[0], HASH_CODE_SEEDS[1])
					.append(zoom).append(tileCoordinate).toHashCode();
		}
	}

	private List<TileIdentifier> getVisibileTiles() {
		Point centerPoint = getCenterPoint();
		int width = getWidth() - getInsets().left - getInsets().right;
		int height = getHeight() - getInsets().top - getInsets().bottom;
		// make list of all tiles that are visible:
		int leftX = centerPoint.x - width / 2;
		int rightX = centerPoint.x + width / 2;
		int topY = centerPoint.y - height / 2;
		int bottomY = centerPoint.y + height / 2;

		int tileXMin = leftX / Tile.SIZE;
		int tileXMax = (rightX / Tile.SIZE) + 1;
		int tileYMin = topY / Tile.SIZE;
		int tileYMax = (bottomY / Tile.SIZE) + 1;

		List<TileIdentifier> visibileTiles = Lists.newLinkedList();
		for (int xTile = tileXMin; xTile <= tileXMax; xTile++) {
			for (int yTile = tileYMin; yTile <= tileYMax; yTile++) {
				visibileTiles.add(new TileIdentifier(new Point(xTile, yTile),
						zoom));
			}
		}

		return visibileTiles;
	}

	private Image paintVisibleMapTiles(
			final List<TileIdentifier> tileIdentifiers) {
		LOGGER.debug("Painting visible tiles onto an image.");
		final TileIdentifier firstTileIdentifier = tileIdentifiers.get(0);
		final TileIdentifier lastTileIdentifier = Iterables
				.getLast(tileIdentifiers);

		int tilesHorizontal = (lastTileIdentifier.getTileCoordinate().x - firstTileIdentifier
				.getTileCoordinate().x) + 1;
		int tilesVertical = tileIdentifiers.size() / tilesHorizontal;

		BufferedImage tilesImage = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration().createCompatibleImage(
						tilesHorizontal * Tile.SIZE, tilesVertical * Tile.SIZE);

		for (TileIdentifier tileIdentifier : tileIdentifiers) {
			int xLocation = tileIdentifier.getTileCoordinate().x
					- firstTileIdentifier.getTileCoordinate().x;
			int yLocation = tileIdentifier.getTileCoordinate().y
					- firstTileIdentifier.getTileCoordinate().y;
			Tile tile = tileController.getTile(tileIdentifier
					.getTileCoordinate().x,
					tileIdentifier.getTileCoordinate().y, tileIdentifier
							.getZoom());
			if (tile != null) {
				int posX = xLocation * Tile.SIZE;
				int posY = yLocation * Tile.SIZE;
				tile.paint(tilesImage.getGraphics(), posX, posY);
			}
		}

		return tilesImage;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Image tilesImage;
		final List<TileIdentifier> tileIdentifiers = getVisibileTiles();
		if (tileIdentifiers.equals(previouslyVisibleTiles) && !newTilesLoaded) {
			tilesImage = previousTilesImage;
		} else {
			tilesImage = paintVisibleMapTiles(tileIdentifiers);
			previousTilesImage = tilesImage;
			previouslyVisibleTiles = tileIdentifiers;
			newTilesLoaded = false;
		}

		// determine position of left top of viewport.
		Point point = getCenterPoint();
		point.translate(-(getWidth() / 2), -(getHeight() / 2));

		LOGGER.debug("map point of left top = {}", point);

		// determine left top position of visible tiles
		TileIdentifier leftTopOfTiles = tileIdentifiers.get(0);
		Point leftTopPoint = new Point(leftTopOfTiles.getTileCoordinate().x
				* Tile.SIZE, leftTopOfTiles.getTileCoordinate().y * Tile.SIZE);

		LOGGER.debug("map point of left top of visible tiles = {}",
				leftTopPoint);

		Point offset = new Point(point.x - leftTopPoint.x, point.y
				- leftTopPoint.y);

		g.drawImage(tilesImage, 0, 0, getWidth(), getHeight(), offset.x,
				offset.y, offset.x + getWidth(), offset.y + getHeight(), null);
	}

	/**
	 * Moves the visible map pane.
	 * 
	 * @param x
	 *            horizontal movement in pixel.
	 * @param y
	 *            vertical movement in pixel
	 */
	public void moveMap(int deltaX, int deltaY) {
		// center to point
		int centerX = OsmMercator.LonToX(center.getLon(), zoom);
		int centerY = OsmMercator.LatToY(center.getLat(), zoom);

		double newCenterLon = OsmMercator.XToLon(centerX + deltaX, zoom);
		double newCenterLat = OsmMercator.YToLat(centerY + deltaY, zoom);

		center = new Coordinate(newCenterLat, newCenterLon);
		repaint();
	}

	/**
	 * @return the current zoom level
	 */
	public int getZoom() {
		return zoom;
	}

	/**
	 * Increases the current zoom level by one
	 */
	public void zoomIn() {
		setZoom(zoom + 1);
	}

	/**
	 * Increases the current zoom level by one
	 */
	public void zoomIn(Point mapPoint) {
		setZoom(zoom + 1, mapPoint);
	}

	/**
	 * Decreases the current zoom level by one
	 */
	public void zoomOut() {
		setZoom(zoom - 1);
	}

	/**
	 * Decreases the current zoom level by one
	 */
	public void zoomOut(Point mapPoint) {
		setZoom(zoom - 1, mapPoint);
	}

	public void setZoom(int zoom, Point mapPoint) {
		if (zoom > tileController.getTileSource().getMaxZoom()
				|| zoom < tileController.getTileSource().getMinZoom()
				|| zoom == this.zoom)
			return;

		tileController.cancelOutstandingJobs(); // Clearing outstanding load

		int xDiff = (getWidth() / 2) - mapPoint.x;
		int yDiff = (getHeight() / 2) - mapPoint.y;

		moveMap(xDiff, yDiff);

		this.zoom = zoom;
		zoomChanged();

		repaint();
	}

	public void setZoom(int zoom) {
		setZoom(zoom, new Point(getWidth() / 2, getHeight() / 2));
	}

	/**
	 * Every time the zoom level changes this method is called. Override it in
	 * derived implementations for adapting zoom dependent values. The new zoom
	 * level can be obtained via {@link #getZoom()}.
	 */
	private void zoomChanged() {
		zoomSlider.setToolTipText("Zoom level " + zoom);
		zoomSlider.setValue(zoom);
		zoomInButton.setToolTipText("Zoom to level " + (zoom + 1));
		zoomOutButton.setToolTipText("Zoom to level " + (zoom - 1));
		zoomOutButton.setEnabled(zoom > tileController.getTileSource()
				.getMinZoom());
		zoomInButton.setEnabled(zoom < tileController.getTileSource()
				.getMaxZoom());
	}

	public void setZoomContolsVisible(boolean visible) {
		zoomSlider.setVisible(visible);
		zoomInButton.setVisible(visible);
		zoomOutButton.setVisible(visible);
	}

	public boolean getZoomContolsVisible() {
		return zoomSlider.isVisible();
	}

	public void setTileSource(TileSource tileSource) {
		if (tileSource.getMaxZoom() > MAX_ZOOM)
			throw new RuntimeException("Maximum zoom level too high");
		if (tileSource.getMinZoom() < MIN_ZOOM)
			throw new RuntimeException("Minumim zoom level too low");
		tileController.setTileSource(tileSource);
		zoomSlider.setMinimum(tileSource.getMinZoom());
		zoomSlider.setMaximum(tileSource.getMaxZoom());
		tileController.cancelOutstandingJobs();
		if (zoom > tileSource.getMaxZoom())
			setZoom(tileSource.getMaxZoom());
		repaint();
	}

	public void tileLoadingFinished(Tile tile, boolean success) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// newTilesLoaded should only be touched on the Swing EDT!
				newTilesLoaded = true;
				repaint();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	public TileCache getTileCache() {
		return tileController.getTileCache();
	}

	public void setTileLoader(TileLoader loader) {
		tileController.setTileLoader(loader);
	}
}
