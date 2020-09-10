package com.sciops.shortener;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long userId;
	
	private String userName;
	
	private String pwdHash;
	
	@OneToMany(mappedBy = "user")
	private List<UrlMapping> mappings = new ArrayList<>();
	
	public User() {
		
	}

	public User(String userName, String pwdHash) {
		this.userName = userName;
		this.pwdHash = pwdHash;
	}



	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwdHash() {
		return pwdHash;
	}

	public void setPwdHash(String pwdHash) {
		this.pwdHash = pwdHash;
	}

	public long getUserId() {
		return userId;
	}

	public List<UrlMapping> getMappings() {
		return mappings;
	}
	
	public void add(UrlMapping mapping) {
		mapping.setUser(this);
		mappings.add(mapping);
	}
	
	public void disassociateMapping(UrlMapping mapping) {
		if(mappings.contains(mapping)) mappings.remove(mapping);
		mapping.setUser(null);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pwdHash == null) ? 0 : pwdHash.hashCode());
		result = prime * result + (int) (userId ^ (userId >>> 32));
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		User other = (User) obj;
		if (pwdHash == null) {
			if (other.pwdHash != null)
				return false;
		} else if (!pwdHash.equals(other.pwdHash))
			return false;
		if (userId != other.userId)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", pwdHash=" + pwdHash + "]";
	}
	
}
