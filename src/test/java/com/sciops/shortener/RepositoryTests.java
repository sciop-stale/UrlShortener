package com.sciops.shortener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class RepositoryTests {

	@Autowired UserRepository ur;
	@Autowired UrlMappingRepository umr;
	
	@Test
	void RepositoryLoadsTest() {
		assertNotNull(ur);
		assertNotNull(ur.count());
		assertEquals(0, ur.count());
		assertNotNull(umr);
		assertNotNull(umr.count());
		assertEquals(0, umr.count());
	}
	
	@Test
	void AddingUsersTest() {
		ur.save(new User("John", "someBlob"));
		assertNotNull(ur.findByUserName("John"));
	}
	
	@Test
	void UserNotFoundTest() {
		ur.save(new User("John", "someBlob"));
		assertNull(ur.findByUserName("Michael"));
	}
	
	@Test
	void UpdateUserNameTest() {
		ur.save(new User("John", "someBlob"));
		User user = ur.findByUserName("John");
		user.setUserName("Jon");
		ur.save(user);
		assertNotNull(ur.findByUserName("Jon"));
		assertEquals("someBlob", ur.findByUserName("Jon").getPwdHash());
		assertNull(ur.findByUserName("John"));
	}
	
	@Test
	void AddingMappingTest() {
		User user;
		user = new User("John", "someBlob");
		ur.save(user);
		assertNotNull(umr.save(new UrlMapping("shortUrl", "longUrl", 10, false, user)));
		assertNotNull(umr.save(new UrlMapping("shortUrl2", "longUrl", 10, false, null)));
	}
	
	@Test
	void MappingCorrectlySavedTest() {
		User user = new User("John", "someBlob");
		UrlMapping urlMapping = new UrlMapping("shortUrl", "longUrl", 10, false, user);
		ur.save(user);
		umr.save(urlMapping);
		assertEquals("shortUrl", umr.findById(urlMapping.getId()).getInput());
		assertEquals("longUrl", umr.findById(urlMapping.getId()).getOutput());
		assertEquals(ur.findByUserName("John"), umr.findById(urlMapping.getId()).getUser());
		assertEquals(10, umr.findById(urlMapping.getId()).getExpiration());
		assertEquals(false, umr.findById(urlMapping.getId()).isSingleUse());
	}
	
	@Test
	void findByOwnerTest() {
		User u1, u2, u3;
		u1 = new User("John", "someBlob");
		u2 = new User("Michael", "anotherBlob");
		u3 = new User("Jon", "BlobGlob");
		UrlMapping[] um = new UrlMapping[] {
				new UrlMapping("shortUrl1", "longUrl", 10, false, u1),
				new UrlMapping("shortUrl2", "longUrl", 10, false, u2),
				new UrlMapping("shortUrl3", "longUrl", 12, true, u1),
				new UrlMapping("shortUrl4", "longUrl", 10, false, u3),
				new UrlMapping("shortUrl5", "longUrl", 13, false, u1),
				new UrlMapping("shortUrl6", "longUrl", 10, false, u1),
				new UrlMapping("shortUrl7", "longUrl", 11, true, u3),
				new UrlMapping("shortUrl8", "longUrl", 8, false, u2)
		};
		ur.save(u1);
		ur.save(u2);
		ur.save(u3);
		for(UrlMapping i: um) umr.save(i);
		assertEquals(4, umr.countByUser(ur.findByUserName("John")));
	}
	
	@Test
	void findByInputTest() {
		User u1, u2, u3;
		u1 = new User("John", "someBlob");
		u2 = new User("Michael", "anotherBlob");
		u3 = new User("Jon", "BlobGlob");
		UrlMapping[] um = new UrlMapping[] {
				new UrlMapping("shortUrl1", "longUrl", 10, false, u1),
				new UrlMapping("shortUrl2", "longUrl", 10, false, u2),
				new UrlMapping("shortUrl3", "longUrl", 12, true, u1),
				new UrlMapping("shortUrl4", "longUrl", 10, false, u3),
				new UrlMapping("shortUrl5", "longUrl", 13, false, u1),
				new UrlMapping("shortUrl6", "longUrl", 10, false, u1),
				new UrlMapping("shortUrl7", "longUrl", 11, true, u3),
				new UrlMapping("shortUrl8", "longUrl", 8, false, u1)
		};
		ur.save(u1);
		ur.save(u2);
		ur.save(u3);
		for(UrlMapping i: um) umr.save(i);
		assertEquals(12, umr.findByInput("shortUrl3").getExpiration());
	}
	
	@Test
	void deleteExpiredTest() {
		User u1, u2, u3;
		u1 = new User("John", "someBlob");
		u2 = new User("Michael", "anotherBlob");
		u3 = new User("Jon", "BlobGlob");
		UrlMapping[] um = new UrlMapping[] {
				new UrlMapping("shortUrl1", "longUrl", 10, false, u1),
				new UrlMapping("shortUrl2", "longUrl", 10, false, u2),
				new UrlMapping("shortUrl3", "longUrl", 12, true, u1),
				new UrlMapping("shortUrl4", "longUrl", 10, false, u3),
				new UrlMapping("shortUrl5", "longUrl", 13, false, u1),
				new UrlMapping("shortUrl6", "longUrl", 10, false, u1),
				new UrlMapping("shortUrl7", "longUrl", 11, true, u3),
				new UrlMapping("shortUrl8", "longUrl", 8, false, u1)
		};
		ur.save(u1);
		ur.save(u2);
		ur.save(u3);
		for(UrlMapping i: um) umr.save(i);
		assertEquals(1, umr.deleteByExpirationLessThanEqual(9));
		assertEquals(7, umr.count());
		assertEquals(4, umr.deleteByExpirationLessThanEqual(10));
		assertEquals(3, umr.count());
	}
	
}
