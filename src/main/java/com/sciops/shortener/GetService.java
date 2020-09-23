package com.sciops.shortener;

import java.util.Calendar;

import com.sciops.shortener.persistency.UrlMapping;
import com.sciops.shortener.persistency.UrlMappingRepository;

public class GetService {
	
	public static UrlMapping processGetBinding(String request, UrlMappingRepository repo) {
		
		UrlMapping ans = repo.findByInput(request);
		
		if(ans == null) return null;
		
		if(ans.isArchived()) return null;  //TODO should it return 302 instead of 301???
		
		if(ans.getExpiration() != 0 &&
		   ans.getExpiration() < Calendar.getInstance().getTimeInMillis()) { 
			ans.setArchived(true);
			repo.save(ans);
			return null;
		}
		
		if(ans.isSingleUse()) {
			ans.setArchived(true);
			repo.save(ans);
		}
		
		return ans;
	}
}
