package com.sciops.shortener;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sciops.shortener.persistency.UrlMappingRepository;
import com.sciops.shortener.persistency.UserRepository;

@Controller
@RequestMapping("/*")
public class UrlShortenerController {
	
	private final UrlMappingRepository umr;
	private final UserRepository ur;
	
	public UrlShortenerController(UrlMappingRepository umr, UserRepository ur) {
		this.umr = umr;
		this.ur = ur;
	}

	@GetMapping("/")
	public void MainPage() {
		
	}
	
	@PostMapping("/")
	public void PostMapping() {
		
	}
	
	@GetMapping("/signin")
	public void Getsignin() {
		
	}
	
	@PostMapping("/signin")
	public void Signin() {
		
	}
	
	@GetMapping("/login")
	public void GetLogin() {
		
	}
	
	@PostMapping("/login")
	public void Login() {
		
	}
	
	@GetMapping("/logout")
	public void GetLogout() {
		
	}
	
	@PostMapping("/logout")
	public void Logout() {
		
	}
	
	@GetMapping("/${id}")
	public void Redirect() {
		
	}
}
