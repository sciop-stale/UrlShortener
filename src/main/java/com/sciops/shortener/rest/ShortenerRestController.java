package com.sciops.shortener.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sciops.shortener.GetService;
import com.sciops.shortener.PostService;
import com.sciops.shortener.model.UrlMappingRequest;
import com.sciops.shortener.persistency.UrlMapping;
import com.sciops.shortener.persistency.UrlMappingRepository;

@RestController
public class ShortenerRestController {
	
	@Autowired
	private UrlMappingRepository urlMappings;
	
	public ShortenerRestController() {
		
	}
	
	@PostMapping("/rest")
	public UrlMapping restPostRedirectCoordinates(@RequestParam(value="suggestedKey") String suggestedKey,
												  @RequestParam(value="value") String value,
												  @RequestParam(value="expiration") long expiration,
												  @RequestParam(value="singleUse") boolean singleUse) {
		
		UrlMappingRequest request = new UrlMappingRequest(suggestedKey, value, expiration, singleUse);
		return PostService.processNewBinding(request, urlMappings);
		
	}
	
	@GetMapping("/rest/{id}")
	public UrlMapping redirect(@PathVariable("id") String id) {
		return GetService.processGetBinding(id, urlMappings);
	}
}
