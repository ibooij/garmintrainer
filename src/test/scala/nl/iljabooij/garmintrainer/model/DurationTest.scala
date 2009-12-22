package nl.iljabooij.garmintrainer.model

import org.junit.Assert._
import org.mockito.Mockito._
import org.joda.time.{DateTime=>JodaDateTime,Duration=>JodaDuration}
import org.scalatest.junit.{JUnit3Suite,AssertionsForJUnit}
import org.scalatest.mock.MockitoSugar

class DurationTest extends JUnit3Suite with AssertionsForJUnit with MockitoSugar {
  val DELTA = 0.00001
  def testPlus {
    val d1 = new Duration(100)
    val d2 = new Duration(200)
    
    assertEquals(300l, (d1 + d2).underlying.getMillis)
  }
  
  def testSeconds {
    assertEquals(0.3, new Duration(300).seconds, DELTA)
    assertEquals(33.3, new Duration(33300).seconds, DELTA)
  }
}
