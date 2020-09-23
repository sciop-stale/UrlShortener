package com.sciops.shortener.persistency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.sciops.shortener.persistency.UrlMapping;
import com.sciops.shortener.persistency.UrlMappingRepository;

@DataJpaTest
public class RepositoryTests {

	@Autowired UrlMappingRepository umr;
	
	@Test
	void RepositoryLoadsTest() {
		assertNotNull(umr);
		assertNotNull(umr.count());
		assertEquals(0, umr.count());
	}
	
	@Test
	void MappingCorrectlySavedTest() {
		UrlMapping urlMapping = new UrlMapping("shortUrl", "longUrl", 10, false);
		umr.save(urlMapping);
		assertEquals("shortUrl", umr.findById(urlMapping.getId()).getInput());
		assertEquals("longUrl", umr.findById(urlMapping.getId()).getOutput());
		assertEquals(10, umr.findById(urlMapping.getId()).getExpiration());
		assertEquals(false, umr.findById(urlMapping.getId()).isSingleUse());
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
		assertEquals(1, umr.deleteByExpirationLessThanEqual(9));
		assertEquals(7, umr.count());
		assertEquals(4, umr.deleteByExpirationLessThanEqual(10));
		assertEquals(3, umr.count());
	}
	
}
