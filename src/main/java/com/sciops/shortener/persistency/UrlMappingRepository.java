package com.sciops.shortener.persistency;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface UrlMappingRepository extends PagingAndSortingRepository<UrlMapping, Long> {

	UrlMapping findByInput(String input);
	
	List<UrlMapping> findByExpirationLessThanEqualAndArchivedFalse(long expiration);
	
	int countByArchivedFalse();

}
