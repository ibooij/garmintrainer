package nl.iljabooij.garmintrainer.parser.digester

import nl.iljabooij.garmintrainer.Preconditions._
import nl.iljabooij.garmintrainer.model.DateTime
import nl.iljabooij.garmintrainer.model.DateTime._
import org.apache.commons.beanutils.Converter


class DateTimeConverter extends Converter {
  override def convert(clazz: Class[_], value: Object): Object = {
    checkNotNull(clazz)
    checkNotNull(value)
    
    checkArgument(clazz == classOf[DateTime])
    checkArgument(classOf[String].isAssignableFrom(value.getClass))
    
    DateTime.fromIsoNoMillis(value.asInstanceOf[String])
  }
}
