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

package org.fosstrak.llrp.commander.persistence.exception;

/**
 * custom exception for the persistence layer.
 * @author swieland
 *
 */
public class PersistenceException extends Exception {

	private static final long serialVersionUID = -8734938260142941390L;

	/**
	 * new persistence exception.
	 * @param message exception cause.
	 */
	public PersistenceException(String message) {
		super(message);
	}


	/**
	 * new persistence exception wrapping the old exception.
	 * @param cause the causing exception
	 */
	public PersistenceException(Throwable cause) {
		super(cause);
	}

}
