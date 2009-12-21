package nl.iljabooij.garmintrainer.model

/**
 TrackPoint as it is used throughout the application. It adds some
 methods to the MeasuredTrackPoint, making it a little easier to use. 
 */
trait TrackPoint extends MeasuredTrackPoint{
  /* check whether or not the track point has a measured position. */
  def hasPosition:Boolean
  
  /**
   * Get the speed from the last track point to this one.
   * @return the Speed.
   */
  def speed:Speed
  
  /**
   * Get altitude gained or lost in this track point compared to the last
   * @return the altitude delta
   */
  def altitudeDelta:Length
}