package com.sciops.shortener;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import com.sciops.shortener.persistency.UrlMappingRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ShortenerApplicationTests {
	
	@LocalServerPort
	private int port;

	@Autowired
	UrlMappingRepository repo;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	void contextLoads() {
		assertNotNull(repo);
	}
	
	@Test
	void getMainTest() {
		assertTrue(restTemplate.getForObject("http://localhost:" + port + "/", String.class).contains("URL to be shortened"));
	}
	
	//TODO
	
	
}
