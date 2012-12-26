/*
 *  
 *  Fosstrak LLRP Commander (www.fosstrak.org)
 * 
 *  Copyright (C) 2008 ETH Zurich
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/> 
 *
 */

package org.fosstrak.llrp.commander.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

/**
 * a little helper allowing to extract a folder from a given jar file.
 * @author swieland
 *
 */
public class JarFolderExtractor {

	private static Logger log = Logger.getLogger(JarFolderExtractor.class);

	/**
	 * copy the given input stream to the given output stream.
	 * @param is the input stream.
	 * @param os the output stream.
	 * @return true if successful.
	 * @throws IOException error while writing or reading.
	 */
	public boolean copyStream(final InputStream is, final OutputStream os) throws IOException  {
		if ((null == is) || (null == os)) {
			throw new IOException("input and output stream must be not null.");
		}
		
		final byte[] buf = new byte[1024];

		int len = 0;
		while ((len = is.read(buf)) > 0) {
			os.write(buf, 0, len);
		}
		is.close();
		os.close();
		return true;
	}

	/**
	 * 
	 * @param jarFile the handle to the JAR file.
	 * @param entry the entry to be extracted.
	 * @param targetBaseFolder the base folder where to write to.
	 * @throws IOException upon read or write error.
	 */
	public void copyToFile(final JarFile jarFile, final JarEntry entry, final String targetBaseFolder) throws IOException  {
		final String fn = targetBaseFolder + entry.getName();
		log.debug(fn);
		if (entry.isDirectory()) {
			log.debug("creating new directory " + fn);
			new File(fn).mkdirs();
		} else {
			log.debug("writing file " + fn);
			copyStream(jarFile.getInputStream(entry), new FileOutputStream(fn));
		}
	}
	
	/**
	 * extracts the given directory and its subfolders/files into the given target folder.
	 * @param pathToJar the path to the JAR to use as container.
	 * @param directoryName the directory to extract from the JAR.
	 * @param targetFolder the folder where to write to.
	 * @throws IOException upon read or write error.
	 */
	public void extractDirectoryFromJar(final String pathToJar, final String directoryName, final String targetFolder) throws IOException {
		JarFile jarFile = new JarFile(new File(pathToJar));
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			final JarEntry entry = entries.nextElement();
			log.trace(entry);
			
			if (entry.getName().startsWith(directoryName)) {
				copyToFile(jarFile, entry, targetFolder);
			} else {
				log.trace("no match");
			}
		}
		jarFile.close();
	}
}
