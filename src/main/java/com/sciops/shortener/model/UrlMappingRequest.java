package com.sciops.shortener.model;

public class UrlMappingRequest {
	
	private String suggestedInput;
	private final String output;
	private final long expiration;
	private final boolean singleUse;
	
	public UrlMappingRequest(String suggestedInput, String output, long expiration, boolean singleUse) {
		this.suggestedInput = suggestedInput;
		this.output = output;
		this.expiration = expiration;
		this.singleUse = singleUse;
	}
	
	public void setSuggestedInput(String suggestedInput) {
		this.suggestedInput = suggestedInput;
	}

	public String getSuggestedInput() {
		return suggestedInput;
	}

	public String getOutput() {
		return output;
	}

	public long getExpiration() {
		return expiration;
	}

	public boolean isSingleUse() {
		return singleUse;
	}

	@Override
	public String toString() {
		return "UrlMappingRequest [suggestedInput=" + suggestedInput + ", output=" + output + ", expiration="
				+ expiration + ", singleUse=" + singleUse + "]";
	}
	
}
