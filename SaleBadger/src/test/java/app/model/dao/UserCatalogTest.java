package app.model.dao;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.listeners.CollectCreatedMocks;

import static org.mockito.Mockito.mock;
import app.model.User;

public class UserCatalogTest {

	User user;
	UserCatalog userCatalog;
	User mockUser;
	@Before
	public void setUp() {
		user = new User("samatase", "123123123", "Emmanouil", "Samatas",
				"samatase@hotmail.com");
		userCatalog = UserCatalogMongo.getInstance();
		mockUser = mock(User.class);
		
	}

	@Test
	public void addOneUserToUsersCollection() {
		long collectionSize = userCatalog.count();
		userCatalog.addUser(user);

		long actualSize = userCatalog.count();
		long expectedSize = collectionSize + 1;

		assertThat(actualSize, is(expectedSize));
	}

	@Test
	public void addManyUsersToCollection() {
		long collectionSize = userCatalog.count();
		int numberOfproductsToAdd = 5;
		for (int i = 0; i < numberOfproductsToAdd; i++) {
			user = new User("user" + i, "123123123", "Emmanouil", "Samatas",
					"samatase@hotmail.com");
			userCatalog.addUser(user);
		}
		
		long actualSize = userCatalog.count();
		long expectedSize = collectionSize + numberOfproductsToAdd;
		
		assertThat(actualSize, is(expectedSize));
	}

	
}