package com.sciops.shortener.web.model;

public class UrlMappingRequest {
	
	private final String suggestedKey;
	private final String value;
	private final byte[] sessionId;
	private final long expiration;
	private final boolean singleUse;
	
	public UrlMappingRequest(String suggestedKey, String value, byte[] sessionId, long expiration, boolean singleUse) {
		this.suggestedKey = suggestedKey;
		this.value = value;
		this.sessionId = sessionId;
		this.expiration = expiration;
		this.singleUse = singleUse;
	}

	public String getSuggestedKey() {
		return suggestedKey;
	}

	public String getValue() {
		return value;
	}

	public byte[] getSessionId() {
		return sessionId;
	}

	public long getExpiration() {
		return expiration;
	}

	public boolean isSingleUse() {
		return singleUse;
	}
	
}
