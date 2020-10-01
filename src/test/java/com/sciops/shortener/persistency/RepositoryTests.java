package com.sciops.shortener.persistency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.sciops.shortener.persistency.UrlMapping;
import com.sciops.shortener.persistency.UrlMappingRepository;

@DataJpaTest
public class RepositoryTests {

	@Autowired UrlMappingRepository umr;
	
	@Test
	void repositoryLoadsTest() {
		assertNotNull(umr);
		assertNotNull(umr.count());
		assertEquals(0, umr.count());
	}
	
	@Test
	void mappingCorrectlySavedTest() {
		UrlMapping urlMapping = new UrlMapping("shortUrl", "longUrl", 10, false);
		umr.save(urlMapping);
		assertEquals("shortUrl", umr.findByInput("shortUrl").getInput());
		assertEquals("longUrl", umr.findByInput("shortUrl").getOutput());
		assertEquals(10, umr.findByInput("shortUrl").getExpiration());
		assertEquals(false, umr.findByInput("shortUrl").isSingleUse());
	}
	
	@Test
	void findByInputTest() {
		UrlMapping[] um = new UrlMapping[] {
				new UrlMapping("shortUrl1", "longUrl", 10, false),
				new UrlMapping("shortUrl2", "longUrl", 10, false),
				new UrlMapping("shortUrl3", "longUrl", 12, true),
				new UrlMapping("shortUrl4", "longUrl", 10, false),
				new UrlMapping("shortUrl5", "longUrl", 13, false),
				new UrlMapping("shortUrl6", "longUrl", 10, false),
				new UrlMapping("shortUrl7", "longUrl", 11, true),
				new UrlMapping("shortUrl8", "longUrl", 8, false)
		};
		for(UrlMapping i: um) umr.save(i);
		assertEquals(12, umr.findByInput("shortUrl3").getExpiration());
	}
	
	@Test
	void deleteExpiredTest() {
		List<UrlMapping> um = new ArrayList<UrlMapping>();
		um.add(new UrlMapping("shortUrl1", "longUrl", 10, false));
		um.add(new UrlMapping("shortUrl2", "longUrl", 10, false));
		um.add(new UrlMapping("shortUrl3", "longUrl", 12, true));
		um.add(new UrlMapping("shortUrl4", "longUrl", 10, false));
		um.add(new UrlMapping("shortUrl5", "longUrl", 13, false));
		um.add(new UrlMapping("shortUrl6", "longUrl", 10, false));
		um.add(new UrlMapping("shortUrl7", "longUrl", 11, true));
		um.add(new UrlMapping("shortUrl8", "longUrl", 8, false));
		umr.saveAll(um);
		
		um = umr.findByExpirationLessThanEqualAndArchivedFalse(9);
		assertEquals(1, um.size());
		for(UrlMapping u : um) {
			u.setArchived(true);
			umr.save(u);
		}
		assertEquals(7, umr.countByArchivedFalse());
		um = umr.findByExpirationLessThanEqualAndArchivedFalse(10);
		assertEquals(4, um.size());
		for(UrlMapping u : um) {
			u.setArchived(true);
			umr.save(u);
		}
		assertEquals(3, umr.countByArchivedFalse());
	}
	
}
