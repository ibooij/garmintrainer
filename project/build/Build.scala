import sbt._

class Build(info: ProjectInfo) extends DefaultProject(info)
{
    val scalaToolsRepo = "Scala Tools Repository" at "http://scala-tools.org/repo-releases/"
    
    val commonsBeanUtils = "commons-beanutils" % "commons-beanutils" % "1.8.0"
    val commonsDigester = "commons-digester" % "commons-digester" % "2.0"
    val commonsLang = "commons-lang" % "commons-lang" % "2.4"
    val googleCollections = "com.google.collections" % "google-collections" % "1.0-rc2"
    val guice = "com.google.inject" % "guice" % "2.0"
    val jodatime = "joda-time" % "joda-time" % "1.6"
    val junit = "junit" % "junit" % "4.6" % "test"
    val mockito = "org.mockito" % "mockito-all" % "1.8.0" % "test"

    val scalaSwing = "org.scala-lang" % "scala-swing" % "2.7.7"
    val scalaTest = "org.scalatest" % "scalatest" % "1.0" % "test"
    val slf4jApi = "org.slf4j" % "slf4j-api" % "1.5.8"
    val slf4jLog4j = "org.slf4j" % "slf4j-log4j12" % "1.5.8"
}
