/*
 * Copyright 2009 Ilja Booij
 * 
 * This file is part of GarminTrainer.
 * 
 * GarminTrainer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GarminTrainer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GarminTrainer.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.iljabooij.garmintrainer.parser.digester

import org.joda.time.DateTime
import org.junit.Assert._
import org.mockito.Mockito._
import org.scalatest.junit.{JUnit3Suite,AssertionsForJUnit}
import org.scalatest.mock.MockitoSugar
import scala.collection.mutable.ListBuffer

/**
 * Test Case for {@link TrackType}.
 * @author ilja
 *
 */
class TrackTypeTest extends JUnit3Suite with AssertionsForJUnit with MockitoSugar {
  private var trackPointTypes: List[TrackPointType] = null
  private val NR_OF_TRACK_POINTS = 100
  private val START_OF_TRACK = new DateTime(2009, 9, 24, 9, 11, 0, 0)
	
  private var testTrackType: TrackType = null
	
  override def setUp {
    val tptBuffer = new ListBuffer[TrackPointType]
    for (i <- 0 until NR_OF_TRACK_POINTS) {
      val tpt = mock[TrackPointType]
      when(tpt.getTime).thenReturn(START_OF_TRACK.plusSeconds(i))
      tptBuffer += tpt
    }
	trackPointTypes = tptBuffer.toList
 
    testTrackType = new TrackType
    trackPointTypes.foreach(testTrackType.addTrackPoint(_))
  }
  
  def testAddAndGetTrackPoints {
    assertEquals(trackPointTypes, testTrackType.trackPointTypes)
  }
//
//	/**
//	 * Test if the start time is returned correctly.
//	 */
//	@Test
//	public void testGetStartTime() {
//		assertEquals("start time equals time of first track point",
//				trackPointTypes.getFirst().getTime(), testTrackType.getStartTime());
//	}
//	
//	/**
//	 * Test if the end time is returned correctly.
//	 */
//	@Test
//	public void testGetEndTime() {
//		assertEquals("end time equals time of last track point",
//				trackPointTypes.getLast().getTime(), testTrackType.getEndTime());
//	}
}
