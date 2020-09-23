package com.sciops.shortener.web;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.sciops.shortener.GetService;
import com.sciops.shortener.PostService;
import com.sciops.shortener.persistency.UrlMapping;
import com.sciops.shortener.persistency.UrlMappingRepository;
import com.sciops.shortener.web.model.UrlMappingRequest;

@Controller
public class WebController {
	
	@Autowired
	private UrlMappingRepository urlMappings;
	
	@Autowired
	private Environment environment;
	
	public WebController() {
		
	}
	
	@GetMapping("/")
	public String getMain(Model model) {
		
		return "main";
	}
	
	@PostMapping("/")
	public String newMapping(Model model, HttpServletRequest request) throws UnknownHostException {
		
		UrlMappingRequest req = new UrlMappingRequest(request.getParameter("suggestedKey"),
													  request.getParameter("value"),
													  0, // TODO set this
													  request.getParameter("singleUse") != null);
		
		UrlMapping mapping = PostService.processNewBinding(req, urlMappings);
		
		String postMessage = "";
		if(mapping != null) {
			String ip = InetAddress.getLocalHost().getHostAddress();
			String port = environment.getProperty("local.server.port");
			if(!port.equals("80")) ip += ":" + port;
			
			postMessage = "New link created for: " + mapping.getOutput();
			model.addAttribute("link", ip + "/" + mapping.getInput());
		}
		else postMessage = "Unable to do it!";
		
		model.addAttribute("postMessage", postMessage);
		return "main";
	}
	
	@GetMapping("/{id}") // TODO exclude style.css, favicon.ico, ???
	public void restGetRedirectCoordinates(HttpServletResponse response, @PathVariable("id") String id) {
		
		UrlMapping mapping = GetService.processGetBinding(id, urlMappings);
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
			try {
				response.sendRedirect(url.toString());
			
				// TODO status 301
				
			} catch (IOException e) {
				try {
					response.sendError(500);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
}
