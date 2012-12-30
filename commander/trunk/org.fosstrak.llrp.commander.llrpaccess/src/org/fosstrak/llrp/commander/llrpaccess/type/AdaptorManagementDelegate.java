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

package org.fosstrak.llrp.commander.llrpaccess.type;

import org.fosstrak.llrp.adaptor.AdaptorManagement;

/**
 * allows to obtain a handle onto the LLRP Adaptor Management. (allow testability of this layer).
 * @author swieland
 *
 */
public class AdaptorManagementDelegate {
	
	/**
	 * retrieves a handle on the adaptor management.
	 * @return the adaptor management singleton.
	 */
	public AdaptorManagement getAdaptorManagement() {
		return AdaptorManagement.getInstance();
	}
}
