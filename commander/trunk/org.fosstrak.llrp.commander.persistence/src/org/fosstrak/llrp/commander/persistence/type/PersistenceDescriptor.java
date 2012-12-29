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

package org.fosstrak.llrp.commander.persistence.type;


/**
 * wrapper describing the underlying persistence repository attributes.
 * 
 * @author swieland
 *
 */
public class PersistenceDescriptor {
	
	private boolean wipeDbAtStartup;
	private boolean wipeRoAccessDbAtStartup;
	private boolean logRoAccess;
	private  String username;
	private  String password;
	private  String jdbc;
	private  String implementingClass;

	public PersistenceDescriptor(boolean wipeDbAtStartup, boolean wipeRoAccessDbAtStartup, boolean logRoAccess, String username, String password, String jdbc, String implementingClass) {
		this.wipeDbAtStartup = wipeDbAtStartup;
		this.wipeRoAccessDbAtStartup = wipeRoAccessDbAtStartup;
		this.logRoAccess = logRoAccess;
		this.username = username;
		this.password = password;
		this.jdbc = jdbc;
		this.implementingClass = implementingClass;
	}
	
	public boolean isWipeDbAtStartup() {
		return wipeDbAtStartup;
	}

	public boolean isWipeRoAccessDbAtStartup() {
		return wipeRoAccessDbAtStartup;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getJdbc() {
		return jdbc;
	}

	public String getImplementingClass() {
		return implementingClass;
	}

	public boolean isLogRoAccess() {
		return logRoAccess;
	}
}
