package nl.iljabooij.garmintrainer.model

import org.joda.time.{Period=>JodaPeriod}

/** Wrapper class around org.joda.time.Period */
class Period(underlying: JodaPeriod) {
  def hours = underlying.getHours
  def minutes = underlying.getMinutes
  def seconds = underlying.getSeconds
}
