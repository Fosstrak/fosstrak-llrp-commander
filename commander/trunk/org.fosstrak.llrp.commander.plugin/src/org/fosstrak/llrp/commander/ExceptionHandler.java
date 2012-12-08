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

package org.fosstrak.llrp.commander;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.fosstrak.llrp.adaptor.exception.LLRPRuntimeException;
import org.fosstrak.llrp.client.LLRPExceptionHandler;
import org.fosstrak.llrp.client.LLRPExceptionHandlerTypeMap;

/**
* Exception Handler that processes Exceptions triggered in the AdaptorManagement. 
* The Exceptions get reported via a dialog box to the user. 
* @author zhanghao
* @author sawielan
*
*/
public class ExceptionHandler implements LLRPExceptionHandler {
	
	private static Logger log = Logger.getLogger(ExceptionHandler.class);
	
	// default caption for the dialog.	
	private final static String DIALOG_CAPTION = "LLRP Client Warning";
	
	private Shell shell;
	
	public ExceptionHandler(Shell aShell) {
		shell = aShell;
	}
	
	public void postExceptionToGUI(LLRPExceptionHandlerTypeMap exceptionType, LLRPRuntimeException e, String adaptorName, String readerName) {
		final LLRPExceptionHandlerTypeMap aExceptionType = exceptionType;
		final String aAdapter = adaptorName;
		final String aReader = readerName;
		final LLRPRuntimeException ex = e;
		
		// we need a special thread access to post a method to a SWT widget concurrently.
		shell.getDisplay().asyncExec(new Runnable () {

			public void run() {
				switch (aExceptionType) {
					case EXCEPTION_ADAPTOR_MANAGEMENT_NOT_INITIALIZED: {
						log.info("adaptor management is not initialized", ex);
						processMessage("AdaptorManagement is not initialized." + '\n'  +ex.getMessage());
						break;
					}
					case EXCEPTION_ADAPTOR_MANAGEMENT_CONFIG_NOT_STORABLE: {
						log.info("adaptor management cannot store the configuration", ex);
						processMessage("Could not store the configuration." + '\n' + ex.getMessage());
						break;
					}
					case EXCEPTION_ADAPTOR_ALREADY_EXISTS: {
						log.info("adaptor already exists", ex);
						processMessage("Adaptor already exists: " +  aAdapter);
						break;
					}
					case EXCEPTION_READER_NOT_EXIST: {
						log.info("no such reader", ex);
						processReaderNotExist(aAdapter, aReader);
						break;
					}
					case EXCEPTION_ADAPTER_NOT_EXIST: {
						log.info("no such adaptor", ex);
						processAdapterNotExist(aAdapter);
						break;
					}
					case EXCEPTION_READER_LOST: {
						log.info("lost reader", ex);
						processReaderLost(aAdapter, aReader);
						break;
					}
					case EXCEPTION_ADAPTER_LOST: {
						log.info("lost adapter", ex);
						processAdapterLost(aAdapter);
						break;
					}
					case EXCEPTION_MSG_SENDING_ERROR: {
						log.info("could not send a message", ex);
						processSendingError(aAdapter, aReader);
						break;
					}
					case EXCEPTION_NO_READER_CONFIG_MSG: {
						log.info("no configuration of the given reader", ex);
						processNoReaderConfigError(aAdapter, aReader);
						break;
					}
					case EXCEPTION_NO_ROSPEC_MSG: {
						log.info("no such ro spec", ex);
						processNoROSpecError(aAdapter, aReader);
						break;
					}
					case EXCEPTION_MSG_SYNTAX_ERROR: {
						log.info("syntax error", ex);
						processMsgSyntaxError();
						break;
					}
					default: {
						log.info("general non treated exception", ex);
						processGeneralException(aAdapter, aReader, ex.getMessage());
					}
				}
				ResourceCenter.getInstance().getReaderExplorerView().refresh();
			}

		});

	}
	
	private void processReaderNotExist(String aAdapter, String aReader) {
		String aText = "Reader " + aReader + " @ " + aAdapter + " is not found!";
		MessageDialog.openWarning(shell, DIALOG_CAPTION, aText);
	}
	
	private void processAdapterNotExist(String aAdapter) {
		String aText = "Adapter " + aAdapter + " is not found!";
		MessageDialog.openWarning(shell, DIALOG_CAPTION, aText);
	}
	
	private void processReaderLost(String aAdapter, String aReader) {
		String aText = "Reader " + aReader + " @ " + aAdapter + " is lost!";
		MessageDialog.openWarning(shell, DIALOG_CAPTION, aText);
	}
	
	private void processAdapterLost(String aAdapter) {
		String aText = "Adapter " + aAdapter + " is lost!";
		MessageDialog.openWarning(shell, DIALOG_CAPTION, aText);
	}
	
	private void processSendingError(String aAdapter, String aReader) {
		String aText = "Sending Message to " + aReader + " @ " + aAdapter + " failed!";
		MessageDialog.openWarning(shell, DIALOG_CAPTION, aText);
	}
	
	private void processNoReaderConfigError(String aAdapter, String aReader) {
		String aText = "No existing GET_READER_CONFIG_RESPONSE message \nfrom " + aReader + " @ " + aAdapter + ".\nPlease send GET_READER_CONFIG first.";
		MessageDialog.openWarning(shell, DIALOG_CAPTION, aText);
	}
	
	private void processNoROSpecError(String aAdapter, String aReader) {
		String aText = "No existing GET_ROSPECS_RESPONSE message \nfrom " + aReader + " @ " + aAdapter + ".\nPlease send GET_ROSPECS first.";
		MessageDialog.openWarning(shell, DIALOG_CAPTION, aText);
	}
	
	private void processGeneralException(String aAdapter, String aReader, String aText) {
		MessageDialog.openWarning(shell, DIALOG_CAPTION, aText);
	}
	
	private void processMsgSyntaxError() {
		String aText = "XML Syntax Error!";
		MessageDialog.openWarning(shell, DIALOG_CAPTION, aText);
	}
	
	private void processMessage(String message) {
		MessageDialog.openWarning(shell, DIALOG_CAPTION, message);
	}
}
