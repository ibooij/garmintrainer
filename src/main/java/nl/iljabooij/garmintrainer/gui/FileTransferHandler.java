/*
 * Copyright 2009 Ilja Booij
 * 
 * This file is part of GarminTrainer.
 * 
 * GarminTrainer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GarminTrainer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GarminTrainer.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.iljabooij.garmintrainer.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.TransferHandler;

import nl.iljabooij.garmintrainer.importer.TcxImportException;
import nl.iljabooij.garmintrainer.importer.TcxImporter;
import nl.iljabooij.garmintrainer.model.ApplicationState;
import nl.iljabooij.garmintrainer.util.InjectLogger;

import org.slf4j.Logger;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class FileTransferHandler extends TransferHandler {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private static final long serialVersionUID = 1L;

    @InjectLogger
    private Logger logger;
    
    private final DataFlavor uriListDataFlavor;
    private transient final ApplicationState applicationState;
    private transient final TcxImporter tcxImporter;

    @Inject
    public FileTransferHandler(final ApplicationState applicationState,
            final TcxImporter tcxImporter) {
        super();
        this.applicationState = applicationState;
        this.tcxImporter = tcxImporter;

        try {
            uriListDataFlavor = new DataFlavor(
                    "text/uri-list;class=java.lang.String");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                    "Unable to create DataFlavor for URI Lists", e);
        }
    }

    @Override
    public boolean canImport(final TransferSupport supp) {
        /* for the demo, we'll only support drops (not clipboard paste) */
        if (!supp.isDrop()) {
            return false;
        }

        /* return true if and only if the drop contains a list of files */
        return (supp.isDataFlavorSupported(uriListDataFlavor) ||
        		supp.isDataFlavorSupported(DataFlavor.javaFileListFlavor));
    }

    @SuppressWarnings("unchecked")
	private List<File> getFileListFromJavaFileList(final Transferable transferable) throws UnsupportedFlavorException, IOException {
    	return (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
    	
    }
    
    private List<File> getFileListFromUriList(final Transferable transferable) throws UnsupportedFlavorException, IOException, URISyntaxException {
        final Object data = transferable.getTransferData(uriListDataFlavor);
        logger.debug("dropped file list = {}", data);

        final String uriList = (String) data;
        final StringTokenizer stringTokenizer = new StringTokenizer(uriList, "\n");
        final List<File> files = Lists.newArrayListWithCapacity(stringTokenizer.countTokens());
        while (stringTokenizer.hasMoreTokens()) {
            files.add(new File(new URI(stringTokenizer.nextToken().trim())));
        }
        
        return files;

    }
    
    public boolean importData(final TransferSupport supp) {
        if (!canImport(supp)) {
            return false;
        }
        logger.debug("importData, can import!");

        try {
        	final Transferable transferable = supp.getTransferable();
        	List<File> files = Lists.newArrayList();
        	if (supp.isDataFlavorSupported(uriListDataFlavor)) {
        		files = getFileListFromUriList(transferable);
        	} else if (supp.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
        		files = getFileListFromJavaFileList(transferable);
        	}
        	parseFiles(files);
        }
        catch (URISyntaxException e) {
                logger.debug("URI syntax not supported. Cannot accept drop");
        } catch (UnsupportedFlavorException e) {
            logger.debug("Can't accept drop");
            return false;
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    private void parseFiles(final List<File> files) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                for (File file : files) {
                    // Create the table model
                    try {
                        tcxImporter.importTcx(file);
                    } catch (TcxImportException ex) {
                        applicationState.setErrorMessage(ex.getMessage());
                    }
                }
            }
        });
    }
}
