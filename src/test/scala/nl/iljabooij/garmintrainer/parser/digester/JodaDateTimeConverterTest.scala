package nl.iljabooij.garmintrainer.parser.digester

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.scalatest.junit.{JUnit3Suite,AssertionsForJUnit}
import junit.framework.Assert._

class JodaDateTimeConverterTest extends JUnit3Suite {
  val TEST_STRING = "2009-02-21T12:57:25Z"
  var converter:JodaDateTimeConverter = null
  var dateTime: DateTime = null
  
  override def setUp {
    converter = new JodaDateTimeConverter
    val dateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis()
    dateTime = dateTimeFormatter.parseDateTime(TEST_STRING); 
  }
  
  def testHappy {
    assertEquals("correct datetime", dateTime, converter.convert(classOf[DateTime], TEST_STRING))
  }
  
  def testUnparsebleDate {
    val someString = "some string"
    intercept[IllegalArgumentException] {
      converter.convert(classOf[DateTime], someString)
    }
  }
  
  def testNullClass {
    intercept[NullPointerException] (converter.convert(null, TEST_STRING))
  }
  
  def testNullValue {
    intercept[NullPointerException] (converter.convert(classOf[DateTime], null))
  }
  
  def testWrongClass {
    intercept[IllegalArgumentException] (converter.convert(classOf[Integer], TEST_STRING))
  }
	
  def testWrongValueClass {
    intercept[IllegalArgumentException] (converter.convert(classOf[DateTime], Integer.valueOf(1)))
  }
}
