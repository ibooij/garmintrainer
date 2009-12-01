package nl.iljabooij.garmintrainer.importer

import java.io.{File,FileInputStream,FileNotFoundException}
import scala.collection.jcl.Conversions
import nl.iljabooij.garmintrainer.model.{Activity,ApplicationState}
import nl.iljabooij.garmintrainer.parser.{ParseException,TcxParser}
import com.google.inject.{Inject,Provider}

/**
 * Importer for Tcx files.
 * @param applicationState the Application's State
 * @param tcxParserProvider provider which generates TcxParser objects.
 */
class TcxImporterImpl @Inject() (applicationState: ApplicationState,
                                 tcxParserProvider: Provider[TcxParser]) 
    extends TcxImporter with LoggerHelper {
  
  /**
   * Import a file. Sends the contents of the tcxFile to a tcx parser. On 
   * succesful parsing, sets the parsed activity as the current activity
   * in the program.
   * @param tcxFile file holding activity.
   * @throws TcxImportException if anything goes wrong.
   */
  @throws(classOf[TcxImportException])
  override def importTcx(tcxFile: File) {
    val activities = try {
      tcxParserProvider.get.parse(new FileInputStream(tcxFile))
    } catch {
      case e: FileNotFoundException => throw new TcxImportException("file not found", e)
      case e: ParseException => throw new TcxImportException("exception while parsing", e)
    }
    
    if (!activities.isEmpty) {
      applicationState.currentActivity = activities(0)
    }
  }         
}
