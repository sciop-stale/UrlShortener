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
	private UrlMappingRepository repo;
	
	public ShortenerRestController() {
		
	}
	
	@PostMapping("/rest")
	public UrlMapping restNewMappingRequest(@RequestParam(value="suggestedInput") String suggestedInput,
											@RequestParam(value="output") String output,
											@RequestParam(value="expiration") long expiration,
											@RequestParam(value="singleUse") boolean singleUse) {
		
		UrlMappingRequest request = new UrlMappingRequest(suggestedInput, output, expiration, singleUse);
		return PostService.processNewMapping(request, repo);
		
	}
	
	@GetMapping("/rest/{id}")
	public UrlMapping restGetMapping(@PathVariable("id") String id) {
		return GetService.processMappingGetRequest(id, repo);
	}
}
