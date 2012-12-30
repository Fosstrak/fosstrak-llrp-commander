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

import java.util.Map;

import org.apache.log4j.Logger;
import org.fosstrak.llrp.client.Repository;
import org.fosstrak.llrp.client.RepositoryFactory;
import org.fosstrak.llrp.commander.persistence.exception.PersistenceException;

/**
 * indirect the static factory call in order to enable testability. 
 * @author swieland
 *
 */
public class RepositoryFactoryDelegate {

	private static final Logger LOG = Logger.getLogger(RepositoryFactoryDelegate.class);
	
	/**
	 * create a new repository with the configuration parameters provided 
	 * via the parameters hash map.
	 * @param args a hash-map providing the parameters.
	 * @return an instance of a {@link Repository}.
	 * @throws PersistenceException when no instantiation was possible.
	 */
	public Repository create(Map<String, String> args) throws PersistenceException {
		try {
			return RepositoryFactory.create(args);
		} catch (Exception e) {
			LOG.info("could not create the repository", e);
			throw new PersistenceException(e);
		}
	}
}
