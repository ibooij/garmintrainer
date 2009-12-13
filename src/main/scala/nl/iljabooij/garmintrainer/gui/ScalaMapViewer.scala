package nl.iljabooij.garmintrainer.gui

import java.awt.{BasicStroke,Color,Graphics,Graphics2D,Point}
import java.awt.geom.Path2D
import java.io.File
import scala.collection.jcl.Conversions._
import com.google.inject.Inject
import org.openstreetmap.gui.jmapviewer._

import nl.iljabooij.garmintrainer.model.{Activity,ApplicationState,Property,TrackPoint}

class ScalaMapViewer @Inject() (applicationState: ApplicationState)
    extends JMapViewer with SwingHelper {
  private val CACHE_DIR:File = new File(new File(System
			.getProperty("user.home"), ".garminTrainer"), "tileCache");
  setName("Map")
  setTileSource(new OsmTileSource.Mapnik)
  setTileLoader(new OsmFileCacheTileLoader(this, CACHE_DIR))
  
  ApplicationStateAdapter.addPropertyChangeListener(applicationState,
                                                    Property.CurrentActivity,
                                                    activityChanger)
      
  private def activityChanger(value: Any) {
    val activity = value.asInstanceOf[Option[Activity]]
   
    onEdt {
      zoomToActivity(activity)
      repaint()
    }
  }
  
  private def boundingBox(coordinates:List[Coordinate]) = {
    var north = coordinates.map(_.getLat).sort(_ > _).head
    var south = coordinates.map(_.getLat).sort(_>_).last
    var west = coordinates.map(_.getLon).sort(_>_).last
    var east = coordinates.map(_.getLon).sort(_>_).head
    
    List(new Coordinate(north, west), new Coordinate(south,east))
  }
  
  private def centerBetween(northWest: Coordinate, southEast: Coordinate) = {
    val lat = (northWest.getLat + southEast.getLat) / 2.0
    val lon = (northWest.getLon + southEast.getLon) / 2.0
	  
    new Coordinate(lat,lon)
  }
  
  private def zoomToActivity(activity: Option[Activity]) {
    /** perform these computations in a background thread */
    def doInBackground = {
      var northWest = new Coordinate(-180.0, 90.0)
      var southEast = new Coordinate(180.0, -90.0)
      if (activity.isDefined) {
        val coordinates = activity.get.trackPoints.filter(tp => tp.hasPosition).map(tp => new Coordinate(tp.latitude, tp.longitude))
        
        val bounds = boundingBox(coordinates)
        northWest = bounds(0)
        southEast = bounds(1)
      }
      Pair(northWest, southEast)
    }
    /** zooming to bounding box has to happen on the EDT */
    def onEdt(bounds: Pair[Coordinate,Coordinate]) {
      val (northWest,southEast) = bounds
      zoomToBoundingBox(northWest, southEast)  
    }
    
    inSwingWorker(doInBackground, onEdt)
  }
  
  private def zoomToBoundingBox(northWest:Coordinate, southEast:Coordinate) {
    val mapZoomMax = tileController.getTileSource.getMaxZoom
    val nwPoint = new Point(OsmMercator.LonToX(northWest.getLon, mapZoomMax),
                            OsmMercator.LatToY(northWest.getLat, mapZoomMax))
    val sePoint = new Point(OsmMercator.LonToX(southEast.getLon, mapZoomMax),
                            OsmMercator.LatToY(southEast.getLat, mapZoomMax))
    var width = sePoint.x - nwPoint.x
    var height = sePoint.y - nwPoint.y
    
    var zoomLevel = mapZoomMax
    while(width > getWidth || height > getHeight) {
      zoomLevel -= 1
      width >>= 1
      height >>= 1
    }
      
    setZoom(zoomLevel)
    val centerCoordinate = centerBetween(northWest, southEast)
    center = new Point(OsmMercator.LonToX(centerCoordinate.getLon, zoomLevel),
                       OsmMercator.LatToY(centerCoordinate.getLat, zoomLevel))
    repaint()
  }
  
  /** This method should be protected, but I cannot get it to compile if
   it is..
   */
  override def paintComponent(g: Graphics) {
    super.paintComponent(g)
    
    if (applicationState.currentActivity.isEmpty) return
    val activity = applicationState.currentActivity.get
    val mapPoints = activity.trackPoints.filter(tp => tp.hasPosition).map(tp => getMapPosition(tp.latitude, tp.longitude, false))
    
    val path = new Path2D.Double
    path.moveTo(mapPoints.head.x, mapPoints.head.y)
    mapPoints.tail.foreach {mapPoint => path.lineTo(mapPoint.x, mapPoint.y)}
    val g2d = g.asInstanceOf[Graphics2D]
    g2d.setColor(new Color(1.0f, 0, 0, 0.5f))
	g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))
	g2d.draw(path)
  }
}
