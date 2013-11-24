package app.saleBadger.authentication;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

public class UserAuthenticationTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void checkPasswordWithValidCredential() {
		final String password = "lunayo";
		String hashedPassword = UserAuthentication
				.getSaltedHashPassword(password);
		assertThat(UserAuthentication.check(password, hashedPassword), is(true));
	}

	@Test
	public void checkPasswordWithEmptyCredential() {
		final String password = "";
		String hashedPassword = UserAuthentication
				.getSaltedHashPassword(password);
		assertThat(hashedPassword, nullValue());
		assertThat(UserAuthentication.check(password, hashedPassword), is(false));
	}
	
	@Test
	public void checkPasswordWithInvalidCredential() {
		final String password = "adsf";
		String hashedPassword = "asdfqr";
		assertThat(UserAuthentication.check(password, hashedPassword), is(false));
	}

}
