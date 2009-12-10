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
package nl.iljabooij.garmintrainer.model;

import org.junit.Assert._
import org.mockito.Mockito._
import org.joda.time.{DateTime,Duration}
import org.scalatest.junit.{JUnit3Suite,AssertionsForJUnit}
import org.scalatest.mock.MockitoSugar
import scala.collection.mutable.ListBuffer

class ActivityImplTest extends JUnit3Suite with AssertionsForJUnit with MockitoSugar {
  private var activity:ActivityImpl = _
  private var laps:List[Lap] = _
  private var allTrackPoints: List[TrackPoint] = _
  private val START_TIME = new DateTime(2009, 9, 17, 14, 45, 36, 0)
  
  private val NR_OF_LAPS = 4
  private val SECONDS_PER_LAP = 100;
  private val distances = List(100.0, 200.0, 300.0, 400.0)
  private val SECONDS_PER_POINT = SECONDS_PER_LAP / distances.size

	override def setUp() {
	  val lapsBuffer = new ListBuffer[Lap]
	  val tpBuffer = new ListBuffer[TrackPoint]	
	  for (lapNr <- 0 until NR_OF_LAPS) {
	    val lap = mock[Lap]
		val lapStartDistance = Length.createLengthInMeters(distances(distances.length - 1) * lapNr)
  		val lapStartTime = START_TIME.plusSeconds(SECONDS_PER_LAP * lapNr)
			
  		when(lap.startTime).thenReturn(lapStartTime);
        val lapTrackPoints = new ListBuffer[TrackPoint]
        for(tpNr <- 0 until distances.size) {
          val trackPoint = mock[TrackPoint]
          val tpDistance = Length.createLengthInMeters(distances(tpNr))
          when(trackPoint.distance).thenReturn(lapStartDistance.plus(tpDistance))
          when(trackPoint.time).thenReturn(lapStartTime.plusSeconds(tpNr * SECONDS_PER_POINT))
          
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
      val netDurationPerLap = Duration.standardMinutes(10)
      // 11 minutes gross riding time per lap
      val grossDurationPerLap = Duration.standardMinutes(11)
      
      // first lap starts 1 second after activity starts!
	  var lapStartTime = startTime.plus(Duration.standardSeconds(1))
	  // 10 laps
      for(i <- 0 until 10) {
        if (i > 0) {
          lapStartTime = lapStartTime.plus(grossDurationPerLap)
        }
        val lap = mock[Lap]
        when(lap.startTime).thenReturn(lapStartTime)
        when(lap.netDuration).thenReturn(netDurationPerLap)
        
        // two track points per lap
		val tp1 = mock[TrackPoint]
        when(tp1.time).thenReturn(lapStartTime)
        val tp2 = mock[TrackPoint]
        when(tp2.time).thenReturn(lapStartTime.plus(grossDurationPerLap))
        when(lap.trackPoints).thenReturn(List(tp1, tp2))

        laps += lap
      }
      
      val activity = new ActivityImpl(startTime, laps.toList)

      // we have 10 laps of 10 minutes riding time per lap, so there should be
	  // 100 minutes of riding time, plus the first second (difference between
	  // Activity start time and the start of the first lap
      assertEquals(Duration.standardMinutes(100).plus(
				Duration.standardSeconds(1)), activity.netDuration)

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
				START_TIME.plusMinutes(1), laps)))
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
		val distance = Length.createLengthInMeters(NR_OF_LAPS
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
		val gain = Length.createLengthInMeters(3.0)
		val smallGain = Length.createLengthInMeters(0.5)
		val loss = Length.createLengthInMeters(-3.0)
		val smallLoss = Length.createLengthInMeters(-0.5)

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
		assertEquals(Length.createLengthInMeters(30.0), activity
				.altitudeGain)
	}

	/**
	 * Test which checks if altitude gain correctly filters out any noise.
	 */
	def testFilterAltitudeDeltaNoise() {
		// values go up and down every trackpoint.
		val gain = Length.createLengthInMeters(4.0)
		val loss = Length.createLengthInMeters(-4.0)

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

		assertEquals(Length.createLengthInMeters(0.0), activity.altitudeGain)
	}

	def testGetMaxAndMinAltitude() {
	  val altitudes = List(1.0, 2.0, 3.0, 2.0)
      
      def mockTrackPointForAltitude(altitude:Double):TrackPoint = {
        val tp = mock[TrackPoint]
        when(tp.altitude).thenReturn(Length.createLengthInMeters(altitude))
        return tp
      }
      val lap = mock[Lap]
      val tps = altitudes.map(mockTrackPointForAltitude(_)).toList
      when(lap.trackPoints).thenReturn(tps)
      val activity = new ActivityImpl(START_TIME, List(lap))
      assertEquals(Length.createLengthInMeters(3.0), activity.maximumAltitude)
	  assertEquals(Length.createLengthInMeters(1.0), activity.minimumAltitude)
	}

//	@Test
//	public void testGetAltitudeClass() {
//		final Length _1meter = Length.createLengthInMeters(1.0);
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
        when(tp.speed).thenReturn(Speed.createExactSpeedInMetersPerSecond(speed))
        tp
      }
      val lap = mock[Lap]
      val tps = speeds.map(tpForSpeed(_))
      when(lap.trackPoints).thenReturn(tps)
      
      val activity = new ActivityImpl(START_TIME, List(lap))

      assertEquals(Speed.createExactSpeedInMetersPerSecond(3.0), activity
				.maximumSpeed)
	}

//	/**
//	 * Test the compareTo method. It should compare by start time
//	 */
//	@Test
//	public void compareTo() {
//		Activity sameActivity = new ActivityImpl(activity.getStartTime(),
//				activity.getLaps());
//		assertEquals(0, activity.compareTo(sameActivity));
//		assertEquals(0, sameActivity.compareTo(activity));
//
//		Activity laterActivity = new ActivityImpl(activity.getStartTime()
//				.plusSeconds(1), activity.getLaps());
//		assertTrue(activity.compareTo(laterActivity) < 0);
//		assertTrue(laterActivity.compareTo(activity) > 0);
//
//		Activity earlierActivity = new ActivityImpl(activity.getStartTime()
//				.minusSeconds(1), activity.getLaps());
//		assertTrue(activity.compareTo(earlierActivity) > 0);
//		assertTrue(earlierActivity.compareTo(activity) < 0);
//	}
//
//	@Test(expected = NullPointerException.class)
//	public void compareToShouldThrowNPE() {
//		activity.compareTo(null);
//	}
//
//	/**
//	 * Some simple tests to check if class is really immutable. We have no way
//	 * of actually really knowing of course, but we must at least test if the
//	 * class doesn't have setters, and if return values are immutable as well.
//	 */
//	@SuppressWarnings("unchecked")
//	@Test
//	public void testIsImmutable() {
//		assertTrue(ActivityImpl.class.isAnnotationPresent(Immutable.class));
//
//		for (Method method : ActivityImpl.class.getMethods()) {
//			assertFalse("setters are not allowed on Immutable classes", method
//					.getName().startsWith("set"));
//
//			// check if returned values are immutable
//			Class returnType = method.getReturnType();
//
//			final Class[] allowedClasses = new Class[] { String.class,
//					DateTime.class, Duration.class, ImmutableList.class,
//					Class.class };
//
//			if (returnType.isPrimitive()) {
//				continue;
//			}
//			if (returnType.isAnnotationPresent(Immutable.class)) {
//				continue;
//			}
//			/*
//			 * if the return type is an interface, we don't know for sure if the
//			 * returned value is an immutable object.
//			 */
//			if (returnType.isInterface()) {
//				continue;
//			}
//
//			if (returnType.isEnum()) {
//				continue;
//			}
//			
//			boolean isKnownImmutableClass = false;
//			for (Class clazz : allowedClasses) {
//				isKnownImmutableClass = isKnownImmutableClass
//						|| (clazz == returnType);
//			}
//			if (isKnownImmutableClass) {
//				continue;
//			}
//
//			fail("return types should be immutable: " + returnType);
//		}
//	}
//
//	@Test
//	public void testGetTrackPointForTimeFirst() {
//		TrackPoint returnedTrackPoint = activity.getTrackPointForTime(allTrackPoints.getFirst().getTime());
//		assertEquals(allTrackPoints.getFirst(), returnedTrackPoint);
//	}
//	
//	@Test
//	public void testGetTrackPointForTimeLast() {
//		TrackPoint returnedTrackPoint = activity.getTrackPointForTime(allTrackPoints.getLast().getTime());
//		assertEquals(allTrackPoints.getLast(), returnedTrackPoint);
//	}
//	
//	@Test
//	public void testGetTrackPointForTime() {
//		// check the exact times
//		for (TrackPoint trackPoint : allTrackPoints.subList(1, allTrackPoints.size() - 1)) {
//			TrackPoint returnedTrackPoint = activity.getTrackPointForTime(trackPoint.getTime());
//			assertTrue(returnedTrackPoint instanceof InterpolatedTrackPoint);
//			assertEquals(trackPoint.getTime(), returnedTrackPoint.getTime());
//			
//			DateTime nextTime = trackPoint.getTime().plus(Duration.standardSeconds(3));
//			TrackPoint next = activity.getTrackPointForTime(nextTime);
//			assertTrue(next instanceof InterpolatedTrackPoint);
//			InterpolatedTrackPoint interpolatedTrackPoint =
//				(InterpolatedTrackPoint) next;
//			assertEquals(nextTime, interpolatedTrackPoint.getTime());
//			assertEquals(trackPoint, interpolatedTrackPoint.getBegin());
//		}
//	}
//	
//	@Test
//	public void testGetTrackPointForTimeWithTimeBeforeActivity() {
//		// too early a time should just return the first track point.
//		assertEquals(allTrackPoints.getFirst(), activity
//				.getTrackPointForTime(START_TIME.minusHours(1)));
//
//	}
//	
//	@Test
//	public void testGetTrackPointForTimeWithTimeAfterActivity() {
//		// too early a time should just return the first track point.
//		assertEquals(allTrackPoints.getLast(), activity
//				.getTrackPointForTime(activity.getEndTime().plusHours(1)));
//
//	}
}
