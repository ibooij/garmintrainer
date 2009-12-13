package org.openstreetmap.gui.jmapviewer;

//License: GPL. Copyright 2008 by Jan Peter Stotz
// Copyright 2009 Ilja Booij

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openstreetmap.gui.jmapviewer.interfaces.TileCache;

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

	private final Map<Tile, Tile> cache;

	MemoryTileCache() {
		Map<Tile,Tile> unsynchronizedCache = new LinkedHashMap<Tile,Tile>(
				CACHE_SIZE, 0.75f, true) {
			private static final long serialVersionUID = 1L;
			protected boolean removeEldestEntry(Map.Entry<Tile, Tile> entry) {
		        return size() > CACHE_SIZE;
			}
		};
		cache = Collections.synchronizedMap(unsynchronizedCache);
	}

	@Override
	public void addTile(Tile tile) {
		cache.put(tile, tile);
	}

	@Override
	public Tile getTile(final Tile tile) {
		Tile entry = cache.get(tile);
		if (entry == null)
			return null;
		return entry;
	}
}
