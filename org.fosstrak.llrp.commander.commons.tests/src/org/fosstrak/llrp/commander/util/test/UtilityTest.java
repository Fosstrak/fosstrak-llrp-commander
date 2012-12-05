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

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.fosstrak.llrp.commander.util.Utility;
import org.junit.Test;


/**
 * small test for the utility class.
 * @author swieland
 */
public class UtilityTest {
	
	@Test
	public void testStackTraceNiceNull() {
		String result = Utility.stackTraceNice(null);
		Assert.assertEquals(StringUtils.EMPTY, result);
	}

	@Test
	public void testStackTraceNice() {
		Exception ex = new Exception("hello");
		String result = Utility.stackTraceNice(ex);
		Assert.assertNotNull(result);
		Assert.assertNotSame(StringUtils.EMPTY, result);
	}
}
