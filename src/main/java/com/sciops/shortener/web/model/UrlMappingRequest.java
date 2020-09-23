package com.sciops.shortener.web.model;

public class UrlMappingRequest {
	
	private String suggestedKey;
	private final String value;
	private final long expiration;
	private final boolean singleUse;
	
	public UrlMappingRequest(String suggestedKey, String value, long expiration, boolean singleUse) {
		this.suggestedKey = suggestedKey;
		this.value = value;
		this.expiration = expiration;
		this.singleUse = singleUse;
	}
	
	public void setSuggestedKey(String suggestedKey) {
		this.suggestedKey = suggestedKey;
	}

	public String getSuggestedKey() {
		return suggestedKey;
	}

	public String getValue() {
		return value;
	}

	public long getExpiration() {
		return expiration;
	}

	public boolean isSingleUse() {
		return singleUse;
	}
	
}
