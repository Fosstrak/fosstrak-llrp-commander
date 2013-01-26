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

package org.fosstrak.llrp.commander.llrpaccess;

import java.util.List;

import org.fosstrak.llrp.client.LLRPExceptionHandler;
import org.fosstrak.llrp.client.MessageHandler;
import org.fosstrak.llrp.commander.llrpaccess.exception.LLRPAccessException;
import org.fosstrak.llrp.commander.type.ReaderMetaData;
import org.llrp.ltk.types.LLRPMessage;

/**
 * mask away direct access to the LLRP adapter (e.g. access to static variables) and 
 * let the commander only work on this eclipse layer.
 * @author swieland
 *
 */
public interface LLRPAccess {
	
	/**
	 * returns the requested reader meta data on the given adapter.
	 * @param adapterName the name of the enclosing adapter.
	 * @param readerName the name of the requested reader.
	 * @return the reader or null if not found.
	 * @throws LLRPAccessException when the chosen operation failed.
	 */
	ReaderMetaData getReaderMetaData(String adapterName, String readerName) throws LLRPAccessException;
	
	/**
	 * enables/disables the report keep-alive flag on the chosen reader.
	 * @param adapterName the name of the enclosing adapter.
	 * @param readerName the name of the requested reader.
	 * @param keepAlive true then enable report keep-alive, false disable report keep-alive.
	 * @throws LLRPAccessException when the chosen operation failed.
	 */
	void setReaderReportKeepalive(String adapterName, String readerName, boolean keepAlive) throws LLRPAccessException;
	
	/**
	 * whether the requested reader is in report keep-alive mode or not.
	 * @param adapterName the name of the enclosing adapter.
	 * @param readerName the name of the requested reader.
	 * @return true if report keep-alive mode, false otherwise.
	 * @throws LLRPAccessException when the chosen operation failed.
	 */
	boolean isReaderReportKeepalive(String adapterName, String readerName) throws LLRPAccessException;
	
	/**
	 * whether the requested reader is connected or not.
	 * @param adapterName the name of the enclosing adapter.
	 * @param readerName the name of the requested reader.
	 * @return true if connected, false otherwise.
	 * @throws LLRPAccessException when the chosen operation failed.
	 */
	boolean isReaderConnected(String adapterName, String readerName) throws LLRPAccessException;
	
	/**
	 * connects this reader.
	 * @param adapterName the name of the adapter enclosing the reader.
	 * @param readerName the name of the reader to connect.
	 * @throws LLRPAccessException when the chosen operation failed.
	 */
	void connectReader(String adapterName, String readerName) throws LLRPAccessException;
	
	/**
	 * connects this reader.
	 * @param adapterName the name of the adapter enclosing the reader.
	 * @param readerName the name of the reader to connect.
	 * @param clientInitiatedConnection if the connection is initiated by the client then you should pass true. if the physical reader initiates the connection then provide false.
	 * @throws LLRPAccessException when the chosen operation failed.
	 */
	void connectReader(String adapterName, String readerName, boolean clientInitiatedConnection) throws LLRPAccessException;
	
	/**
	 * disconnects this reader.
	 * @param adapterName the name of the adapter enclosing the reader.
	 * @param readerName the name of the reader to connect.
	 * @throws LLRPAccessException when the chosen operation failed.
	 */
	void disconnectReader(String adapterName, String readerName) throws LLRPAccessException;
	
	/**
	 * tells whether a reader already exists on an enclosing adapter.
	 * @param adapterName the name of the enclosing adaptor.
	 * @param readerName the name of the reader to check.
	 * @return true if reader exists else false.
	 * @throws LLRPAccessException when the chosen operation failed.
	 */
	boolean containsReader(String adapterName, String readerName) throws LLRPAccessException;
	
	/**
	 * get the names of all the readers enclosed by this adapter.
	 * @param adapterName the name of the enclosing adapter.
	 * @return a list of all the reader names on this adapter.
	 */
	List<String> getReaderNames(String adapterName) throws LLRPAccessException;

	/**
	 * undefine the given reader.
	 * @param adapterName the adapter where the reader is stored on.
	 * @param readerName then name of the reader to be undefined.
	 * @throws LLRPAccessException when the chosen operation failed.
	 */
	void undefineReader(String adapterName, String readerName) throws LLRPAccessException;

	/**
	 * define a new reader on the given adapter.
	 * @param adapterName the adapter where to define the reader on.
	 * @param readerName the new readers name.
	 * @param ip the IP of the reader.
	 * @param port the port.
	 * @param clientInitiated client initiates the connection when set to true.
	 * @param connectImmediately connect to the reader immediately after connection when set to true.
	 * @throws LLRPAccessException when the chosen operation failed.
	 */
	void defineReader(String adapterName, String readerName, String ip, int port, boolean clientInitiated, boolean connectImmediately) throws LLRPAccessException;
	
	/**
	 * tells whether an adapter already exists.
	 * @param adapterName the name of the adaptor to check.
	 * @return true if adaptor exists else false.
	 * @throws LLRPAccessException when the chosen operation failed.
	 */
	boolean containsAdapter(String adapterName) throws LLRPAccessException;

	/**
	 * adds a new adapter to the adapter list. <strong>the name might be changed automatically thus have a look at the returned adapter!</strong>
	 * @param adaptorNamePreferred the preferred name of the new adapter - the actual name of the created adapter is returned!
	 * @param address if you are using a client adapter you have to provide the address of the server stub.
	 * @return the final name of the adapter chosen by the underlying subsystem.
	 * @throws LLRPAccessException when the chosen operation failed.
	 */
	String defineAdapter(String adaptorNamePreferred, String address) throws LLRPAccessException;
	
	/**
	 * removes an adapter from the adaptor list.
	 * @param adapterName the name of the adaptor to remove.
	 * @throws LLRPAccessException when either the name does not exist or when an internal runtime error occurs.
	 */
	void undefineAdapter(String adapterName) throws LLRPAccessException;
	
	/**
	 * returns a list of all the available adapter names.
	 * @return a list of all the available adapter names.
	 * @throws LLRPAccessException when the chosen operation failed.
	 */
	List<String> getAdaptorNames() throws LLRPAccessException;
	
	/**
	 * helper to access the default local adapter name more convenient.
	 * @return the default local adapters name.
	 */
	String getDefaultAdaptorName() throws LLRPAccessException;

	/**
	 * checks, whether a given adapter is a the default adapter or not.
	 * @param adapterName the name of the adapter to check.
	 * @return true if the adapter is default adapter, false otherwise.
	 */
	boolean isDefaultAdapter(String adapterName);
	
	/**
	 * checks, whether a given adapter is a local adapter or not.
	 * @param adapterName the name of the adapter to check.
	 * @return true if the adapter is local, false otherwise.
	 * @throws LLRPAccessException when the chosen operation failed.
	 */
	boolean isLocalAdapter(String adapterName) throws LLRPAccessException;
	
	/**
	 * disconnectReaders shuts down all local readers. 
	 */
	void disconnectReaders();
	
	/**
	 * the client leaves the adapter management. the management makes the cleanup.
	 */
	void shutdown();
	
	/**
	 * enqueue an LLRPMessage to be sent to a LLRP reader. the adapter will process the message when ready.
	 * @param adapterName the name of the adapter holding the LLRP reader.
	 * @param readerName the name of the LLRP reader.
	 * @param message the LLRPMessage.
	 * @throws LLRPAccessException when the queue of the adapter is full.
	 */
	void enqueueLLRPMessage(String adapterName, String readerName, LLRPMessage message) throws LLRPAccessException;

	/**
	 * register a handler that will receive only a restricted set of messages.
	 * @param handler the handler.
	 * @param clzz the type of messages that the handler likes to receive (example KEEPALIVE.class).
	 */
	void registerPartialHandler(MessageHandler handler, Class<?> clzz);

	/**
	 * remove a handler from the handlers list.
	 * @param handler the handler to remove.
	 * @param clzz the class where the handler is registered.
	 */
	void deregisterPartialHandler(MessageHandler handler, Class<?> clzz);
	
	/**
	 * register a handler that will receive all the incoming messages.
	 * @param handler the handler.
	 */
	void registerFullHandler(MessageHandler handler);
	
	/**
	 * sets the exception handler.
	 * @param exceptionHandler the exception handler.
	 */
	void setExceptionHandler(LLRPExceptionHandler handler);

	/**
	 * initialize the layer.
	 * @param configurationPath the path to where the configuration can be stored.
	 * @throws LLRPAccessException when the chosen operation failed.
	 */
	void initialize(final String configurationPath) throws LLRPAccessException;

}
