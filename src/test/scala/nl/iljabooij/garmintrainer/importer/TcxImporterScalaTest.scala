package nl.iljabooij.garmintrainer.importer

import java.io.{File,InputStream}
import scala.collection.jcl.Conversions._
import org.scalatest.junit.{AssertionsForJUnit,JUnit3Suite}
import org.scalatest.mock.MockitoSugar
import org.junit.{Before,Test}
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.hamcrest.core.IsInstanceOf
import com.google.inject.Provider

import nl.iljabooij.garmintrainer.model.{Activity,ApplicationState}
import nl.iljabooij.garmintrainer.parser.{TcxParser,ParseException}


class TcxImporterScalaTest extends JUnit3Suite with AssertionsForJUnit with MockitoSugar{
  var tcxImporter: TcxImporter = null
  var applicationState: ApplicationState = null
  var tcxParser: TcxParser = null
  var tcxParserProvider: Provider[TcxParser] = null
  
  override def setUp() {
    applicationState = mock[ApplicationState]
    tcxParser = mock[TcxParser]
    
    tcxParserProvider = mock[Provider[TcxParser]]
	when(tcxParserProvider.get()).thenReturn(tcxParser);
 
	tcxImporter = new TcxImporterImpl(applicationState, tcxParserProvider)
  }

  def testFileDoesNotExist() {
    intercept[TcxImportException] {
      tcxImporter.importTcx(new File("/ThisFileDoesNotExist"))
    }
  }
  
  def testImportActivity() {
     val uri = getClass().getResource("/sample.tcx").toURI()
     val file = new File(uri)
     
     val activities = List(mock[Activity])
     
     when(tcxParser.parse(any(classOf[InputStream]))).thenReturn(activities)
     
     tcxImporter.importTcx(file)
     
     verify(applicationState, times(1)).setCurrentActivity(activities.first)
     verify(tcxParser, times(1)).parse(any(classOf[InputStream]))
   }
   
   	/**
	 * Test what happens when the parser throws a ParseException
	 */
   def testWithParserThrowingException() {
     val uri = getClass().getResource("/sample.tcx").toURI()
     val file = new File(uri);
     val activities = List(mock[Activity])
     
     when(tcxParser.parse(any(classOf[InputStream]))).thenThrow(
       new ParseException("test exception"))
      
     intercept[TcxImportException] {
	   tcxImporter.importTcx(file);
     }
   }
}
