package com.sciops.shortener.web;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sciops.shortener.GetService;
import com.sciops.shortener.PostService;
import com.sciops.shortener.model.UrlMappingRequest;
import com.sciops.shortener.persistency.UrlMapping;
import com.sciops.shortener.persistency.UrlMappingRepository;

@Controller
public class ShortenerWebController {
	
	@Autowired
	private UrlMappingRepository repo;
	
	@Autowired
	private Environment environment;
	
	public ShortenerWebController() {
		
	}
	
	@GetMapping("/")
	public String getMain(Model model) {
		
		return "main";
	}
	
	@PostMapping("/")
	public String newMapping(Model model, 
							 @RequestParam(value="suggestedInput", defaultValue="") String suggestedInput,
							 @RequestParam(value="output", defaultValue="") String output,
							 @RequestParam(value="expiration", defaultValue="0") long expiration,
							 @RequestParam(value="singleUse", defaultValue="false") boolean singleUse) 
									 throws UnknownHostException {
		
		UrlMappingRequest req = new UrlMappingRequest(suggestedInput, output, expiration, singleUse);
		
		UrlMapping mapping = PostService.processNewMapping(req, repo);
		
		String postMessage = "";
		if(mapping != null) {
			String ip = InetAddress.getLocalHost().getHostAddress();
			String port = environment.getProperty("local.server.port");
			if(!port.equals("80")) ip += ":" + port;
			
			postMessage = "New link created for: " + mapping.getOutput();
			model.addAttribute("link", ip + "/" + mapping.getInput());
		}
		else postMessage = "Rejected";
		
		model.addAttribute("postMessage", postMessage);
		return "main";
	}
	
	@GetMapping("/{id}")
	public void getMapping(HttpServletResponse response, @PathVariable("id") String id) {
		
		UrlMapping mapping = GetService.processMappingGetRequest(id, repo);
		URL url = null;
		if(mapping != null)
			try {
				url = new URL(mapping.getOutput());
			} catch (MalformedURLException e2) {
				try {
					url = new URL("http://" + mapping.getOutput());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		if(url == null) {
			try {
				response.sendRedirect("/");
			} catch (IOException e) {
				try {
					response.sendError(500);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			if(mapping.isSingleUse()) response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			else response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
			response.setHeader("location", url.toString());
			response.setHeader("Connection", "close");
		}
	}
	
}
