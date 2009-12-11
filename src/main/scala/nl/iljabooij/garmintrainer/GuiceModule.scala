package nl.iljabooij.garmintrainer

import com.google.inject.{Binder,Module}
import com.google.inject.matcher.Matchers

import nl.iljabooij.garmintrainer.model._
import nl.iljabooij.garmintrainer.importer._
import nl.iljabooij.garmintrainer.parser._
import nl.iljabooij.garmintrainer.parser.digester._

/**
 * Module file for Guice.
 */
class GuiceModule extends Module {
	override def configure(binder: Binder) {
		binder.bind(classOf[ApplicationState]).to(classOf[ApplicationStateImpl]);
		binder.bind(classOf[TcxImporter]).to(classOf[TcxImporterImpl]);
		binder.bind(classOf[TcxParser]).to(classOf[DigesterTcxParser]);
	}
}
