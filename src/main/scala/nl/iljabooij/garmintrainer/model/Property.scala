package nl.iljabooij.garmintrainer.model

object Property extends Enumeration {
  type Property = Value
  val CurrentActivity = Value("currentActivity")
  val ErrorMessage = Value("errorMessage")
}
