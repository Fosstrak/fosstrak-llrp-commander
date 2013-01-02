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

import org.apache.commons.lang.StringUtils;


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

	/**
	 * create a descriptor.
	 * @param wipeDbAtStartup whether to wipe the database at startup.
	 * @param wipeRoAccessDbAtStartup
	 * @param logRoAccess whether to log RO ACCESS reports.
	 * @param username user name to the database.
	 * @param password password to the database.
	 * @param jdbc JDBC connector if JDBC shall be used.
	 * @param implementingClass class of the desired repository.
	 */
	public PersistenceDescriptor(boolean wipeDbAtStartup, boolean wipeRoAccessDbAtStartup, boolean logRoAccess, String username, String password, String jdbc, String implementingClass) {
		this.wipeDbAtStartup = wipeDbAtStartup;
		this.wipeRoAccessDbAtStartup = wipeRoAccessDbAtStartup;
		this.logRoAccess = logRoAccess;
		this.username = username;
		this.password = password;
		this.jdbc = jdbc;
		this.implementingClass = implementingClass;
	}
	
	/**
	 * @return whether to wipe the database at startup.
	 */
	public boolean isWipeDbAtStartup() {
		return wipeDbAtStartup;
	}

	/**
	 * @return whether to wipe the RO access reports repository at startup.
	 */
	public boolean isWipeRoAccessDbAtStartup() {
		return wipeRoAccessDbAtStartup;
	}

	/**
	 * @return user name to the database.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return password to the database.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return JDBC connector if JDBC shall be used.
	 */
	public String getJdbc() {
		return jdbc;
	}

	/**
	 * @return class of the desired repository.
	 */
	public String getImplementingClass() {
		return implementingClass;
	}

	/**
	 * @return whether to log RO ACCESS reports.
	 */
	public boolean isLogRoAccess() {
		return logRoAccess;
	}
	
	/**
	 * @return a dummy persistence descriptor.
	 */
	public static final PersistenceDescriptor dummy() {
		return new PersistenceDescriptor(true, true, true, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);
	}
}
