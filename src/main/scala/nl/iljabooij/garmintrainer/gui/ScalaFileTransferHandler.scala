package nl.iljabooij.garmintrainer.gui

import java.awt.datatransfer.{DataFlavor,Transferable}
import java.io.File
import java.net.URI
import java.util.concurrent.Executors
import javax.swing.TransferHandler
import TransferHandler.TransferSupport

import com.google.inject.Inject

import nl.iljabooij.garmintrainer.model.ApplicationState 
import nl.iljabooij.garmintrainer.importer.{TcxImporter,TcxImportException}

/**
 * Implementation of the file transfer handler. This class is used
 * to allow dropping of files onto the GUI. It will then send all those
 * files to the importer.
 */
class ScalaFileTransferHandler @Inject() (applicationState:ApplicationState, importer: TcxImporter) 
	extends TransferHandler with LoggerHelper {
  val uriListDataFlavor = new DataFlavor("text/uri-list;class=java.lang.String")
  val executor = Executors.newSingleThreadExecutor()
  
  override def canImport(support: TransferSupport): Boolean = {
    (support.isDrop && (
      support.isDataFlavorSupported(uriListDataFlavor) || support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)
    ))
  }
  
  private def extractFilesFromUriList(transferable: Transferable): List[File] = {
    transferable.getTransferData(uriListDataFlavor)
      .asInstanceOf[String]
      .split("\n").toList
      .map(str=> new File(new URI(str.trim)))
  }
  
  private def extractFilesFromJavaFileList(transferable: Transferable): List[File] = {
    transferable.getTransferData(DataFlavor.javaFileListFlavor).asInstanceOf[List[File]]
  }
  
  private def extractFileList(support: TransferSupport): List[File] = {
    val transferable = support.getTransferable
    
    if (support.isDataFlavorSupported(uriListDataFlavor)) {
      extractFilesFromUriList(transferable)
    } else {
      extractFilesFromJavaFileList(transferable) 
    }
  }
  
  override def importData(support: TransferSupport): Boolean = {
    if (canImport(support)) {
      debug("Can import!")
      
      
      val files:List[File] = try {
        extractFileList(support)
      } catch {
        case e:Exception => {
          debug("Cannot except drop")
          return false
        }
      }
      parseFiles(files)
      
      true
    } else {
      false
    }
  }
  
  private def parseFiles(files: List[File]): Unit = {
    executor.execute(new Runnable {
      def run = {
        files.foreach { file =>
          try {
            importer.importTcx(file)
          } catch {
            case e: TcxImportException => applicationState.setErrorMessage(e.getMessage)
          }
        }
      }
    })
  }
}
