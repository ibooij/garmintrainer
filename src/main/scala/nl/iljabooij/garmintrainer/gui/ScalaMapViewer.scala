package nl.iljabooij.garmintrainer.gui

import java.awt.{BasicStroke,Color,Graphics,Graphics2D}
import java.awt.geom.Path2D
import java.io.File
import scala.collection.jcl.Conversions._
import com.google.inject.Inject
import org.openstreetmap.gui.jmapviewer._

import nl.iljabooij.garmintrainer.model.{Activity,ApplicationState,TrackPoint}
import ApplicationState.Property

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
    var northWest = new Coordinate(-180.0, 90.0)
    var southEast = new Coordinate(180.0, -90.0)
    if (activity != null) {
      val coordinates = trackPoints.filter(tp => tp.hasPosition).map(tp => new Coordinate(tp.getLatitude, tp.getLongitude))
      
      val bounds:Array[Coordinate] = Coordinate.getBoundingBox(coordinates.toArray)
      northWest = bounds(0)
      southEast = bounds(1)
    }
    zoomToBoundingBox(northWest, southEast)
  }
  
  def trackPoints: List[TrackPoint] = {
    if (applicationState.getCurrentActivity == null)
      List[TrackPoint]()
    else {
      val julPoints = applicationState.getCurrentActivity.getTrackPoints
      convertList(julPoints).toList
    }
  }
  
  /** This method should be protected, but I cannot get it to compile if
   it is..
   */
  override def paintComponent(g: Graphics) {
    super.paintComponent(g)
    
    val activity = applicationState.getCurrentActivity
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
