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

package org.fosstrak.llrp.commander.persistence;

import java.util.List;

import org.fosstrak.llrp.adaptor.exception.LLRPRuntimeException;
import org.fosstrak.llrp.client.LLRPMessageItem;
import org.fosstrak.llrp.client.repository.sql.roaccess.ROAccessItem;
import org.fosstrak.llrp.commander.persistence.exception.PersistenceException;
import org.fosstrak.llrp.commander.persistence.type.PersistenceDescriptor;

/**
 * interface to the persistence layer of the LLRP commander.
 * @author swieland
 *
 */
public interface Persistence {

	/** flag to set the retrieval of messages to all messages in the persistence. */
	public static final int RETRIEVE_ALL = -1;
	
	/**
	 * initialize the persistence layer. if the given container cannot be used for 
	 * any reason, the persistence layer will try to use a fall-back persistence.
	 * @param desc the container to use below the hoods.
	 * @param useFallbackOnly if set to true, the fall-back persistence is applied directly.
	 * @return if an automatic fall-back has been applied, then the exception of the initial setup call is returned here. null otherwise.
	 * @throws LLRPRuntimeException upon non-correctable errors.
	 */
	PersistenceException initialize(boolean useFallbackOnly, PersistenceDescriptor desc) throws LLRPRuntimeException;
	
	/**
	 * either initialize a new persistence Container or switch the currently used to the new one. the
	 * new container is automatically created and initialized.
	 * @param desc description of the persistence container.
	 * @return true if OK. false if the same container as the requested one is already in use.
	 * @throws PersistenceException when the container could not be initialized.
	 */
	boolean change(PersistenceDescriptor desc) throws PersistenceException;
	
	/**
	 * test if the given persistence container can be initialized and used. in order to do so,
	 * the container is created, tested and then destroyed. the currently used container is kept.
	 * @param desc description of the persistence container.
	 * @return true if test OK. false if the same container as the requested one is already in use.
	 * @throws PersistenceException when the container could not be initialized.
	 */
	boolean test(PersistenceDescriptor desc) throws PersistenceException;
	
	/**
	 * tests if the given persistence layer supports to store RoAccessRepositories.
	 * @return true if supported, false otherwise.
	 */
	boolean supportsRoAccessRepository();
	
	/**
	 * Put the LLRP Message Item to the repository. if null, then the method immediately returns.
	 * 
	 * @param message LLRP Message Wrapper Item
	 */
	void put(LLRPMessageItem message);
	
	/**
	 * Get the LLRP Message identified by its message id.
	 * 
	 * @param id The unique message ID
	 * @return LLRP Message Wrapper Item
	 */
	LLRPMessageItem get(String id);
	
	/**
	 * returns all the messages from the specified adaptor and the reader 
	 * limited by num. if you set num to RETRIEVE_ALL all messages get returned.
	 * if you set readerName to null, all the messages of all the readers with 
	 * adaptor adaptorName will be returned.
	 * @param adaptorName the name of the adaptor.
	 * @param readerName the name of the reader.
	 * @param num how many messages to retrieve.
	 * @param content if true retrieve the message content, false no content.
	 * @return a list of messages.
	 */
	List<LLRPMessageItem> get(String adaptorName, String readerName, int num, boolean content);

	/**
	 * @return all the ro access reports stored in the database.
	 */
	List<ROAccessItem> getAllRoAccessReports();
	
	/**
	 * performs an internal health check.
	 * @return true if health check is OK, false otherwise. 
	 */
	boolean isClean();
	
	/**
	 * returns the number of messages in the repository to a given filter.
	 * @param adaptor the name of the adaptor to filter. if null all the 
	 * messages in the repository get return.
	 * @param reader the name of the reader to filter. if null all the 
	 * messages of the given adaptor will be returned.
	 * @return the number of messages in the repository.
	 */
	int count(String adaptor, String reader);
	
	/**
	 * Clear all the items in repository.
	 */
	void clearAll();
	
	/**
	 * clear all the items that belong to a given adapter.
	 * @param adapter the name of the adapter to clear.
	 */
	void clearAdapter(String adapter);
	
	/**
	 * clear all the items that belong to a given reader on a given adapter.
	 * @param adapter the name of the adapter where the reader belongs to.
	 * @param reader the name of the reader to clear.
	 */
	void clearReader(String adapter, String reader);
	
	/**
	 * clear the roAccess Reports repository.
	 */
	void clearRoAccessReports();
	
	/**
	 * Close the repository.
	 */
	void close();

	/**
	 * register the ro access reports repository for ro access reports.
	 */
	void registerForRoAccessReports();
}
