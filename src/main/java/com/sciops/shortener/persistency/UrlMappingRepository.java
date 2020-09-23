package com.sciops.shortener.persistency;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface UrlMappingRepository extends PagingAndSortingRepository<UrlMapping, Long> {

	UrlMapping findById(long id);
	
	UrlMapping findByInput(String input);
	
	long deleteByExpirationLessThanEqual(long expiration);
	
	List<UrlMapping> removeByExpirationLessThanEqual(long expiration);

}
