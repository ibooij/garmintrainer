package org.openstreetmap.gui.jmapviewer;

import org.openstreetmap.gui.jmapviewer.interfaces.TileCache;
import org.openstreetmap.gui.jmapviewer.interfaces.TileLoader;
import org.openstreetmap.gui.jmapviewer.interfaces.TileLoaderListener;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;

import com.google.inject.Inject;

public class TileController {
    protected TileLoader tileLoader;
    protected TileCache tileCache;
    protected TileSource tileSource;
    
    JobDispatcher jobDispatcher;
    
    @Inject
    public TileController(final TileSource tileSource, final TileLoader tileLoader,
    		final TileCache tileCache, final JobDispatcher jobDispatcher) {
        this.tileSource = tileSource;
        this.tileLoader = tileLoader;
        this.tileCache = tileCache;
        this.jobDispatcher = jobDispatcher;
    }
    
    public void setTileLoaderListener(final TileLoaderListener listener) {
    	tileLoader.setTileLoaderListener(listener);
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
        if (tilex < 0 || tilex >= max || tiley < 0 || tiley >= max)
            return null;
        Tile tile = tileCache.getTile(tileSource, tilex, tiley, zoom);
        if (tile == null) {
            tile = new Tile(tileSource, tilex, tiley, zoom);
            tileCache.addTile(tile);
            tile.loadPlaceholderFromCache(tileCache);
        }
        if (!tile.isLoaded()) {
            jobDispatcher.addJob(tileLoader.createTileLoaderJob(tileSource, tilex, tiley, zoom));
        }
        return tile;
    }

    public TileCache getTileCache() {
        return tileCache;
    }

    public void setTileCache(TileCache tileCache) {
        this.tileCache = tileCache;
    }

    public TileLoader getTileLoader() {
        return tileLoader;
    }

    public void setTileLoader(TileLoader tileLoader) {
        this.tileLoader = tileLoader;
    }

    public TileSource getTileLayerSource() {
        return tileSource;
    }

    public TileSource getTileSource() {
        return tileSource;
    }

    public void setTileSource(TileSource tileSource) {
        this.tileSource = tileSource;
    }

    /**
     * 
     */
    public void cancelOutstandingJobs() {
        jobDispatcher.cancelOutstandingJobs();
    }
}
