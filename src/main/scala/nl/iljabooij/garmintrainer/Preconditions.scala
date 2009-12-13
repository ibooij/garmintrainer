package nl.iljabooij.garmintrainer

/**
 Some simple preconditions checks. Inspired by the checks in google collections.
 Instead of using those checks I chose to implement them myself, because they're
 very simple and that saves a dependency on a library.
 */
object Preconditions {
  /** Check if the argument is not null. If it is, raise a NullPointerException */
  def checkNotNull(o:Any) {
    if(o == null) {
      throw new NullPointerException
    }
  }
  
  def checkArgument(argument:Boolean) {
    if (!argument) {
      throw new IllegalArgumentException
    }
  }
}
