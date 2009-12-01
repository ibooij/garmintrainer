package nl.iljabooij.garmintrainer.gui

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

import nl.iljabooij.garmintrainer.model.{ApplicationState,Property}

object ApplicationStateAdapter {
  def addPropertyChangeListener(applicationState: ApplicationState,
                                property: Property.Property,
                                handler: Any => Unit) = {
    val listener = new PropertyChangeListener {
      def propertyChange(event: PropertyChangeEvent): Unit = {
        handler(event.getNewValue)
      }
    }
    applicationState.addPropertyChangeListener(property, listener)                            
  }
}
