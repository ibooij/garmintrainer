package org.openstreetmap.gui.jmapviewer.interfaces;

import org.openstreetmap.gui.jmapviewer.Tile;

//License: GPL. Copyright 2008 by Jan Peter Stotz

/**
 * Interface for implementing a tile loader. Tiles are usually loaded via HTTP
 * or from a file.
 * 
 * @author Jan Peter Stotz
 */
public interface TileLoader {
    /**
     * Load a tile. 
     * 
     * @param tile tile to load
     * @param tileLoaderListener object that will be notified when tile is loaded
     */
    void loadTile(Tile tile,
    		TileLoaderListener tileLoaderListener);
    
    /**
     * Cancel all current downloads.
     */
    void cancelDownloads();
}
