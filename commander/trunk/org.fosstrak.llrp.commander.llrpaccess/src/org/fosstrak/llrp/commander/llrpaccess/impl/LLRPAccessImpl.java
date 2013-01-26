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

package org.fosstrak.llrp.commander.llrpaccess.impl;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.fosstrak.llrp.adaptor.AdaptorManagement;
import org.fosstrak.llrp.adaptor.config.FileStoreConfiguration;
import org.fosstrak.llrp.adaptor.exception.LLRPRuntimeException;
import org.fosstrak.llrp.client.LLRPExceptionHandler;
import org.fosstrak.llrp.client.MessageHandler;
import org.fosstrak.llrp.commander.llrpaccess.LLRPAccess;
import org.fosstrak.llrp.commander.llrpaccess.exception.LLRPAccessException;
import org.fosstrak.llrp.commander.llrpaccess.type.AdaptorManagementDelegate;
import org.fosstrak.llrp.commander.type.ReaderMetaData;
import org.llrp.ltk.types.LLRPMessage;

/**
 * default implementation of the LLRP access layer.
 * @author swieland
 *
 */
public class LLRPAccessImpl implements LLRPAccess {
	
	private AdaptorManagementDelegate delegate = new AdaptorManagementDelegate();

	private static final String CAUGHT_EXCEPTION = "caught exception";
	
	
	/** the logger. */
	private static final Logger LOG = Logger.getLogger(LLRPAccessImpl.class);
	
	private org.fosstrak.llrp.adaptor.Reader getReaderInternal(String adapterName, String readerName) throws LLRPAccessException {
		try {
			return getMgmt().getAdaptor(adapterName).getReader(readerName);
		} catch (Exception e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		}
	}

	@Override
	public ReaderMetaData getReaderMetaData(String adapterName, String readerName) throws LLRPAccessException {
		try {
			org.fosstrak.llrp.adaptor.ReaderMetaData metaData = getReaderInternal(adapterName, readerName).getMetaData();
			ReaderMetaData meta = new ReaderMetaData();
			BeanUtils.copyProperties(meta, metaData);
			return meta;
		} catch (RemoteException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		} catch (Exception e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		}
	}

	@Override
	public void setReaderReportKeepalive(String adapterName, String readerName, boolean keepAlive) throws LLRPAccessException {
		try {
			getReaderInternal(adapterName, readerName).setReportKeepAlive(keepAlive);
		} catch (RemoteException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		}
	}

	@Override
	public boolean isReaderReportKeepalive(String adapterName, String readerName) throws LLRPAccessException {
		try {
			return getReaderInternal(adapterName, readerName).isReportKeepAlive();
		} catch (RemoteException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		}
	}

	@Override
	public boolean isReaderConnected(String adapterName, String readerName)
			throws LLRPAccessException {
		try {
			return getReaderInternal(adapterName, readerName).isConnected();
		} catch (RemoteException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		}
	}

	@Override
	public boolean containsReader(String adaptorName, String readerName) throws LLRPAccessException {
		try {
			if (containsAdapter(adaptorName)) {
				return getMgmt().getAdaptor(adaptorName).containsReader(readerName);
			}
			return false;
		} catch (LLRPRuntimeException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		} catch (RemoteException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		}
	}

	@Override
	public List<String> getReaderNames(String adapterName) throws LLRPAccessException {
		List<String> readerNames = new LinkedList<String>();
		try {
			if (getMgmt().containsAdaptor(adapterName)) {
				readerNames.addAll(getMgmt().getAdaptor(adapterName).getReaderNames());
			}
		} catch (LLRPRuntimeException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		} catch (RemoteException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		}
		return readerNames;
	}

	@Override
	public boolean containsAdapter(String adaptorName) throws LLRPAccessException {
		try {
			return getMgmt().containsAdaptor(adaptorName);
		} catch (LLRPRuntimeException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		}
	}

	@Override
	public String defineAdapter(String adaptorNamePreferred, String address) throws LLRPAccessException {
		try {
			return getMgmt().define(adaptorNamePreferred, address);
		} catch (Exception e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		}
	}

	@Override
	public void undefineAdapter(String adapterName) throws LLRPAccessException {
		try {
			getMgmt().undefine(adapterName);
		} catch (LLRPRuntimeException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		}
	}

	@Override
	public List<String> getAdaptorNames() throws LLRPAccessException {
		try {
			return getMgmt().getAdaptorNames();
		} catch (LLRPRuntimeException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		}
	}

	@Override
	public String getDefaultAdaptorName() throws LLRPAccessException {
		return AdaptorManagement.DEFAULT_ADAPTOR_NAME;
	}

	@Override
	public boolean isDefaultAdapter(String adapterName) {
		return AdaptorManagement.DEFAULT_ADAPTOR_NAME.equalsIgnoreCase(adapterName);
	}

	@Override
	public boolean isLocalAdapter(String adapterName) throws LLRPAccessException {
		try {
			return getMgmt().isLocalAdapter(adapterName);
		} catch (LLRPRuntimeException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		}
	}

	@Override
	public void disconnectReaders() {
		getMgmt().disconnectReaders();
	}

	@Override
	public void connectReader(String adapterName, String readerName, boolean clientInitiatedConnection) throws LLRPAccessException {
		try {
			getReaderInternal(adapterName, readerName).connect(clientInitiatedConnection);
		} catch (LLRPRuntimeException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		} catch (RemoteException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		}
	}

	@Override
	public void disconnectReader(String adapterName, String readerName)
			throws LLRPAccessException {
		try {
			getReaderInternal(adapterName, readerName).disconnect();
		} catch (RemoteException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		}
	}

	@Override
	public void shutdown() {
		getMgmt().shutdown();
	}

	@Override
	public void enqueueLLRPMessage(String adapterName, String readerName, LLRPMessage message) throws LLRPAccessException {
		try {
			getMgmt().enqueueLLRPMessage(adapterName, readerName, message);
		} catch (LLRPRuntimeException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		}
	}
	
	@Override
	public void registerPartialHandler(MessageHandler handler, Class<?> clzz) {
		getMgmt().registerPartialHandler(handler, clzz);
	}

	@Override
	public void deregisterPartialHandler(MessageHandler handler, Class<?> clzz) {
		getMgmt().deregisterPartialHandler(handler, clzz);
	}

	@Override
	public void registerFullHandler(MessageHandler handler) {
		getMgmt().registerFullHandler(handler);
	}

	@Override
	public void setExceptionHandler(LLRPExceptionHandler handler) {
		getMgmt().setExceptionHandler(handler);
	}

	@Override
	public void initialize(final String configurationPath) throws LLRPAccessException {

		final Map<String, Object> config = new HashMap<String, Object> ();
		config.put(FileStoreConfiguration.KEY_LOADFILEPATH, configurationPath);
		config.put(FileStoreConfiguration.KEY_STOREFILEPATH, configurationPath);
		
		final String configurationClass = FileStoreConfiguration.class.getCanonicalName();
		final boolean commitChanges = true;
		
		try {
			getMgmt().initialize(config, config, configurationClass, commitChanges, null, null);
		} catch (LLRPRuntimeException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		}
	}
	
	/**
	 * short cut to the adaptor management via delegate.
	 * @return the adaptor management.
	 */
	private AdaptorManagement getMgmt() {
		return delegate.getAdaptorManagement();
	}

	/**
	 * allow to inject a different delegate to the LLRP adaptor management.
	 * @param delegate the new delegate.
	 */
	public void setDelegate(AdaptorManagementDelegate delegate) {
		this.delegate = delegate;
	}

	@Override
	public void undefineReader(String adapterName, String readerName) throws LLRPAccessException {
		try {
			getMgmt().getAdaptor(adapterName).undefine(readerName);
		} catch (LLRPRuntimeException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		} catch (RemoteException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		}
	}

	@Override
	public void defineReader(String adapterName, String readerName, String ip,
			int port, boolean clientInitiated, boolean connectImmediately)
			throws LLRPAccessException {
		try {
			getMgmt().getAdaptor(adapterName).define(readerName, ip, port, clientInitiated, connectImmediately);					
		} catch (LLRPRuntimeException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		} catch (RemoteException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		}
	}

	@Override
	public void connectReader(String adapterName, String readerName) throws LLRPAccessException {
		try {
			org.fosstrak.llrp.adaptor.Reader reader = getReaderInternal(adapterName, readerName);
			reader.connect(reader.isClientInitiated());
		} catch (LLRPRuntimeException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		} catch (RemoteException e) {
			LOG.debug(CAUGHT_EXCEPTION, e);
			throw new LLRPAccessException(e);
		}
	}

}
