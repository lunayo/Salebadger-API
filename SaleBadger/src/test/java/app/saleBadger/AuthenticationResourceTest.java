package app.saleBadger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.net.ssl.SSLContext;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AuthenticationResourceTest {

	private static HttpServer server;
	private WebTarget target;

	@BeforeClass
	public static void startServer() {
		// start the server
		server = Main.startServer();
	}

	@Before
	public void setUp() throws Exception {

		final SSLContext sslContext = Main.createSSLContext(false);

		// create the client
		Client c = ClientBuilder.newBuilder().sslContext(sslContext)
				.register(JacksonFeature.class).build();
		c.register(new LoggingFilter());

		target = c.target(Main.BASE_URI);

	}

	@AfterClass
	public static void tearDown() throws Exception {
		server.shutdown();
	}

	private void executeUserBasicAuthenticationAndAssertResponse(
			String username, String password, int responseCode) {
		try {
			target.register(HttpAuthenticationFeature.basic("lunayo", password));
			Response response = target.path("v1/auth/basic")
					.request(MediaType.APPLICATION_JSON).get(Response.class);

			assertThat(response.getStatus(), is(responseCode));

		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}

	@Test
	public void authenticateUserWithInvalidCredentialShouldReturnForbiddenResponse() {
		final String username = "lunayo";
		final String password = "123434";

		executeUserBasicAuthenticationAndAssertResponse(username, password, 403);
	}
	
	@Test
	public void authenticateUserWithValidCredentialShouldReturnNoContentResponse() {
		final String username = "lunayo";
		final String password = "qwertyui";

		executeUserBasicAuthenticationAndAssertResponse(username, password, 204);
	}
}
