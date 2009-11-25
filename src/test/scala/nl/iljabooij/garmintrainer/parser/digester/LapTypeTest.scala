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
package nl.iljabooij.garmintrainer.parser.digester;

import org.joda.time.{DateTime,Duration}
import org.junit.Assert._
import org.mockito.Mockito._
import org.scalatest.junit.{JUnit3Suite,AssertionsForJUnit}
import org.scalatest.mock.MockitoSugar
import scala.collection.mutable.ListBuffer

/**
 * Test cases for {@link LapType}.
 * @author ilja
 *
 */
class LapTypeTest extends JUnit3Suite with AssertionsForJUnit with MockitoSugar {
	private var testLapType: LapType = null
    private var tracks = List[TrackType]()
	
	private var START_TIME = new DateTime();
	private val START_INTERVALS = List(
		Duration.standardSeconds(1),
		Duration.standardSeconds(1),
		Duration.standardSeconds(1),
		Duration.standardSeconds(1),
		Duration.standardSeconds(1)
	)
	private val END_INTERVALS = List(
		Duration.standardSeconds(10),
		Duration.standardSeconds(120),
		Duration.standardSeconds(10),
		Duration.standardSeconds(120),
		Duration.standardSeconds(10)
	)
	
	private val NR_OF_TRACKS = 5
	
	/**
	 * set up tests
	 */
	override def setUp {
	  testLapType = new LapType
      testLapType.startTime = START_TIME
      val tracksBuffer = new ListBuffer[TrackType]
      
      var startTime = START_TIME
      var endTime = START_TIME
      for(i <- 0 until NR_OF_TRACKS) {
        val trackType = mock[TrackType]
        startTime = endTime.plus(START_INTERVALS(i))
        when(trackType.getStartTime()).thenReturn(startTime)
        endTime = startTime.plus(END_INTERVALS(i))
        when(trackType.getEndTime()).thenReturn(endTime)
		tracksBuffer += trackType
        testLapType.addTrack(trackType);
	  }
      tracks = tracksBuffer.toList
	}
	
	/**
	 * Test addition and getting of tracks
	 */
	def testAddAndGetTracks {
		assertEquals(tracks, testLapType.tracks);
	}
	
	/**
	 * Test getStartTime()
	 */ 
	def testGetStartTime {
		assertEquals(START_TIME, testLapType.startTime);
	}
//	
//	/**
//	 * Test getEndTime()
//	 */ 
	def testGetEndTime {
		assertEquals(tracks.last.getEndTime, testLapType.endTime);
	}
//	
}
