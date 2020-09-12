package com.sciops.shortener.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sciops.shortener.persistency.UrlMappingRepository;
import com.sciops.shortener.persistency.UserRepository;

@Controller
public class WebController {
	
	@Autowired
	private UserRepository users;
	
	@Autowired
	private UrlMappingRepository urlMappings;
	
	public WebController() {
		
	}
	
	
	
}
