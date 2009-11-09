package nl.iljabooij.garmintrainer

import org.slf4j.{Logger, LoggerFactory}

trait LoggerHelper {
  val loggerName = this.getClass.getName
  lazy val logger = LoggerFactory.getLogger(loggerName)

  def debug(s:String) = {
    if (logger.isDebugEnabled) logger.debug(s)
  }
}
