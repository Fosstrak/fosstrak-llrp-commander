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

package org.fosstrak.llrp.commander.type;

/**
 * some constants used in order to determine the different messages.
 * 
 * <strong>Please notice that the message must remain upper case!</strong>
 * @author swieland
 *
 */
public final class MessageConstants {
	
	public static final String GET_READER_CAPABILITIES = "GET_READER_CAPABILITIES";
	public static final String GET_READER_CONFIG = "GET_READER_CONFIG";
	public static final String SET_READER_CONFIG = "SET_READER_CONFIG";
	public static final String GET_REPORT = "GET_REPORT";
	public static final String ENABLE_EVENTS_AND_REPORTS = "ENABLE_EVENTS_AND_REPORTS";
	public static final String CLOSE_CONNECTION = "CLOSE_CONNECTION";
	public static final String CUSTOM_MESSAGE = "CUSTOM_MESSAGE";
	
	public static final String GET_ROSPECS = "GET_ROSPECS";
	public static final String ADD_ROSPEC = "ADD_ROSPEC";
	public static final String ENABLE_ROSPEC = "ENABLE_ROSPEC";
	public static final String DISABLE_ROSPEC = "DISABLE_ROSPEC";
	public static final String START_ROSPEC = "START_ROSPEC";
	public static final String STOP_ROSPEC = "STOP_ROSPEC";
	public static final String DELETE_ROSPEC = "DELETE_ROSPEC";
	
	public static final String ENABLE_ACCESSSPEC= "ENABLE_ACCESSSPEC";
	public static final String GET_ACCESSSPECS = "GET_ACCESSSPECS";
	public static final String ADD_ACCESSSPEC = "ADD_ACCESSSPEC";
	public static final String DISABLE_ACCESSSPEC = "DISABLE_ACCESSSPEC";
	public static final String DELETE_ACCESSSPEC = "DELETE_ACCESSSPEC";
	
	public static final String RO_SPEC_ID = "ROSpecID";
	public static final String ACCESS_SPEC_ID = "AccessSpecID";
		
	private MessageConstants() {		
	}
}
