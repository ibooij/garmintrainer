package org.openstreetmap.gui.jmapviewer;

//License: GPL. Copyright 2008 by Jan Peter Stotz

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;

/**
 * Holds one map tile. Additionally the code for loading the tile image and
 * painting it is also included in this class.
 * 
 * @author Jan Peter Stotz
 */
public class Tile {

    private TileSource source;
    private int xtile;
    private int ytile;
    private int zoom;
    private BufferedImage image = null;
    public static final int SIZE = 256;

    /**
     * Creates a tile with empty image.
     * 
     * @param source
     * @param xtile
     * @param ytile
     * @param zoom
     */
    public Tile(TileSource source, int xtile, int ytile, int zoom) {
        super();
        this.source = source;
        this.xtile = xtile;
        this.ytile = ytile;
        this.zoom = zoom;
    }

    public TileSource getSource() {
        return source;
    }

    /**
     * @return tile number on the x axis of this tile
     */
    public int getXtile() {
        return xtile;
    }

    /**
     * @return tile number on the y axis of this tile
     */
    public int getYtile() {
        return ytile;
    }

    /**
     * @return zoom level of this tile
     */
    public int getZoom() {
        return zoom;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void loadImage(InputStream input) throws IOException {
        image = ImageIO.read(input);
    }

    public String getUrl() {
        return source.getTileUrl(zoom, xtile, ytile);
    }

    /**
     * Paints the tile-image on the {@link Graphics} <code>g</code> at the
     * position <code>x</code>/<code>y</code>.
     * 
     * @param g
     * @param x
     *            x-coordinate in <code>g</code>
     * @param y
     *            y-coordinate in <code>g</code>
     */
    public void paint(Graphics g, int x, int y) {
        if (image == null)
            return;
        g.drawImage(image, x, y, null);
    }

    @Override
    public String toString() {
    	return new ToStringBuilder(this)
    		.append("x", xtile)
    		.append("y", ytile)
    		.append("zoom", zoom)
    		.append("source", source)
    		.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tile))
            return false;
        Tile tile = (Tile) obj;
        return (xtile == tile.xtile) && (ytile == tile.ytile) && (zoom == tile.zoom) 
        	&& (source == tile.source);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
    	return new HashCodeBuilder(13, 23)
    		.append(source)
    		.append(xtile)
    		.append(ytile)
    		.append(zoom)
    		.toHashCode();
    }
}