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
package nl.iljabooij.garmintrainer.importer;

import java.io.File;

/**
 * Interface for importer for tcx files.
 * @author "Ilja Booij <ibooij@gmail.com>"
 *
 */
public interface TcxImporter {
	/**
	 * Import a tcx file
	 * @param tcxFile the file to import
	 * @throws TcxImportException if there was an error importing the tcx file
	 */
	public void importTcx(final File tcxFile) throws TcxImportException;
}
