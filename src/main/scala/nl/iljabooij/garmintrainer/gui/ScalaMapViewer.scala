package nl.iljabooij.garmintrainer.gui

import java.awt.{BasicStroke,Color,Graphics,Graphics2D}
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
    val activity = value.asInstanceOf[Activity]
   
    onEdt {
      zoomToActivity(activity)
      repaint()
    }
  }
  
  private def zoomToActivity(activity: Activity) {
    /** perform these computations in a background thread */
    def doInBackground = {
      var northWest = new Coordinate(-180.0, 90.0)
      var southEast = new Coordinate(180.0, -90.0)
      if (activity != null) {
        val coordinates = trackPoints.filter(tp => tp.hasPosition).map(tp => new Coordinate(tp.getLatitude, tp.getLongitude))
        val bounds:Array[Coordinate] = Coordinate.getBoundingBox(coordinates.toArray)
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
  
  def trackPoints: List[TrackPoint] = {
    val activity = applicationState.currentActivity
    if (activity == null) List[TrackPoint]() else activity.trackPoints
  }
  
  /** This method should be protected, but I cannot get it to compile if
   it is..
   */
  override def paintComponent(g: Graphics) {
    super.paintComponent(g)
    
    val activity = applicationState.currentActivity
    if (activity == null) return
    
    val mapPoints = trackPoints.filter(tp => tp.hasPosition).map(tp => getMapPosition(tp.getLatitude, tp.getLongitude, false))
    
    val path = new Path2D.Double
    path.moveTo(mapPoints.head.x, mapPoints.head.y)
    mapPoints.tail.foreach {mapPoint => path.lineTo(mapPoint.x, mapPoint.y)}
    val g2d = g.asInstanceOf[Graphics2D]
    g2d.setColor(new Color(1.0f, 0, 0, 0.5f))
	g2d.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))
	g2d.draw(path)
  }
}
