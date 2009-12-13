package nl.iljabooij.garmintrainer.parser.digester

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.junit.Assert._
import org.mockito.Mockito._
import org.scalatest.junit.{JUnit3Suite,AssertionsForJUnit}
import org.scalatest.mock.MockitoSugar
import org.xml.sax.Attributes

class LapBuilderFactoryTest extends JUnit3Suite with AssertionsForJUnit 
    with MockitoSugar {
  def testCreateObject {
    val dateTime = new DateTime().withMillis(0)
    val formatter = ISODateTimeFormat.dateTimeNoMillis
    val dateTimeString = formatter.print(dateTime)
    val attributes = mock[Attributes]
    
    val attributeName = LapBuilderFactory.startTimeAttribute
    when(attributes.getValue(attributeName)).thenReturn(dateTimeString)
    
    val lapBuilderFactory = new LapBuilderFactory
    val lapType = lapBuilderFactory.createObject(attributes)
    
    assertEquals(dateTime, lapType.getStartTime)
    verify(attributes, times(1)).getValue(attributeName)
  }

}
