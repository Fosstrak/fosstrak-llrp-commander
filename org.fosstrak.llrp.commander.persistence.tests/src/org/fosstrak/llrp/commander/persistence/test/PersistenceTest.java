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
import java.util.Map;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.fosstrak.llrp.adaptor.exception.LLRPRuntimeException;
import org.fosstrak.llrp.client.LLRPMessageItem;
import org.fosstrak.llrp.client.ROAccessReportsRepository;
import org.fosstrak.llrp.client.Repository;
import org.fosstrak.llrp.client.RepositoryFactory;
import org.fosstrak.llrp.client.repository.sql.roaccess.ROAccessItem;
import org.fosstrak.llrp.commander.llrpaccess.LLRPAccess;
import org.fosstrak.llrp.commander.llrpaccess.impl.LLRPAccessImpl;
import org.fosstrak.llrp.commander.persistence.Persistence;
import org.fosstrak.llrp.commander.persistence.exception.PersistenceException;
import org.fosstrak.llrp.commander.persistence.impl.PersistenceImpl;
import org.fosstrak.llrp.commander.persistence.type.PersistenceDescriptor;
import org.fosstrak.llrp.commander.persistence.type.RepositoryFactoryDelegate;
import org.junit.Test;
import org.llrp.ltk.generated.messages.RO_ACCESS_REPORT;

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
	
	/**
	 * test must fail during invocation of the repository.
	 * @throws PersistenceException
	 */
	@Test(expected = PersistenceException.class)
	public void testVerifyOldRepoNotSameNoRepoNoInput() throws PersistenceException {
		// register the creator and ensure that is is not called.
		CreatorMock creator = new CreatorMock(null);
		Persistence p = newPersistenceImpl();
		((PersistenceImpl)p).setRepositoryFactory(creator);
		try {
			p.test(null);
		} catch (PersistenceException exe) {
			Assert.assertEquals(0, creator.getHits());
			throw exe;
		}
	}
	
	@Test
	public void testVerifyOldRepoNotSameWithValidInput() throws PersistenceException {
		final Repository repository = EasyMock.createMock(Repository.class);
		repository.close();
		EasyMock.expectLastCall();
		
		CreatorMock creator = new CreatorMock(repository);
		
		EasyMock.replay(repository);
		
		Persistence p = newPersistenceImpl();
		((PersistenceImpl)p).setRepository(repository);
		((PersistenceImpl)p).setRepositoryFactory(creator);
		
		PersistenceDescriptor desc = new PersistenceDescriptor(true, true, true, "test", "pass", "jdbc", "abc.def");
		
		Assert.assertTrue(p.test(desc));
		Map<String, String> args = creator.getArgs();
		Assert.assertNotNull(args);		
		Assert.assertEquals("true", args.get(RepositoryFactory.ARG_WIPE_DB));
		Assert.assertEquals("true", args.get(RepositoryFactory.ARG_WIPE_RO_ACCESS_REPORTS_DB));
		Assert.assertEquals("true", args.get(RepositoryFactory.ARG_LOG_RO_ACCESS_REPORT));
		Assert.assertEquals("test", args.get(RepositoryFactory.ARG_USERNAME));
		Assert.assertEquals("pass", args.get(RepositoryFactory.ARG_PASSWRD));
		Assert.assertEquals("jdbc", args.get(RepositoryFactory.ARG_JDBC_STRING));
		Assert.assertEquals("abc.def", args.get(RepositoryFactory.ARG_DB_CLASSNAME));

		EasyMock.verify(repository);
	}
	
	/**
	 * test must fail during invocation of the repository.
	 * @throws PersistenceException
	 */
	@Test
	public void testVerifyOldRepoNotSameButRequestSame() throws PersistenceException {
		Repository repository = EasyMock.createMock(Repository.class);
		PersistenceDescriptor desc = new PersistenceDescriptor(true, true, true, "", "", "", repository.getClass().getName());
		Persistence p = newPersistenceImpl();
		((PersistenceImpl)p).setRepository(repository);
		Assert.assertFalse(p.test(desc));		
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
	public void testGetAllRoAccessReportsNoRepository() throws Exception {
		Persistence p = newPersistenceImpl();
		((PersistenceImpl)p).setRepository(null);
		
		List<ROAccessItem> items = p.getAllRoAccessReports();
		Assert.assertNotNull(items);
	}
	
	@Test
	public void testGetAllRoAccessReportsNoRoRepository() throws Exception {
		Repository repository = EasyMock.createMock(Repository.class);
		EasyMock.expect(repository.getROAccessRepository()).andReturn(null);
		
		EasyMock.replay(repository);
		
		Persistence p = newPersistenceImpl();
		((PersistenceImpl)p).setRepository(repository);
		
		List<ROAccessItem> items = p.getAllRoAccessReports();
		Assert.assertNotNull(items);
		EasyMock.verify(repository);
	}
	
	@Test
	public void testGetAllRoAccessReports() throws Exception {
		ROAccessReportsRepository roAccessReportsRepo = EasyMock.createMock(ROAccessReportsRepository.class);
		EasyMock.expect(roAccessReportsRepo.getAll()).andThrow(new LLRPRuntimeException("Mock triggered exception"));
		
		Repository repository = EasyMock.createMock(Repository.class);
		EasyMock.expect(repository.getROAccessRepository()).andReturn(roAccessReportsRepo);
		
		EasyMock.replay(repository);
		EasyMock.replay(roAccessReportsRepo);	
		
		Persistence p = newPersistenceImpl();
		((PersistenceImpl)p).setRepository(repository);
		
		List<ROAccessItem> items = p.getAllRoAccessReports();
		Assert.assertNotNull(items);
		EasyMock.verify(repository);
		EasyMock.verify(roAccessReportsRepo);
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
	public void testClearRoAccessReports() throws Exception {
		ROAccessReportsRepository roAccessReportsRepo = EasyMock.createMock(ROAccessReportsRepository.class);
		roAccessReportsRepo.clear();
		EasyMock.expectLastCall().andThrow(new LLRPRuntimeException("Mock triggered exception"));
		
		Repository repository = EasyMock.createMock(Repository.class);
		EasyMock.expect(repository.getROAccessRepository()).andReturn(roAccessReportsRepo);
		
		EasyMock.replay(repository);
		EasyMock.replay(roAccessReportsRepo);	
		
		Persistence p = newPersistenceImpl();
		((PersistenceImpl)p).setRepository(repository);
		
		p.clearRoAccessReports();
		EasyMock.verify(repository);
		EasyMock.verify(roAccessReportsRepo);
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
	
	@Test
	public void testRegisterForRoAccessReports() {
		ROAccessReportsRepository roAccessReportsRepo = EasyMock.createMock(ROAccessReportsRepository.class);
		
		Repository repository = EasyMock.createMock(Repository.class);
		EasyMock.expect(repository.getROAccessRepository()).andReturn(roAccessReportsRepo);
		
		LLRPAccess llrpAccess = EasyMock.createMock(LLRPAccess.class);
		llrpAccess.registerPartialHandler(roAccessReportsRepo, RO_ACCESS_REPORT.class);
		EasyMock.expectLastCall();
		
		EasyMock.replay(repository);
		EasyMock.replay(llrpAccess);
		EasyMock.replay(roAccessReportsRepo);	
		
		Persistence p = new PersistenceImpl(llrpAccess);
		((PersistenceImpl)p).setRepository(repository);
		
		p.registerForRoAccessReports();
		EasyMock.verify(repository);
		EasyMock.verify(llrpAccess);
		EasyMock.verify(roAccessReportsRepo);
	}

	
	/**
	 * need this indirection class as the easy mock version in orbit is below 3 (no delegates yet ...).
	 * @author swieland
	 *
	 */
	private class CreatorMock extends RepositoryFactoryDelegate {
		
		private int hit = 0;
		private Repository repository;
		private Map<String, String> args;
		
		public CreatorMock(Repository repository) {
			this.repository = repository;
		}
		
		@Override
		public Repository create(Map<String, String> args) throws PersistenceException {
			this.hit++;
			this.args = args;
			return repository;
		}

		public Map<String, String> getArgs() {
			return args;
		}
		
		public int getHits() {
			return hit;
		}
	}
}
