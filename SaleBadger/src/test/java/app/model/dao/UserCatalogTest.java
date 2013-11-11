package app.model.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import app.model.User;

public class UserCatalogTest {

	User user;
	UserCatalog userCatalog;

	@Before
	public void setUp() {
		user = new User("samatase", "123123123", "Emmanouil", "Samatas",
				"samatase@hotmail.com");
		userCatalog = UserCatalogMongo.getInstance();
		userCatalog.clear();

	}

	@Test
	public void addOneUserToUsersCollection() {
		long collectionSize = userCatalog.count();
		userCatalog.add(user);

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
			userCatalog.add(user);
		}

		long actualSize = userCatalog.count();
		long expectedSize = collectionSize + numberOfproductsToAdd;

		assertThat(actualSize, is(expectedSize));
	}

	@Test
	public void clearUserCatalogIsEmpty() {
		userCatalog.add(user);
		userCatalog.clear();

		long catalogSize = userCatalog.count();

		assertThat(catalogSize, is(0L));
	}

	@Test
	public void addAUserAndFindByUsernameReturnsUser() {
		String username = "lunayo";
		User userToAdd = new User(username, "pass1234", "Iskandar", "null",
				"incre@codebadge.com");
		userCatalog.add(userToAdd);
		User userFound = userCatalog.find(username);

		assertThat(userFound, is(userToAdd));

	}

}