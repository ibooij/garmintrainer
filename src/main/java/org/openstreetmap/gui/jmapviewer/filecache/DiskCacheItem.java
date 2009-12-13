package org.openstreetmap.gui.jmapviewer.filecache;

import org.joda.time.DateTime;
import org.openstreetmap.gui.jmapviewer.Tile;

import com.google.inject.internal.Nullable;

public class DiskCacheItem {
	private final Tile tile;
	private final String etag;
	private final DateTime lastModified;
	
	DiskCacheItem(final Tile tile, @Nullable final String etag, @Nullable final DateTime lastModified) {
		this.tile = tile;
		this.etag = etag;
		this.lastModified = lastModified;
	}

	public Tile getTile() {
		return tile;
	}

	public String getEtag() {
		return etag;
	}

	public DateTime getLastModified() {
		return lastModified;
	}
	public boolean tileLoaded() {
		return (tile != null);
	}

}
