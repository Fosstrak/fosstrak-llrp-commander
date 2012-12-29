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
package org.fosstrak.llrp.commander.util.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.fosstrak.llrp.commander.util.JarFolderExtractor;
import org.junit.Assert;
import org.junit.Test;

/**
 * unit test some parts of the jar folder extractor not already covered by the tests using the extractor (e.g. LLRP test).
 * @author swieland
 *
 */
public class JarFolderExtractorTest {
	
	@Test(expected = IOException.class)
	public void testCopyStreamBothNull() throws IOException {
		JarFolderExtractor extractor = new JarFolderExtractor();
		extractor.copyStream(null, null);
	}
	
	@Test(expected = IOException.class)
	public void testCopyInStreamNull() throws IOException {
		JarFolderExtractor extractor = new JarFolderExtractor();
		ByteArrayOutputStream bis = new ByteArrayOutputStream();
		Assert.assertNotNull(bis);
		extractor.copyStream(null, bis);
	}
	
	@Test(expected = IOException.class)
	public void testCopyOutStreamNull() throws IOException {
		JarFolderExtractor extractor = new JarFolderExtractor();
		ByteArrayInputStream bis = new ByteArrayInputStream("".getBytes());
		Assert.assertNotNull(bis);
		extractor.copyStream(bis, null);
	}
}
