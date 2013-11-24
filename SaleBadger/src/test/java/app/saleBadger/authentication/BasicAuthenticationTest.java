package app.saleBadger.authentication;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class BasicAuthenticationTest {
	
	private final String username = "lunayo";
	private final String password = "qwertyui";

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void decodeAuthorizationHeaderWithWrongCredential() {
		// encoded authorisation headers with username lunayo
		// and password qwertyui
		final String encodedCredential = "Basic bHVuYXlvOnJhbmRvbQ==";
		String[] userAndPass = BasicAuthentication.decode(encodedCredential);
		assertThat(userAndPass[0], is(username));
		assertThat(userAndPass[1], not(password));
	}

	@Test
	public void decodeAuthorizationHeaderWithValidCredential() {
		final String encodedCredential = "Basic bHVuYXlvOnF3ZXJ0eXVp";
		String[] userAndPass = BasicAuthentication.decode(encodedCredential);
		assertThat(userAndPass[0], is(username));
		assertThat(userAndPass[1], is(password));
	}

	@Test
	public void decodeAuthorizationHeaderWithInvalidCredential() {
		final String encodedCredential = "Basic bHVuYXlvOnJaRvbQ==";
		String[] userAndPass = BasicAuthentication.decode(encodedCredential);
		assertThat(userAndPass, nullValue());
	}
	
	@Test
	public void decodeAuthorizationHeaderWithEmptyCredential() {
		final String encodedCredential = "Basic ";
		String[] userAndPass = BasicAuthentication.decode(encodedCredential);
		assertThat(userAndPass, nullValue());
	}

}
