package nl.iljabooij.garmintrainer.parser.digester

import com.google.inject.Provider
import java.io.InputStream
import java.util.{List=>JList,ArrayList}
import nl.iljabooij.garmintrainer.model.Activity
import org.apache.commons.digester.Digester
import org.scalatest.junit.{AssertionsForJUnit,JUnit3Suite}
import org.scalatest.mock.MockitoSugar
import org.junit.Assert._
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.hamcrest.core.IsInstanceOf
import scala.collection.jcl.Conversions._

class DigesterTcxParserTest extends JUnit3Suite with AssertionsForJUnit 
    with MockitoSugar {
  var parser: DigesterTcxParser = null
  var digester: Digester = null
  var provider: Provider[Digester] = null
  
  override def setUp {
    digester = mock[Digester]
    provider = mock[Provider[Digester]]
    when(provider.get).thenReturn(digester)
    parser = new DigesterTcxParser(provider)
  }
  
  def testParse {
    val inputStream = mock[InputStream]  
    val activityTypes = new ArrayList[ActivityType]
    var activities = List[Activity]() 
    for(i <- 1 to 2) {
      val activityTypeMock = mock[ActivityType]
      val activity = mock[Activity]
      when(activityTypeMock.build()).thenReturn(activity)
      activityTypes.add(activityTypeMock)
      activities = activity :: activities
    }
    activities = activities.reverse
    
    var returnedActivityTypes: JList[ActivityType] = null
    
	// when a list is pushed into the digester, register that list into
	// this Test class, so it can be filled later on. We need to do this
	// because of the way Digester works. An object (the list) is pushed
	// into the digester. This object is later referred to in the code
	// calling the digester, but is manipulated by the digester. To be able 
	// to do something with the object, we have to have a reference to it
	// here.
	doAnswer(new Answer[Object] {
		override def answer(invocation: InvocationOnMock): Object = {
		  val args = invocation.getArguments
          val activityTypes = args(0).asInstanceOf[JList[ActivityType]]
          returnedActivityTypes = activityTypes
          return null
		}
	}).when(digester).push(any(classOf[JList[ActivityType]]))
    
    // When the code calling the digester calls Digester.parse(inputStream)
    // we will fill the list that was previously set with some objects of
    // type ActivityType. These are actually mocks that will return
	// a mocked Activity when their build() method is called
    doAnswer(new Answer[Object] {
      override def answer(invocation: InvocationOnMock): Object = {
        returnedActivityTypes.addAll(activityTypes)
        return null
      }
    }).when(digester).parse(inputStream);
    
    assertEquals(activities, parser.parse(inputStream))
    // verify calls to the mocks we provided
    verify(provider, times(1)).get
    verify(digester, times(1)).push(returnedActivityTypes)
    verify(digester, times(1)).parse(inputStream)
    convertList(activityTypes).toList.foreach(
      at => verify(at, times(1)).build
    )
  }
  
  def testWithNullInputStream {
    intercept[NullPointerException] {
      parser.parse(null)
    }
    verify(digester, never).push(anyObject)
    verify(digester, never).parse(any(classOf[InputStream]))
  }
  
  def testWithDigesterThrowingException {
    when(digester.parse(any(classOf[InputStream]))).thenThrow(new RuntimeException)
    val inputStream = mock[InputStream]
    intercept[ParseException] {
      parser.parse(inputStream)
    }
  }
  
  
}
