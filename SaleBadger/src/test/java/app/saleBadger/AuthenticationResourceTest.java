package app.saleBadger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import app.saleBadger.model.Contact;
import app.saleBadger.model.Role;
import app.saleBadger.model.User;
import app.saleBadger.model.dao.UserRepository;
import app.saleBadger.model.dao.config.SpringMongoConfig;

public class AuthenticationResourceTest {

	private final Locale gb = new Locale("en", "GB");
	private final Contact userContact = new Contact(gb.getCountry(),
			gb.getDisplayCountry(), "7446653997");
	private final User dummyUser = new User("lunayo", "qwertyui",
			"lun@codebadge.com", Role.USER, "Iskandar", "Goh", userContact);
	private static HttpServer server;
	private WebTarget target;
	private final ApplicationContext context = new AnnotationConfigApplicationContext(
			SpringMongoConfig.class);
	private final UserRepository userRepository = context
			.getBean(UserRepository.class);

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
		userRepository.deleteAll();
		userRepository.save(dummyUser);
		executeUserBasicAuthenticationAndAssertResponse(username, password, 403);
	}
	
	@Test
	public void authenticateUserWithValidCredentialShouldReturnNoContentResponse() {
		final String username = "lunayo";
		final String password = "qwertyui";
		userRepository.deleteAll();
		userRepository.save(dummyUser);
		executeUserBasicAuthenticationAndAssertResponse(username, password, 204);
	}
}
