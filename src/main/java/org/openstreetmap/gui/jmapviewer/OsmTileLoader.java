package org.openstreetmap.gui.jmapviewer;

//License: GPL. Copyright 2008 by Jan Peter Stotz

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nl.iljabooij.garmintrainer.util.InjectLogger;

import org.openstreetmap.gui.jmapviewer.filecache.DiskCache;
import org.openstreetmap.gui.jmapviewer.filecache.DiskCacheItem;
import org.openstreetmap.gui.jmapviewer.interfaces.TileLoader;
import org.openstreetmap.gui.jmapviewer.interfaces.TileLoaderListener;
import org.slf4j.Logger;

import com.google.inject.Inject;

/**
 * A {@link TileLoader} implementation that loads tiles from OSM via HTTP.
 * 
 * @author Jan Peter Stotz
 */
public class OsmTileLoader implements TileLoader {
	@InjectLogger
	Logger logger;

	private final JobDispatcher jobDispatcher;
	private final ExecutorService diskLoadExecutor = Executors
			.newSingleThreadExecutor();

	private final ConcurrentMap<Tile, TileLoaderListener> loadingTiles = new ConcurrentHashMap<Tile, TileLoaderListener>();

	private final DiskCache diskCache;

	/**
	 * Holds the used user agent used for HTTP requests. If this field is
	 * <code>null</code>, the default Java user agent is used.
	 */
	private static final String USER_AGENT = "GarminTrainer";
	private static final String ACCEPT_HEADER = "text/html, image/png, image/jpeg, image/gif, */*";

	@Inject
	OsmTileLoader(final JobDispatcher jobDispatcher, final DiskCache diskCache) {
		this.jobDispatcher = jobDispatcher;
		this.diskCache = diskCache;
	}

	/**
	 * Creates a new Job that will attempt to download a tile from an OSM
	 * server.
	 * 
	 * @param tile
	 *            tile to load.
	 * @param listener
	 *            listener to notify when tile is loaded
	 * @return a Runnable that can be executed.
	 */
	private Runnable createTileLoaderJob(final Tile tile) {
		return new Runnable() {

			InputStream input = null;

			public void run() {
				try {
					logger.debug("Attempting to download tile from {}", tile
							.getUrl());
					input = loadTileFromOsm(tile);
					tile.loadImage(input);
					input.close();
					loadedFromNetwork(tile);
				} catch (Exception e) {
					loadingFromDiskFailed(tile);
					logger.error("failed loading " + tile.getZoom() + "/"
							+ tile.getXtile() + "/" + tile.getYtile(), e);
				} finally {
					loadingTiles.remove(tile);
				}
			}

		};
	}

	private InputStream loadTileFromOsm(Tile tile) throws IOException {
		URL url;
		url = new URL(tile.getUrl());
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setRequestProperty("Accept", ACCEPT_HEADER);
		urlConn.setRequestProperty("USER_AGENT", USER_AGENT);
		urlConn.setReadTimeout(30000); // 30 seconds read timeout
		return urlConn.getInputStream();
	}

	private void loadFromDisk(final Tile template) {
		Runnable loadFromDiskRunnable = new Runnable() {
			@Override
			public void run() {
				DiskCacheItem diskCacheItem = diskCache.getEntry(template);
				if (diskCacheItem.tileLoaded()) {
					loadedFromDisk(diskCacheItem.getTile());
				} else {
					loadingFromDiskFailed(template);
				}
			}
		};
		diskLoadExecutor.execute(loadFromDiskRunnable);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadTile(Tile tile, TileLoaderListener tileLoaderListener) {
		if (loadingTiles.putIfAbsent(tile, tileLoaderListener) == null) {
			loadFromDisk(tile);
		}
	}

	private void loadedFromDisk(final Tile tile) {
		tileLoadingFinished(tile, true);
	}

	private void loadingFromDiskFailed(final Tile template) {
		jobDispatcher.addJob(createTileLoaderJob(template));
	}

	private void loadedFromNetwork(final Tile tile) {
		diskCache.addTile(tile, null);
		tileLoadingFinished(tile, true);
	}

	private void loadingFromNetworkFailed(final Tile tile) {
		tileLoadingFinished(tile, false);
	}

	private void tileLoadingFinished(final Tile tile, boolean success) {
		final TileLoaderListener listener = loadingTiles.remove(tile);
		listener.tileLoadingFinished(tile, success);
	}

	@Override
	public void cancelDownloads() {
		jobDispatcher.cancelOutstandingJobs();
		loadingTiles.clear();
	}
}
