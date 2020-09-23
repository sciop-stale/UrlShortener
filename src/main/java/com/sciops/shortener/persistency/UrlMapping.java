package com.sciops.shortener.persistency;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UrlMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long urlMappingId;
	
	private String input;
	
	private String output;
	private long expiration;
	
	private boolean singleUse;
	private boolean archived;
	
	protected UrlMapping() {
		
	}

	public UrlMapping(String input, String output, long expiration, boolean singleUse) {
		this.input = input;
		this.output = output;
		this.expiration = expiration;
		this.singleUse = singleUse;
		this.setArchived(false);
		
	}

	public long getId() {
		return urlMappingId;
	}

	public String getInput() {
		return input;
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

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (expiration ^ (expiration >>> 32));
		result = prime * result + ((input == null) ? 0 : input.hashCode());
		result = prime * result + ((output == null) ? 0 : output.hashCode());
		result = prime * result + (singleUse ? 1231 : 1237);
		result = prime * result + (int) (urlMappingId ^ (urlMappingId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UrlMapping other = (UrlMapping) obj;
		if (expiration != other.expiration)
			return false;
		if (input == null) {
			if (other.input != null)
				return false;
		} else if (!input.equals(other.input))
			return false;
		if (output == null) {
			if (other.output != null)
				return false;
		} else if (!output.equals(other.output))
			return false;
		if (singleUse != other.singleUse)
			return false;
		if (urlMappingId != other.urlMappingId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UrlMapping [urlMappingId=" + urlMappingId + ", input=" + input + ", output=" + output + ", expiration="
				+ expiration + ", singleUse=" + singleUse + "]";
	}

}
