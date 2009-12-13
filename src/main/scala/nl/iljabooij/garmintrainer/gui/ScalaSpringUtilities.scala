package nl.iljabooij.garmintrainer.gui

import nl.iljabooij.garmintrainer.LoggerHelper

import java.awt.{Component,Container}
import javax.swing.{Spring,SpringLayout}

/**
  Port to scala of SpringUtilities. This is quite an ugly port. Need to
  rewrite!
 */
object ScalaSpringUtilities extends LoggerHelper {
  def makeGrid(parent: Container,
               rows: Int, cols: Int,
               initialX: Int, initialY: Int,
               padX: Int, padY: Int) {
    val layout:SpringLayout = try {
      parent.getLayout.asInstanceOf[SpringLayout]
    } catch {
      case e: ClassCastException => {
        error("Layout of container must be SpringLayout")
        return
      }
    }
    val xPadSpring = Spring.constant(padX);
    val yPadSpring = Spring.constant(padY);
    val initialXSpring = Spring.constant(initialX);
    val initialYSpring = Spring.constant(initialY);
    val max = rows * cols;
  
    val components = getComponents(parent, rows*cols)
    val (maxWidthSpring,maxHeightSpring) = findMaxSprings(layout, components)
    
    getConstraints(layout, parent, rows*cols).foreach(c => {
      c.setWidth(maxWidthSpring)
      c.setHeight(maxHeightSpring)
    })
    
    alignInGrid(getConstraints(layout, parent, rows*cols), initialXSpring,
                initialYSpring, xPadSpring, yPadSpring, cols)
    resizeContainer(layout, parent, rows*cols, padX, padY)
  }
  
  private def resizeContainer(layout: SpringLayout, container: Container, 
                              nr: Int, xPad: Int, yPad: Int) {
    val lastConstraint = getConstraints(layout, container, nr).reverse.head
    val parentConstraint = layout.getConstraints(container)
    
    parentConstraint.setConstraint(SpringLayout.SOUTH,
                                   Spring.sum(Spring.constant(yPad),
                                              lastConstraint.getConstraint(SpringLayout.SOUTH)))
    parentConstraint.setConstraint(SpringLayout.EAST,
                                   Spring.sum(Spring.constant(xPad),
                                              lastConstraint.getConstraint(SpringLayout.EAST)))    
  }
 
  private def getWidthSpring(layout: SpringLayout)(component: Component) = {
    getSpring(layout, component).getWidth
  }
  
  private def getHeightSpring(layout: SpringLayout)(component: Component) = {
    getSpring(layout, component).getHeight
  }
  
  private def getSpring(layout: SpringLayout, component: Component) = {
    layout.getConstraints(component)
  }
  
  private def getComponents(container: Container, nr: Int): List[Component] = {
    (for (i <- 0 until nr) yield container.getComponent(i)).toList
  }
  
  private def getConstraints(layout: SpringLayout, container: Container, nr: Int) = {
    (for (i <- 0 until nr) yield layout.getConstraints(container.getComponent(i))).toList
  }
  
  private def findMaxSprings(layout: SpringLayout, 
                             components: List[Component]): Pair[Spring,Spring] = {
    def widthF = getWidthSpring(layout)_
    val startWidth = widthF(components.head)
    val maxWidth = components.tail.map(c => widthF(c)).foldLeft(startWidth)((l,r) => Spring.max(l,r))
    
    def heightF = getHeightSpring(layout)_
    val startHeight = heightF(components.head)
    val maxHeight = components.tail.map(c => heightF(c))
      .foldLeft(startHeight)((l,r) => Spring.max(l,r))
    
    new Pair(maxWidth, maxHeight)
  }

  private def alignInGrid(constraints: List[SpringLayout.Constraints],
                          initialX: Spring, initialY: Spring,
                          padX: Spring, padY: Spring,
                          columns: Int) {
    val rows = (for(i <- 0 until constraints.length 
                    by columns) yield constraints.slice(i, i + columns)).toList
    var ySpring = Spring.sum(layoutRow(rows.head, initialX, padX, initialY), padY)
    rows.tail.foreach(row => ySpring = Spring.sum(layoutRow(row, initialX, padX, ySpring), padY))
  }
  
  private def layoutRow(row: List[SpringLayout.Constraints],
                        initialX: Spring, padX: Spring, 
                        ySpring: Spring) = {
    row.head.setX(initialX)
    var lastConstraint = row.head
    row.tail.foreach(constraint => {
      constraint.setX(Spring.sum(lastConstraint.getConstraint(SpringLayout.EAST), padX))
      lastConstraint = constraint
    })
    row.foreach(constraint => constraint.setY(ySpring))
    
    // return the y position of the row so the next row can be put under it.
    row.head.getConstraint(SpringLayout.SOUTH)
  }
}
