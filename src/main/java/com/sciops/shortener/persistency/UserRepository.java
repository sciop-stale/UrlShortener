package com.sciops.shortener.persistency;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
	
	User findByUserName(String userName);
	
	User findById(long id);
	
}