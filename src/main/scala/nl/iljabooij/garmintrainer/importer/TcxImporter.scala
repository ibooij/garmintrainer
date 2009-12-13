package nl.iljabooij.garmintrainer.importer

import java.io.File

/**
 * Interface for importer for tcx files.
 * @author "Ilja Booij <ibooij@gmail.com>"
 */
trait TcxImporter {
  /**
   * Import a tcx file
   * @param tcxFile the file to import
   * @throws TcxImportException if there was an error importing the tcx file
   */
  def importTcx(tcxFile: File)
}
