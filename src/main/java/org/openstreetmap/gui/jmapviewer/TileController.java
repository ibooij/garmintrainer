package org.openstreetmap.gui.jmapviewer;

import org.openstreetmap.gui.jmapviewer.interfaces.TileCache;
import org.openstreetmap.gui.jmapviewer.interfaces.TileLoader;
import org.openstreetmap.gui.jmapviewer.interfaces.TileLoaderListener;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;

import com.google.inject.Inject;

public class TileController implements TileLoaderListener {
    private final TileLoader tileLoader;
    private final TileCache tileCache;
    private final TileSource tileSource;
    
    private TileLoaderListener listener;
    
    @Inject
    public TileController(final TileSource tileSource, final TileLoader tileLoader,
    		final TileCache tileCache) {
        this.tileSource = tileSource;
        this.tileLoader = tileLoader;
        this.tileCache = tileCache;
    }
    
    public void setTileLoaderListener(final TileLoaderListener listener) {
    	this.listener = listener;
    }
    /**
     * retrieves a tile from the cache. If the tile is not present in the cache
     * a load job is added to the working queue of {@link JobThread}.
     * 
     * @param tilex
     * @param tiley
     * @param zoom
     * @return specified tile from the cache or <code>null</code> if the tile
     *         was not found in the cache.
     */
    public Tile getTile(int tilex, int tiley, int zoom) {
        int max = (1 << zoom);
        
        // if illegal tile, just return null
        if (tilex < 0 || tilex >= max || tiley < 0 || tiley >= max)
            return null;
        
        // try to get the tile from cache
        Tile template = new Tile(tileSource, tilex, tiley, zoom);
        Tile tile = tileCache.getTile(template);
        if (tile != null) {
        	return tile;
        }
        
        // tile needs to be loaded. 
        tileLoader.loadTile(template, this);
        
        return null;
    }

    public TileSource getTileSource() {
        return tileSource;
    }

    /**
     * 
     */
    public void cancelOutstandingJobs() {
    	tileLoader.cancelDownloads();
    }

	@Override
	public void tileLoadingFinished(Tile tile, boolean success) {
		tileCache.addTile(tile);
		listener.tileLoadingFinished(tile, success);
	}
}
