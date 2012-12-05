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

import org.fosstrak.llrp.commander.util.Range;

import org.junit.Test;

/**
 * small test case for the range utility.
 * 
 * @author swieland
 *
 */
public class RangeTest {

    @Test
	public void testRange() {
    	Range range = new Range(-10, 10);
    	Assert.assertEquals(-10, range.getLowerBound());
    	Assert.assertEquals(10, range.getUpperBound());
	}
}
