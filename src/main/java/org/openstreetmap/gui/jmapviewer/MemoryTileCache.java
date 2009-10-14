package org.openstreetmap.gui.jmapviewer;

//License: GPL. Copyright 2008 by Jan Peter Stotz
// Copyright 2009 Ilja Booij

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openstreetmap.gui.jmapviewer.interfaces.TileCache;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;

/**
 * {@link TileCache} implementation that stores all {@link Tile} objects in
 * memory up to a certain limit ({@link #getCacheSize()}). If the limit is
 * exceeded the least recently used {@link Tile} objects will be deleted.
 * 
 * @author Jan Peter Stotz
 */
public class MemoryTileCache implements TileCache {
	/**
	 * Default cache size
	 */
	private static final int CACHE_SIZE = 200;

	private final Map<String, CacheEntry> cache;

	MemoryTileCache() {
		Map<String, CacheEntry> unsynchronizedCache = new LinkedHashMap<String, CacheEntry>(
				CACHE_SIZE, 0.75f, true) {
			private static final long serialVersionUID = 1L;
			protected boolean removeEldestEntry(Map.Entry<String, CacheEntry> entry) {
		        return size() > CACHE_SIZE;
			}
		};
		cache = Collections.synchronizedMap(unsynchronizedCache);
	}

	public void addTile(Tile tile) {
		CacheEntry entry = createCacheEntry(tile);
		cache.put(tile.getKey(), entry);
	}

	public Tile getTile(TileSource source, int x, int y, int z) {
		CacheEntry entry = cache.get(Tile.getTileKey(source, x, y, z));
		if (entry == null)
			return null;
		return entry.getTile();
	}

	protected CacheEntry createCacheEntry(Tile tile) {
		return new CacheEntry(tile);
	}

	/**
	 * Clears the cache deleting all tiles from memory
	 */
	public void clear() {
		cache.clear();
	}

	public int getTileCount() {
		return cache.size();
	}

	/**
	 * Linked list element holding the {@link Tile} and links to the
	 * {@link #next} and {@link #prev} item in the list.
	 */
	private static class CacheEntry {
		private Tile tile;

		protected CacheEntry(Tile tile) {
			this.tile = tile;
		}

		public Tile getTile() {
			return tile;
		}
	}
}
