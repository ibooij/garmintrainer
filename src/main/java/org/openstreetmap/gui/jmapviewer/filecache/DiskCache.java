package org.openstreetmap.gui.jmapviewer.filecache;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import nl.iljabooij.garmintrainer.util.InjectLogger;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.openstreetmap.gui.jmapviewer.Tile;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource.TileUpdate;
import org.slf4j.Logger;

import com.google.inject.internal.Nullable;

/**
 * Cache for tiles which caches the files on the local filesystem.
 * 
 * @author ilja
 * 
 */
public class DiskCache {
	@InjectLogger
	Logger logger;

	private static final File MAIN_CACHE_DIR = new File(System
			.getProperty("java.io.tmpdir"), "garmintrainer-cache");

	private static final Duration MAX_AGE = Duration.standardDays(1);

	public void addTile(final Tile tile, @Nullable final String etag) {
		final File cacheFile = getFileForTile(tile);
		final File directory = cacheFile.getParentFile();

		if (!directory.exists()) {
			directory.mkdirs();
		}

		try {
			ImageIO.write(tile.getImage(), tile.getSource().getTileType(),
					cacheFile);
		} catch (IOException e) {
			logger.error("Error writing cache for tile.", e);
		}
	}

	public DiskCacheItem getEntry(final Tile template) {
		final File cacheFile = getFileForTile(template);

		Tile result = null;
		DateTime lastModified = null;
		
		if (cacheFile.exists()) {
			if (logger.isDebugEnabled()) {
				logger.debug("cache file exists for {}", template);
			}
			lastModified = new DateTime(cacheFile.lastModified());
			if (cacheFileYoungEnough(cacheFile)) {
				try {
					BufferedImage image = ImageIO.read(cacheFile);
					template.setImage(image);
					result = template;
				} catch (IOException e) {
					logger.error("unable to load image for tile", e);
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Cache file too old, will need to check");
				}
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("cache does not file exist for {}", template);
			}
		}

		return new DiskCacheItem(result, null, lastModified);
	}

	/**
	 * Check if the cache file is young enough to use. If so, return true;
	 * 
	 * @param cacheFile
	 *            the cache file to check.
	 * @return true if young enough, false otherwise.
	 */
	private boolean cacheFileYoungEnough(File cacheFile) {
		DateTime expirationTime = new DateTime().minus(MAX_AGE);

		DateTime lastModification = new DateTime(cacheFile.lastModified());

		return lastModification.isAfter(expirationTime);
	}

	/**
	 * Get the File which should be used for the tile.
	 * 
	 * @param tile
	 *            the tile
	 * @return file for the tile. No check is done whether or not the tile
	 *         exists.
	 */
	private File getFileForTile(final Tile tile) {
		final File dir = new File(MAIN_CACHE_DIR, tile.getSource().getName());

		final String filename = tile.getZoom() + "_" + tile.getXtile() + "_"
				+ tile.getYtile() + "." + tile.getSource().getTileType();

		return new File(dir, filename);
	}

	/**
	 * Get file in which the etag for a file will be stored.
	 * @param tile
	 * @return
	 */
	private File getFileForEtag(final Tile tile) {
		if (tile.getSource().getTileUpdate() == TileUpdate.IfNoneMatch) {
			final File dir = new File(MAIN_CACHE_DIR, tile.getSource().getName());

			final String filename = tile.getZoom() + "_" + tile.getXtile() + "_"
				+ tile.getYtile() + ".etag";

			return new File(dir, filename);
		} else {
			return null;
		}
	}
}
