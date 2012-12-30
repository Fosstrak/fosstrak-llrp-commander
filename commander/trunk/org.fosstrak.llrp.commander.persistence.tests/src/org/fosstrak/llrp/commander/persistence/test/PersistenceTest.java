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

package org.fosstrak.llrp.commander.persistence.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.fosstrak.llrp.client.LLRPMessageItem;
import org.fosstrak.llrp.client.ROAccessReportsRepository;
import org.fosstrak.llrp.client.Repository;
import org.fosstrak.llrp.commander.llrpaccess.impl.LLRPAccessImpl;
import org.fosstrak.llrp.commander.persistence.Persistence;
import org.fosstrak.llrp.commander.persistence.impl.PersistenceImpl;
import org.junit.Test;

/**
 * unit test the persistence layer.
 * @author swieland
 *
 */
public class PersistenceTest {

	private static final String ADAPTER_NAME = "adapterName";
	private static final String READER_NAME = "readerName";
	private static final String ID = "id";
	
	private Persistence newPersistenceImpl() {
		return new PersistenceImpl(new LLRPAccessImpl());
	}
	
	@Test
	public void testSupportsRoAccessRepository() {
		ROAccessReportsRepository roAccessRepo = EasyMock.createMock(ROAccessReportsRepository.class);
		Repository repository = EasyMock.createMock(Repository.class);
		EasyMock.expect(repository.getROAccessRepository()).andReturn(roAccessRepo);
		EasyMock.replay(repository);
		
		Persistence p = newPersistenceImpl();
		((PersistenceImpl)p).setRepository(repository);

		Assert.assertTrue(p.supportsRoAccessRepository());
		EasyMock.verify(repository);
	}
	
	@Test
	public void testSupportsRoAccessRepositoryNoROAccessRepo() {
		Repository repository = EasyMock.createMock(Repository.class);
		EasyMock.expect(repository.getROAccessRepository()).andReturn(null);
		EasyMock.replay(repository);
		
		Persistence p = newPersistenceImpl();
		((PersistenceImpl)p).setRepository(repository);
		
		Assert.assertFalse(p.supportsRoAccessRepository());
		EasyMock.verify(repository);
	}
	
	@Test
	public void testSupportsRoAccessRepositoryNoRepo() {
		Persistence p = newPersistenceImpl();
		Assert.assertFalse(p.supportsRoAccessRepository());
	}

	/**
	 * this test first puts a null value which must not be propagated to the test repository. 
	 * that for the repository expects exactly one call instead of two.
	 */
	@Test
	public void testPut() {
		final LLRPMessageItem llrpMessageItem = new LLRPMessageItem();
		Repository repository = EasyMock.createMock(Repository.class);
		repository.put(llrpMessageItem);
		EasyMock.expectLastCall();
		EasyMock.replay(repository);
		
		Persistence p = newPersistenceImpl();
		((PersistenceImpl)p).setRepository(repository);
		
		p.put(null);
		p.put(llrpMessageItem);
		EasyMock.verify(repository);
	}
	
	@Test
	public void testGetByIdNull() {
		Persistence p = newPersistenceImpl();
		LLRPMessageItem item = p.get(null);
		Assert.assertNull(item);
	}
	
	@Test
	public void testGetById() {
		Repository repository = EasyMock.createMock(Repository.class);
		EasyMock.expect(repository.get(ID)).andReturn(new LLRPMessageItem());
		EasyMock.replay(repository);
		
		Persistence p = newPersistenceImpl();
		((PersistenceImpl)p).setRepository(repository);
		
		LLRPMessageItem item = p.get(ID);
		Assert.assertNotNull(item);
		EasyMock.verify(repository);
	}
	
	@Test
	public void testGet() {
		Repository repository = EasyMock.createMock(Repository.class);
		EasyMock.expect(repository.get(ADAPTER_NAME, READER_NAME, 10, true)).andReturn(new ArrayList<LLRPMessageItem> ());
		EasyMock.replay(repository);
		
		Persistence p = newPersistenceImpl();
		((PersistenceImpl)p).setRepository(repository);
		
		List<LLRPMessageItem> items = p.get(ADAPTER_NAME, READER_NAME, 10, true);
		Assert.assertNotNull(items);
		EasyMock.verify(repository);
	}
	
	@Test
	public void testGetRetrieveAll() {
		Repository repository = EasyMock.createMock(Repository.class);
		EasyMock.expect(repository.get(ADAPTER_NAME, READER_NAME, Repository.RETRIEVE_ALL, true)).andReturn(new ArrayList<LLRPMessageItem> ());
		EasyMock.replay(repository);
		
		Persistence p = newPersistenceImpl();
		((PersistenceImpl)p).setRepository(repository);
		
		List<LLRPMessageItem> items = p.get(ADAPTER_NAME, READER_NAME, Persistence.RETRIEVE_ALL, true);
		Assert.assertNotNull(items);
		EasyMock.verify(repository);
	}
	
	@Test
	public void testIsCleanNoRepo() {
		Persistence p = newPersistenceImpl();
		Assert.assertFalse(p.isClean());
	}
	
	@Test
	public void testIsClean() {
		Repository repository = EasyMock.createMock(Repository.class);
		
		EasyMock.expect(repository.isHealth()).andReturn(false);
		
		EasyMock.replay(repository);
		
		Persistence p = newPersistenceImpl();
		((PersistenceImpl)p).setRepository(repository);
		Assert.assertFalse(p.isClean());
		
		EasyMock.verify(repository);
	}
	
	@Test
	public void testCount() {
		Repository repository = EasyMock.createMock(Repository.class);
		
		EasyMock.expect(repository.count(ADAPTER_NAME, READER_NAME)).andReturn(10);
		
		EasyMock.replay(repository);
		
		Persistence p = newPersistenceImpl();
		((PersistenceImpl)p).setRepository(repository);
		Assert.assertEquals(10, p.count(ADAPTER_NAME, READER_NAME));
		
		EasyMock.verify(repository);
	}
	
	@Test
	public void testClearAll() {
		Repository repository = EasyMock.createMock(Repository.class);
		repository.clearAll();
		EasyMock.expectLastCall();
		EasyMock.replay(repository);
		
		Persistence p = newPersistenceImpl();
		((PersistenceImpl)p).setRepository(repository);
		
		p.clearAll();
		EasyMock.verify(repository);
	}
	
	@Test
	public void testClearAdapter() {
		Repository repository = EasyMock.createMock(Repository.class);
		repository.clearAdapter(ADAPTER_NAME);
		EasyMock.expectLastCall();
		EasyMock.replay(repository);
		
		Persistence p = newPersistenceImpl();
		((PersistenceImpl)p).setRepository(repository);
		
		p.clearAdapter(ADAPTER_NAME);
		EasyMock.verify(repository);
	}
	
	@Test
	public void testClearReader() {
		Repository repository = EasyMock.createMock(Repository.class);
		repository.clearReader(ADAPTER_NAME, READER_NAME);
		EasyMock.expectLastCall();
		EasyMock.replay(repository);
		
		Persistence p = newPersistenceImpl();
		((PersistenceImpl)p).setRepository(repository);
		
		p.clearReader(ADAPTER_NAME, READER_NAME);
		EasyMock.verify(repository);
	}
	
	@Test
	public void testClose() {
		Repository repository = EasyMock.createMock(Repository.class);
		repository.close();
		EasyMock.expectLastCall();
		EasyMock.replay(repository);
		
		Persistence p = newPersistenceImpl();
		((PersistenceImpl)p).setRepository(repository);
		
		p.close();
		EasyMock.verify(repository);
	}
}
