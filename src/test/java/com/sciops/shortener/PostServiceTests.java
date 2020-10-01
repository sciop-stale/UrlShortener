package com.sciops.shortener;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import org.springframework.boot.test.context.SpringBootTest;

import com.sciops.shortener.model.UrlMappingRequest;
import com.sciops.shortener.persistency.UrlMapping;
import com.sciops.shortener.persistency.UrlMappingRepository;

@SpringBootTest public class PostServiceTests {
	
	@Mock private UrlMappingRepository repo;

	@Mock private UrlMappingRequest request_too_short;
	@Mock private UrlMappingRequest request_empty_input;
	@Mock private UrlMappingRequest request_incorrect_input;
	@Mock private UrlMappingRequest request_incorrect_output;
	@Mock private UrlMappingRequest request_expired;
	@Mock private UrlMappingRequest request_correct_with_input_that_does_not_exist;
	@Mock private UrlMappingRequest request_with_input_that_exists;
	@Mock private UrlMappingRequest request_correct_with_empty_input;
	
	@Mock private UrlMapping mapping_that_exists;
	
	@BeforeEach void flushRepo() {

		MockitoAnnotations.initMocks(this);
		
		when(request_too_short.getSuggestedInput()).thenReturn("1234");
		
		when(request_empty_input.getSuggestedInput()).thenReturn(null);
		
		when(request_incorrect_input.getSuggestedInput()).thenReturn("12/456");
		
		when(request_incorrect_output.getSuggestedInput()).thenReturn("12345");
		when(request_incorrect_output.getOutput()).thenReturn("12¬çñ´456");
		
		when(request_expired.getExpiration()).thenReturn(1L);
		
		when(request_correct_with_input_that_does_not_exist.getSuggestedInput()).thenReturn("input_with_no_presence_in_db");
		when(request_correct_with_input_that_does_not_exist.getOutput()).thenReturn("");
		
		when(request_with_input_that_exists.getSuggestedInput()).thenReturn("input_with_presence_in_db");
		when(request_with_input_that_exists.getOutput()).thenReturn("");
		
		when(request_correct_with_empty_input.getSuggestedInput()).thenReturn("");
		when(request_correct_with_empty_input.getOutput()).thenReturn("");
		
		when(repo.findByInput(anyString())).thenReturn(null);
		when(repo.findByInput("input_with_presence_in_db")).thenReturn(mapping_that_exists);
		when(repo.findByInput("input_with_no_presence_in_db")).thenReturn(null);
		when(repo.save(any())).thenReturn(mapping_that_exists);
		
		
	}
	
	@Test void validateRequestNullTest() {
		UrlMappingRequest request = null;
		assertNull(PostService.processNewMapping(request, repo));
		verifyNoInteractions(repo);
	}
	
	@Test void validateRequestInputTooShortTest() {
		
		assertNull(PostService.processNewMapping(request_too_short, repo));
		verifyNoInteractions(repo);
		
	}
	
	@Test void validateRequestInputWithDisallowedCharactersTest() {
		
		assertNull(PostService.processNewMapping(request_incorrect_input, repo));
		verifyNoInteractions(repo);
		
	}
	
	@Test void validateRequestOutputWithDisallowedCharactersTest() {
		
		assertNull(PostService.processNewMapping(request_incorrect_output, repo));
		verifyNoInteractions(repo);
		
	}
	
	@Test void validateRequestExpired() {
		
		assertNull(PostService.processNewMapping(request_expired, repo));
		verifyNoInteractions(repo);
		
	}
	
	@Test void processNewBindingDuplicateRequestTest() {
		
		assertNull(PostService.processNewMapping(request_with_input_that_exists, repo));
		verify(repo).findByInput(any());
		verify(repo, never()).save(any());
		
	}
	
	@Test void correctRequestTest() {
		
		assertNotNull(PostService.processNewMapping(request_correct_with_input_that_does_not_exist, repo));
		verify(repo).findByInput(any());
		verify(repo).save(any());
		
	}
	
	@Test void correctRequestWithEmptySuggestionTest() {
		
		assertNotNull(PostService.processNewMapping(request_correct_with_empty_input, repo));
		verify(repo, atLeastOnce()).findByInput(any());
		verify(repo).save(any());
		
	}
	
}
