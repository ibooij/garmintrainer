package nl.iljabooij.garmintrainer

import org.slf4j.{Logger, LoggerFactory}

trait LoggerHelper {
  val loggerName = this.getClass.getName
  lazy val logger = LoggerFactory.getLogger(loggerName)

  def debug(s:String) = {
    if (logger.isDebugEnabled) logger.debug(s)
  }
  
  def error(s:String) = {
    if (logger.isErrorEnabled) logger.error(s)
  }
  
  def error(s:String, o: Any) = {
    if (logger.isErrorEnabled) logger.error(s, o)
  }
}
