package nl.iljabooij.garmintrainer.model

import org.junit.Assert._
import org.mockito.Mockito._
import org.joda.time.{DateTime=>JodaDateTime,Duration=>JodaDuration}
import org.scalatest.junit.{JUnit3Suite,AssertionsForJUnit}
import org.scalatest.mock.MockitoSugar

class DateTimeTest extends JUnit3Suite with AssertionsForJUnit with MockitoSugar {
  def testTo {
    val start = new DateTime
    val stop = new DateTime(start.underlying plusMinutes 5)
    
    val duration = new Duration(JodaDuration.standardMinutes(5))
    assertEquals(duration, start to stop)
  }
  
  def testPlus {
    val start = new DateTime
    val stop = new DateTime(start.underlying plusMinutes 5)
    val duration = new Duration(JodaDuration.standardMinutes(5))
    
    assertEquals(stop, start + duration)
	  
  }
  
  def testToString {
    val dt = new DateTime
    val format = "yyyy-MM-dd"
    assertEquals(dt.underlying.toString(format), dt.toString(format))
  }
}
