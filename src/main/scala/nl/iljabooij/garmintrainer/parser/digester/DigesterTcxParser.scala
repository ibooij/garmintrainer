package nl.iljabooij.garmintrainer.parser.digester

import java.io.InputStream
import com.google.inject.{Inject,Provider}
import org.apache.commons.beanutils.ConvertUtils
import org.apache.commons.digester.Digester
import java.util.{List => JList, ArrayList => JAList}

import scala.collection.jcl.Conversions._

import nl.iljabooij.garmintrainer.model.Activity
import nl.iljabooij.garmintrainer.parser.ParseException

class DigesterTcxParser @Inject() (digesterProvider: Provider[Digester])
    extends TcxParser with LoggerHelper with NotNull {
  // initialization will be done by the DigesterTcxParser singleton.
  DigesterTcxParser.initialize
  
  private val ACTIVITY = "TrainingCenterDatabase/Activities/Activity"
  private val LAP = ACTIVITY + "/Lap"
  private val TRACK = LAP + "/Track"
  private val TRACK_POINT = TRACK + "/Trackpoint"

  override def parse(inputStream: InputStream): List[Activity] = {
    if (inputStream == null) throw new NullPointerException("inputStream should not be null")
    debug("Start parsing inputStream")
    
    val digester = setUpDigester
    
    val activityTypes = try {
      val activityTypes = new JAList[ActivityType]
      digester.push(activityTypes)
      digester.parse(inputStream)
      
      convertList(activityTypes).toList
    } catch {
      case e: Exception => throw new ParseException("Exception parsing TCX file", e)
    }
      
    activityTypes.map(_.build)
  }
  
  private def setUpDigester(): Digester = {
    val digester = digesterProvider.get()
    
    digester.addObjectCreate(ACTIVITY, classOf[ActivityType])
    digester.addSetNext(ACTIVITY, "add", "java.lang.Object")
    digester.addBeanPropertySetter(ACTIVITY + "/Id", "id")
    
    digester.addFactoryCreate(LAP, classOf[LapBuilderFactory])
    digester.addSetNext(LAP, "addLap")
    
    digester.addObjectCreate(TRACK, classOf[TrackType])
    digester.addSetNext(TRACK, "addTrack")
    
    digester.addObjectCreate(TRACK_POINT, classOf[TrackPointType])
    digester.addBeanPropertySetter(TRACK_POINT + "/Time", "time")
    digester.addBeanPropertySetter(TRACK_POINT + "/AltitudeMeters", "altitude")
    digester.addBeanPropertySetter(TRACK_POINT + "/Position/LatitudeDegrees", "latitude")
    digester.addBeanPropertySetter(TRACK_POINT + "/Position/LongitudeDegrees", "longitude")
    digester.addBeanPropertySetter(TRACK_POINT + "/HeartRateBpm/Value", "heartRate")
    digester.addBeanPropertySetter(TRACK_POINT + "/DistanceMeters", "distance")
    digester.addSetNext(TRACK_POINT, "addTrackPoint")
    
    return digester
  }
}

object DigesterTcxParser {
  import org.joda.time.DateTime
  import nl.iljabooij.garmintrainer.model.Length
  
  /**
   * initialize is evaluated only once. We'll just need to call initialize from
   * the DigesterTcxParser class every time the constructor is invoked and it will
   * run the initialize block only once.
   */
  private lazy val initialize = {
    ConvertUtils.register(new DateTimeConverter, classOf[DateTime])
    ConvertUtils.register(new LengthConverter, classOf[Length])
  }
}