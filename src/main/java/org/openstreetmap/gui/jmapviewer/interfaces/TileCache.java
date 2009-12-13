package org.openstreetmap.gui.jmapviewer.interfaces;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Tile;

//License: GPL. Copyright 2008 by Jan Peter Stotz

/**
 * Implement this interface for creating your custom tile cache for
 * {@link JMapViewer}.
 * 
 * @author Jan Peter Stotz
 */
public interface TileCache {

    /**
     * Retrieves a tile from the cache if present, otherwise <code>null</code>
     * will be returned. The template is used to match the tiles in the cache. If
     * a tile in cache is equal to the template, this tile is returned.
     * 
     * @param template template tile. 
     * @return the requested tile or <code>null</code> if the tile is not
     *         present in the cache
     */
    public Tile getTile(Tile template);

    /**
     * Adds a tile to the cache. How long after adding a tile can be retrieved
     * via {@link #getTile(int, int, int)} is unspecified and depends on the
     * implementation.
     * 
     * @param tile
     */
    public void addTile(Tile tile);
}
