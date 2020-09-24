package com.sciops.shortener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sciops.shortener.model.UrlMappingRequest;
import com.sciops.shortener.persistency.UrlMapping;
import com.sciops.shortener.persistency.UrlMappingRepository;

@SpringBootTest
public class GetServiceTests {
	
	@Autowired
	private UrlMappingRepository repo;

	@BeforeEach
	void flushRepo() {

		repo.deleteAll();
		
	}
	
	@Test
	void noRecordTest() {
		assertNull(GetService.processMappingGetRequest("whatever", repo));
	}
	
	@Test
	void archivedTest() {
		UrlMapping um = PostService.processNewMapping(new UrlMappingRequest("archivedTest", "google.com", 0, true), repo);
		um.setArchived(true);
		repo.save(um);
		
		assertNull(GetService.processMappingGetRequest("archivedTest", repo));
	}
	
	@Test
	void singleUseIsArchivedTest() {
		PostService.processNewMapping(new UrlMappingRequest("singleUseIsArchivedTest", "google.com", 0, true), repo);
		GetService.processMappingGetRequest("singleUseIsArchivedTest", repo);
		
		assertNull(GetService.processMappingGetRequest("singleUseIsArchivedTest", repo));
	}
	
	@Test
	void expiredIsArchivedTest() {
		UrlMappingRequest umr =  new UrlMappingRequest("expiredIsArchivedTest", "google.com",
									Calendar.getInstance().getTimeInMillis() + 200, false);
		PostService.processNewMapping(umr, repo);
		assertNotNull(GetService.processMappingGetRequest("expiredIsArchivedTest", repo));
		try {
			Thread.sleep(201);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertNull(GetService.processMappingGetRequest("expiredIsArchivedTest", repo));
	}
	
	@Test
	void correctGetBindingTest() {
		long exp = Calendar.getInstance().getTimeInMillis() + 1000;
		PostService.processNewMapping(new UrlMappingRequest("correctGetBindingTest", "google.com", exp, false), repo);
		UrlMapping um = GetService.processMappingGetRequest("correctGetBindingTest", repo);
		assertEquals("correctGetBindingTest", um.getInput());
		assertEquals("google.com", um.getOutput());
		assertEquals(exp, um.getExpiration());
		assertEquals(false, um.isSingleUse());
		assertEquals(false, um.isArchived());
	}
	
	@Test
	void cachePseudoTest() {
		List<String> q = new ArrayList<String>();
		List<String> names = new ArrayList<String>(); 
		long[][] times = new long[2][2];
		for(int i = 0; i < 1100; i++) {
			if(i == 0 || (i >= 100 && i < 600)) {
				names.add(PostService.processNewMapping(new UrlMappingRequest(null, "w/e", 0, true), repo).getInput());
				GetService.processMappingGetRequest(names.get(names.size() - 1), repo);
			}
			else names.add(PostService.processNewMapping(new UrlMappingRequest(null, "w/e", 0, false), repo).getInput());
		}
		
		// "No caching" ~~> all queries are different
		times[0][0] = Calendar.getInstance().getTimeInMillis();
		for(int i = 100; i < 1100; i++) {
			GetService.processMappingGetRequest(names.get(i), repo);
		}
		times[0][1] = Calendar.getInstance().getTimeInMillis();
		
		// "Caching" with 98/2
		Random random = new Random();
		for(int i = 0; i < 1000; i++) {
			int aux = random.nextInt(100);
			if(aux == 0) q.add(names.get(random.nextInt(98) + 2));
			else q.add(names.get(random.nextInt(2)));
		}
		times[1][0] = Calendar.getInstance().getTimeInMillis();
		for(String s : q) {
			GetService.processMappingGetRequest(s, repo);
		}
		times[1][1] = Calendar.getInstance().getTimeInMillis();
		long f = times[0][1] - times[0][0];
		long s = times[1][1] - times[1][0];
		assertTrue(s < f);
		// System.out.println(f + " vs " + s);
	}
	
}
