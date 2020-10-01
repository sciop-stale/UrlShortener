package com.sciops.shortener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.sciops.shortener.persistency.UrlMapping;
import com.sciops.shortener.persistency.UrlMappingRepository;

@SpringBootTest
public class GetServiceTests {
	
	@Mock
	private UrlMappingRepository repo;
	
	@Mock
	private UrlMapping um;
	
	@Mock
	private UrlMapping um_archived;
	
	@Mock
	private UrlMapping um_expired_but_unchecked;
	
	@Mock
	private UrlMapping um_single_use;

	@BeforeEach
	void flushRepo() {

		MockitoAnnotations.initMocks(this);
		when(repo.findByInput("normal_entry")).thenReturn(um);
		when(repo.findByInput("not_exists")).thenReturn(null);
		when(repo.findByInput("archived")).thenReturn(um_archived);
		when(repo.findByInput("expired_but_unchecked")).thenReturn(um_expired_but_unchecked);
		when(repo.findByInput("single_use")).thenReturn(um_single_use);
		
		when(um.getExpiration()).thenReturn(0L);
		when(um.getInput()).thenReturn("entry_input");
		
		when(um_archived.isArchived()).thenReturn(true);
		
		when(um_expired_but_unchecked.getExpiration()).thenReturn(1L);
		
		when(um_single_use.isSingleUse()).thenReturn(true);
		
	}
	
	@Test
	void noRecordTest() {
		
		assertNull(GetService.processMappingGetRequest("not_exists", repo));
		
	}
	
	@Test
	void archivedTest() {
		
		assertNull(GetService.processMappingGetRequest("archived", repo));
		
	}
	
	@Test
	void singleUseIsReturnedTest() {
		
		assertNotNull(GetService.processMappingGetRequest("single_use", repo));
		
	}
	
	@Test
	void singleUseIsArchivedTest() {
		
		GetService.processMappingGetRequest("single_use", repo);
		
		verify(um_single_use).setArchived(true);
		
	}
	
	@Test
	void expiredIsArchivedTest() {
		
		GetService.processMappingGetRequest("expired_but_unchecked", repo);
		
		verify(um_expired_but_unchecked).setArchived(true);
		
	}
	
	@Test
	void expiredIsNotReturnedTest() {
		
		assertNull(GetService.processMappingGetRequest("expired_but_unchecked", repo));
		
	}
	
	@Test
	void correctGetBindingTest() {
		assertEquals("entry_input", GetService.processMappingGetRequest("normal_entry", repo).getInput());
	}
	
	//TODO test the cache is working
	
}
