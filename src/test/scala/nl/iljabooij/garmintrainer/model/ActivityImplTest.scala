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
package nl.iljabooij.garmintrainer.model

import nl.iljabooij.garmintrainer.model.Duration._
import nl.iljabooij.garmintrainer.model.Length.{Meter}
import nl.iljabooij.garmintrainer.model.Speed.MetersPerSecond

import org.junit.Assert._
import org.mockito.Mockito._
import org.scalatest.junit.{JUnit3Suite,AssertionsForJUnit}
import org.scalatest.mock.MockitoSugar
import scala.collection.mutable.ListBuffer

class ActivityImplTest extends JUnit3Suite with AssertionsForJUnit with MockitoSugar {
  private var activity:ActivityImpl = _
  private var laps:List[Lap] = _
  private var allTrackPoints: List[TrackPoint] = _
  private val START_TIME = new DateTime
  
  private val NR_OF_LAPS = 4
  private val SECONDS_PER_LAP = second * 100;
  private val distances = List(100.0, 200.0, 300.0, 400.0)
  private val SECONDS_PER_POINT = SECONDS_PER_LAP / distances.size

	override def setUp() {
	  val lapsBuffer = new ListBuffer[Lap]
	  val tpBuffer = new ListBuffer[TrackPoint]	
	  for (lapNr <- 0 until NR_OF_LAPS) {
	    val lap = mock[Lap]
		val lapStartDistance = new Meter(distances(distances.length - 1) * lapNr)
  		val lapStartTime = START_TIME + (SECONDS_PER_LAP * lapNr)
			
  		when(lap.startTime).thenReturn(lapStartTime);
        val lapTrackPoints = new ListBuffer[TrackPoint]
        for(tpNr <- 0 until distances.size) {
          val trackPoint = mock[TrackPoint]
          val tpDistance = new Meter(distances(tpNr))
          when(trackPoint.distance).thenReturn(lapStartDistance + tpDistance)
          when(trackPoint.time).thenReturn(
            lapStartTime + (SECONDS_PER_POINT * tpNr))
          
          lapTrackPoints += trackPoint
        }
		when(lap.trackPoints).thenReturn(lapTrackPoints.toList)
        tpBuffer ++= lapTrackPoints.toList
        lapsBuffer += lap
      }
      allTrackPoints = tpBuffer.toList
      laps = lapsBuffer.toList
	  activity = new ActivityImpl(START_TIME, lapsBuffer.toList)
	}

	def testGrossDuration {
		val grossDuration = new Duration(START_TIME, allTrackPoints.last.time)
		assertEquals(grossDuration, activity.grossDuration)
	}

	/**
	 * Test if the net duration (riding time, total time minus pauses) of an
	 * Activity is calculated correctly.
	 */
	def testnetDuration() {
	  /** construct some laps that have some track points */
	  val laps = new ListBuffer[Lap]
      val startTime = new DateTime()
      // 10 minutes net riding time per lap
      val netDurationPerLap = minute * 10 
      // 11 minutes gross riding time per lap
      val grossDurationPerLap = minute * 11
      
      // first lap starts 1 second after activity starts!
	  var lapStartTime = startTime + second * 1
	  // 10 laps
      for(i <- 0 until 10) {
        if (i > 0) {
          lapStartTime = lapStartTime + grossDurationPerLap
        }
        val lap = mock[Lap]
        when(lap.startTime).thenReturn(lapStartTime)
        when(lap.netDuration).thenReturn(netDurationPerLap)
        
        // two track points per lap
		val tp1 = mock[TrackPoint]
        when(tp1.time).thenReturn(lapStartTime)
        val tp2 = mock[TrackPoint]
        when(tp2.time).thenReturn(lapStartTime + grossDurationPerLap)
        when(lap.trackPoints).thenReturn(List(tp1, tp2))

        laps += lap
      }
      
      val activity = new ActivityImpl(startTime, laps.toList)

      // we have 10 laps of 10 minutes riding time per lap, so there should be
	  // 100 minutes of riding time, plus the first second (difference between
	  // Activity start time and the start of the first lap
      assertEquals((minute * 100) + second, activity.netDuration)
   }
	
   /**
	* We'll just test if toString() returns anything..
	*/
   def testToString() {
     assertFalse(activity.toString().isEmpty());
   }

	/**
	 * Simple tests for equals
	 */
	def testEqualsAndHashCode() {
		assertFalse("not equal to null", activity.equals(null))
		assertFalse("not equal to unrelated Object", activity
				.equals(new Object()))
		assertTrue("equal to itself", activity.equals(activity))
		assertEquals(activity.hashCode(), activity.hashCode())

		/* same content, different object */
		val sameActivity = new ActivityImpl(START_TIME, laps)
		assertTrue("equal to same object", activity.equals(new ActivityImpl(
				START_TIME, laps)))
		assertEquals(activity.hashCode(), sameActivity.hashCode())

		/* differently filled objects, should all differ */
		assertFalse("different date time", activity.equals(new ActivityImpl(
				START_TIME + minute, laps)))
		val oneLess = laps.slice(0, 2)
		assertFalse("different laps", activity.equals(new ActivityImpl(
				START_TIME, oneLess)))
	}

	/**
	 * Test if hashCode() works according to the rules.
	 */
	def testHashCodeRepeatsResult() {
		// check that hashCode returns the same result over and over
		val hash = activity.hashCode;
		for (i <- 0 until 20) {
			assertEquals(hash, activity.hashCode);
		}
	}

	/**
	 * 
	 */
	def testGetTrackPoints() {
	  assertEquals(allTrackPoints, activity.trackPoints);
	}

	def testGetStartTime() {
		assertEquals(START_TIME, activity.startTime);
	}

	def testGetEndTime() {
	  val endTime = allTrackPoints.last.time
      assertEquals(endTime, activity.endTime);
	}

	def testGetDistance() {
		val distance = new Meter(NR_OF_LAPS
				* distances(distances.length - 1))
		assertEquals("lengths equal", distance, activity.distance)
	}

	def testGetLaps() {
		assertEquals(laps, activity.laps)
	}

	def testNullStartTimeShouldCauseNPE() {
	  intercept[NullPointerException] {
		new ActivityImpl(null, laps)
      }
	}

	def testNullLapsShouldCauseNPE {
	  intercept[NullPointerException] {
		new ActivityImpl(START_TIME, null)
      }
	}

	def testEmptyLapsShouldCouseIAE() {
	  intercept[IllegalArgumentException] {
		new ActivityImpl(START_TIME, List[Lap]())
      }
	}

	def testGetAltitudeGain() {
		val gain = new Meter(3.0)
		val smallGain = new Meter(0.5)
		val loss = new Meter(-3.0)
		val smallLoss = new Meter(-0.5)

		val laps = new ListBuffer[Lap]
		for (i <- 0 until 2) {
			laps += mock[Lap]

			val trackPoints = new ListBuffer[TrackPoint]
			for (j <- 0 until 20) {
			  trackPoints += mock[TrackPoint]
              if (j < 5) {
                when(trackPoints(j).altitudeDelta).thenReturn(gain)
              } else if (j < 10){
                when(trackPoints(j).altitudeDelta).thenReturn(loss)
              } else if (j < 15 ){
                when(trackPoints(j).altitudeDelta).thenReturn(smallGain)
              } else {
                when(trackPoints(j).altitudeDelta).thenReturn(smallLoss)
              }
			}
			when(laps(i).trackPoints).thenReturn(trackPoints.toList)
		}

		val activity = new ActivityImpl(START_TIME, laps.toList)

		// test gain. There are two laps, both with a climb of 15m, which is significant
		// and a small climb of 2.5m which will be ignored.
		assertEquals(new Meter(30.0), activity
				.altitudeGain)
	}

	/**
	 * Test which checks if altitude gain correctly filters out any noise.
	 */
	def testFilterAltitudeDeltaNoise() {
		// values go up and down every trackpoint.
		val gain = new Meter(4.0)
		val loss = new Meter(-4.0)

		val laps = new ListBuffer[Lap]
		for (i <- 0 until 1) {
			val lap = mock[Lap]
            laps += lap
			val trackPoints = new ListBuffer[TrackPoint]
			for (j <- 0 until 10) {
				val trackPoint = mock[TrackPoint]
                trackPoints += trackPoint                 
				val delta = if (j % 2 == 0) gain else loss
				when(trackPoint.altitudeDelta).thenReturn(delta)
			}
			
			when(lap.trackPoints).thenReturn(trackPoints.toList)
		}

		val activity = new ActivityImpl(START_TIME, laps.toList)

		assertEquals(new Meter(0.0), activity.altitudeGain)
	}

	def testGetMaxAndMinAltitude() {
	  val altitudes = List(1.0, 2.0, 3.0, 2.0, 1.0, 0.5, -0.5)
      
      def mockTrackPointForAltitude(altitude:Double):TrackPoint = {
        val tp = mock[TrackPoint]
        when(tp.altitude).thenReturn(new Meter(altitude))
        return tp
      }
      val lap = mock[Lap]
      val tps = altitudes.map(mockTrackPointForAltitude(_)).toList
      when(lap.trackPoints).thenReturn(tps)
      val activity = new ActivityImpl(START_TIME, List(lap))
      assertEquals(new Meter(3.0), activity.maximumAltitude)
	  assertEquals(new Meter(-0.5), activity.minimumAltitude)
	}
 
	/** I found a bug where getting the max altitude when
        there were only negative altitudes resulted in 
        a max altitude of Length.ZERO (0 meters)*/
    def testGetMaxAltitudeWithNegativeAltitudes {
      val altitudes = List(-1.0, -2.0, -3.0, -2.0, -1.0, -0.5, -0.5)
      
      def mockTrackPointForAltitude(altitude:Double):TrackPoint = {
        val tp = mock[TrackPoint]
        when(tp.altitude).thenReturn(new Meter(altitude))
        return tp
      }
      val lap = mock[Lap]
      val tps = altitudes.map(mockTrackPointForAltitude(_)).toList
      when(lap.trackPoints).thenReturn(tps)
      val activity = new ActivityImpl(START_TIME, List(lap))
	  assertEquals(new Meter(-0.5), activity.maximumAltitude)
	}

//	@Test
//	public void testGetAltitudeClass() {
//		final Length _1meter = new Meter(1.0);
//		for (AltitudeClass altitudeClass : AltitudeClass.values()) {
//			Lap lap = mock(Lap.class);
//			TrackPoint[] trackPoints = new TrackPoint[10];
//			for (int i = 0; i < 10; i++) {
//				TrackPoint trackPoint = mock(TrackPoint.class);
//				when(trackPoint.getAltitude()).thenReturn(
//						altitudeClass.getHigh().minus(_1meter));
//				trackPoints[i] = trackPoint;
//			}
//			when(lap.getTrackPoints()).thenReturn(
//					ImmutableList.copyOf(Arrays.asList(trackPoints)));
//			
//			Lap[] laps = new Lap[] {lap};
//			Activity activity = new ActivityImpl(START_TIME, Arrays.asList(laps));
//			
//			assertEquals(altitudeClass, activity.getAltitudeClass());
//		}
//	}

	/**
	 * test the getMaximumSpeed() method
	 */
	def testGetMaximumSpeed() {
	  val speeds = List(1.0, 2.0, 3.0, 2.0)
	  def tpForSpeed(speed:Double) = {
	    val tp = mock[TrackPoint]
        when(tp.speed).thenReturn(new MetersPerSecond(speed))
        tp
      }
      val lap = mock[Lap]
      val tps = speeds.map(tpForSpeed(_))
      when(lap.trackPoints).thenReturn(tps)
      
      val activity = new ActivityImpl(START_TIME, List(lap))

      assertEquals(new MetersPerSecond(3.0), activity
				.maximumSpeed)
	}
}
