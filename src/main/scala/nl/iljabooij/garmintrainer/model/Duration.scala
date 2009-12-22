package nl.iljabooij.garmintrainer.model

import org.joda.time.{Duration => JodaDuration}

object Duration {
  object second extends Duration(1000)
  object minute extends Duration(60 * 1000)
  object hour extends Duration(60 * 60 * 1000)
  
  implicit def *(s:Double,duration:Duration) = duration * s
}

class Duration(val underlying:JodaDuration) {
  def this() = this(new JodaDuration(0l))
  def this(millis:Long) = this(new JodaDuration(millis))
  
  def this(start:DateTime, end:DateTime) = this(
    new JodaDuration(start.underlying, end.underlying))
  
  def +(that:Duration) = new Duration(underlying.plus(that.underlying))
  def -(that:Duration) = new Duration(underlying.minus(that.underlying))
  def *(that:Double) = new Duration(Math.round(underlying.getMillis * that))
  def /(that:Double) = new Duration(Math.round(underlying.getMillis / that))
  
  def seconds = underlying.getMillis / 1000.0
  
  
  /** convert duration to a period */
  def toPeriod = new Period(underlying.toPeriod)
  
  override def equals(that:Any) = {
    if (that == null || !that.isInstanceOf[Duration]) false
    else underlying.equals(that.asInstanceOf[Duration].underlying)
  }
}
