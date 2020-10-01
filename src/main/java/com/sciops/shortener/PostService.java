package com.sciops.shortener;

import java.util.Calendar;
import java.util.Random;

import com.sciops.shortener.model.UrlMappingRequest;
import com.sciops.shortener.persistency.UrlMapping;
import com.sciops.shortener.persistency.UrlMappingRepository;

public class PostService {
	private static final int MIN_LENGTH = 5; 

	public static int getMinLength() {
		return MIN_LENGTH;
	}

	public static UrlMapping processNewMapping(UrlMappingRequest request, UrlMappingRepository repo) {
		
		if(!validateRequest(request)) return null;
		if(repo.findByInput(request.getSuggestedInput()) != null) return null;
		UrlMapping mapping;
		if(request.getSuggestedInput() == null || request.getSuggestedInput().length() == 0) {
			mapping = new UrlMapping(newRandomUrl(repo), request.getOutput(), request.getExpiration(), 
									 request.isSingleUse());
		}
		else mapping = new UrlMapping(request.getSuggestedInput(), request.getOutput(), 
												request.getExpiration(), request.isSingleUse());
		if(repo.save(mapping) == null) return null;
		return mapping;
	}

	private static boolean validateRequest(UrlMappingRequest request) {
		
		if(request == null) return false;
		
		if(request.getSuggestedInput() == null) return false;
		if(request.getOutput() == null) return false;
		if(request.getSuggestedInput().length() > 0) {
			if(request.getSuggestedInput().length() < getMinLength()) return false;
		
			for(int i = 0; i < request.getSuggestedInput().length(); i++) {
				char c = request.getSuggestedInput().charAt(i);
				if(!(Character.isDigit(c) || Character.isLetter(c) || c == '-' || c == '_' || c == '~'))
					return false;
			}
		}
		
		for(int i = 0; i < request.getOutput().length(); i++) {
			char c = request.getOutput().charAt(i);
			if(!(Character.isDigit(c) || Character.isLetter(c) || c == '-' || c == '_' || c == '~' || 
				 c == '.' || c == ':' || c == '/' || c == '?' || c == '#' || c == '[' || c == ']' || 
				 c == '@' || c == '!' || c == '$' || c == '&' || c == '\'' || c == '(' || c == ')' || 
				 c == '*' || c == '+' || c == ',' || c == ';' || c == '%' || c == '='))
				return false;
		}
		
		if(request.getExpiration() != 0 && request.getExpiration() < Calendar.getInstance().getTimeInMillis())
			return false;
		
		return true;
	}

	private static String newRandomUrl(UrlMappingRepository repo) {
		String ans = "";
		Random random = new Random();
		do {
			ans += randomIntToChar(random.nextInt(65));
		} while(ans.length() < getMinLength() || repo.findByInput(ans) != null);
		System.out.println(ans);
		return ans;
	}

	private static char randomIntToChar(int i) {
		if(i < 26) return (char) ('A' + i);
		if(i < 52) return (char) ('a' + i - 26);
		if(i < 62) return (char) ('0' + i - 52);
		if(i == 62) return '-';
		if(i == 63) return '_';
		return '~';
	}
}
