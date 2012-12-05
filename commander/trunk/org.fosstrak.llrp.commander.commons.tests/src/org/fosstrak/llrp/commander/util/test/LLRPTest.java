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

import org.junit.Assert;
import org.junit.Test;
import org.llrp.ltkGenerator.generated.LlrpDefinition;
import org.apache.log4j.Logger;
import org.fosstrak.llrp.commander.util.LLRP;

/**
 * test the LLRP helper.
 * 
 * @author swieland
 *
 */
public class LLRPTest {

	private static Logger log = Logger.getLogger(LLRPTest.class);
	
	@Test
	public void testDefinitionIsExctractedAndNotNull() {
		 LlrpDefinition definition = LLRP.getLlrpDefintion();
		 Assert.assertNotNull(definition);
		 int i=0;
		 for (Object o : definition.getMessageDefinitionOrParameterDefinitionOrChoiceDefinition()) {
			 log.trace(o.getClass().getCanonicalName());
			 i++;
		 }
		 Assert.assertNotSame(0, i);
	}
}
