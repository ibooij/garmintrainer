package nl.iljabooij.garmintrainer.model

import org.joda.time.{DateTime=>JodaDateTime,
                      DateTimeZone,Duration=>JodaDuration}
import org.joda.time.format.ISODateTimeFormat
object DateTime {
  implicit def fromJodaDateTime(jdt:JodaDateTime) = new DateTime(jdt)
  def fromIsoNoMillis(s:String) = new DateTime(ISODateTimeFormat.dateTimeNoMillis.parseDateTime(s))
  
  val EPOCH = new DateTime(new JodaDateTime(0L, DateTimeZone.UTC))
}

class DateTime(val underlying: JodaDateTime) {
  def this() = this(new JodaDateTime)
  
  def to(that: DateTime) = new Duration(new JodaDuration(underlying, that.underlying))
  
  def +(that:Duration) = new DateTime(underlying.plus(that.underlying))
  def -(that:Duration) = new DateTime(underlying.minus(that.underlying))
  
  def toString(format:String) = underlying.toString(format)
  
  override def equals(that:Any) = {
    if (that == null || !that.isInstanceOf[DateTime]) false
    else underlying.equals(that.asInstanceOf[DateTime].underlying)
  }
}
