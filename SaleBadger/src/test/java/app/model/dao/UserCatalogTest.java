package app.model.dao;

import static org.junit.Assert.*;

import org.junit.Test;

import app.model.User;

public class UserCatalogTest {

	User user;
	UserCatalog userCatalog;

	public void setUp() {
	user = new User("samatase","123123123");
	userCatalog = new UsersCollection("users");
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
