package com.sciops.shortener.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.sciops.shortener.persistency.UrlMappingRepository;

@Controller
public class RestController {
	
	@Autowired
	private UrlMappingRepository urlMappings;
	
	public RestController() {
		
	}
	
	@PostMapping("/rest")
	public String restPostRedirectCoordinates() {
		
		//TODO
		
		return "";
	}
	
	@GetMapping("/rest/{id}")
	public void redirect(HttpServletRequest request, HttpServletResponse response) {
		
		//TODO
		
	}
}
