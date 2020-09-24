package com.sciops.shortener;

import java.util.Calendar;

import org.springframework.cache.annotation.Cacheable;

import com.sciops.shortener.persistency.UrlMapping;
import com.sciops.shortener.persistency.UrlMappingRepository;

public class GetService {
	
	@Cacheable(value="urlmappings", unless="(#result != null) && (#result.isArchived() == true)")
	public static UrlMapping processMappingGetRequest(String request, UrlMappingRepository repo) {
		
		UrlMapping ans = repo.findByInput(request);
		
		if(ans == null) return null;
		
		if(ans.isArchived()) return null;
		
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
