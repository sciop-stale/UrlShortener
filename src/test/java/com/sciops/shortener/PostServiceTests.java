package com.sciops.shortener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sciops.shortener.model.UrlMappingRequest;
import com.sciops.shortener.persistency.UrlMapping;
import com.sciops.shortener.persistency.UrlMappingRepository;

@SpringBootTest
public class PostServiceTests {
	
	@Autowired
	private UrlMappingRepository repo;
	
	@BeforeEach
	void flushRepo() {

		repo.deleteAll();
		
	}

	@Test
	void intToCharTest() {
		assertEquals('A', PostService.randomIntToChar(0));
		assertEquals('Z', PostService.randomIntToChar(25));
		assertEquals('a', PostService.randomIntToChar(26));
		assertEquals('z', PostService.randomIntToChar(51));
		assertEquals('0', PostService.randomIntToChar(52));
		assertEquals('9', PostService.randomIntToChar(61));
		assertEquals('-', PostService.randomIntToChar(62));
		assertEquals('_', PostService.randomIntToChar(63));
		assertEquals('~', PostService.randomIntToChar(64));
	}
	
	@Test
	void newRandomUrlTest() {
		
		Set<String> set = new HashSet<>();
		for(int i = 0; i < 1000; i++) {
			String aux = PostService.newRandomUrl(repo);
			assertFalse(aux.length() < PostService.getMinLength());
			assertFalse(set.contains(aux));

			UrlMapping mapping = new UrlMapping(aux, "", 0, false);
			repo.save(mapping);
		}
	}
	
	@Test
	void validateRequestNullTest() {
		UrlMappingRequest request = null;
		assertFalse(PostService.validateRequest(request));
	}
	
	@Test
	void validateRequestInputTooShortTest() {
		
		UrlMappingRequest request = new UrlMappingRequest("aaaa", "a", 0, false);
		assertFalse(PostService.validateRequest(request));
		
		request = new UrlMappingRequest("", "a", 0, false);
		assertFalse(PostService.validateRequest(request));
		
	}
	
	@Test
	void validateRequestInputWithDisallowedCharactersTest() {
		
		UrlMappingRequest request = new UrlMappingRequest("a/aaa", "a", 0, false);
		assertFalse(PostService.validateRequest(request));
		
		request = new UrlMappingRequest("aaa+a", "a", 0, false);
		assertFalse(PostService.validateRequest(request));
		
		request = new UrlMappingRequest("aaaa\\", "a", 0, false);
		assertFalse(PostService.validateRequest(request));
		
		request = new UrlMappingRequest("?aaaa", "a", 0, false);
		assertFalse(PostService.validateRequest(request));
		
	}
	
	@Test
	void validateRequestOutputWithDisallowedCharactersTest() {
		
		UrlMappingRequest request = new UrlMappingRequest("aaaaa", "a/aaa", 0, false);
		assertTrue(PostService.validateRequest(request));
		
		request = new UrlMappingRequest("aaaaa", "aaa+a", 0, false);
		assertTrue(PostService.validateRequest(request));
		
		request = new UrlMappingRequest("aaaaa", "aaaa\'", 0, false);
		assertTrue(PostService.validateRequest(request));
		
		request = new UrlMappingRequest("aaaaa", "?aaaa", 0, false);
		assertTrue(PostService.validateRequest(request));
		
		request = new UrlMappingRequest("aaaaa", "aaaa\\", 0, false);
		assertFalse(PostService.validateRequest(request));
		
	}
	
	@Test
	void validateRequestExpired() {
		long time = Calendar.getInstance().getTimeInMillis();
		try {
			Thread.sleep(1000, 0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		UrlMappingRequest request = new UrlMappingRequest("aaaaa", "aaaaa", time, false);
		assertFalse(PostService.validateRequest(request));
	}
	
	@Test
	void validateRequestCorrectTest() {
		
		UrlMappingRequest request = new UrlMappingRequest("aaaaa", "a", 0, false);
		assertTrue(PostService.validateRequest(request));
		
		request = new UrlMappingRequest("aaaaa", "a", Calendar.getInstance().getTimeInMillis() + 1000, false);
		assertTrue(PostService.validateRequest(request));
		
	}
	
	@Test
	void processNewBindingBadRequestTest() {
		
		UrlMappingRequest request = new UrlMappingRequest("aaa*a", "a", 0, false);
		assertNull(PostService.processNewMapping(request, repo));
		
	}
	
	@Test
	void processNewBindingCorrectRequestTest() {
		
		UrlMappingRequest request = new UrlMappingRequest("google", "http://www.google.com", 0, false);
		UrlMapping mapping = PostService.processNewMapping(request, repo);
		assertNotNull(mapping);
		
	}
	
	@Test
	void processNewBindingDuplicateRequestTest() {
		
		UrlMappingRequest request = new UrlMappingRequest("google", "http://www.google.com", 0, false);
		UrlMapping mapping = PostService.processNewMapping(request, repo);
		
		mapping = PostService.processNewMapping(request, repo);
		assertNull(mapping);
		
	}
	
	@Test
	void processNewBindingDifferentBindingsEmptySuggestionTest() {
		
		UrlMappingRequest request = new UrlMappingRequest(null, "http://www.google.com", 0, false);
		UrlMapping mapping = PostService.processNewMapping(request, repo);
		assertNotNull(mapping);
		String first = mapping.getInput();
		mapping = PostService.processNewMapping(request, repo);
		assertNotNull(mapping);
		assertNotEquals(first, mapping.getInput());
		
	}
	
}
