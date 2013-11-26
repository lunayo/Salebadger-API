package app.saleBadger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.net.ssl.SSLContext;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.client.filter.HttpBasicAuthFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import app.model.Role;
import app.model.User;
import app.model.dao.UserRepository;
import app.model.dao.config.SpringMongoConfig;

public class UserResourceTest {

	private final User dummyUser = new User("lunayo", "qwertyui",
			"lun@codebadge.com", Role.ADMIN, "Iskandar", "Goh");
	private final ApplicationContext context = new AnnotationConfigApplicationContext(
			SpringMongoConfig.class);
	private final UserRepository userRepository = context
			.getBean(UserRepository.class);
	
	private HttpServer server;
	private WebTarget target;

	@Before
	public void setUp() throws Exception {
		// start the server
		server = Main.startServer();

		SslConfigurator sslConfig = SslConfigurator.newInstance()
				.trustStoreFile(Main.TRUSTSTORE_CLIENT_FILE)
				.trustStorePassword(Main.TRUSTSTORE_CLIENT_PWD)
				.keyStoreFile(Main.KEYSTORE_CLIENT_FILE)
				.keyPassword(Main.KEYSTORE_CLIENT_PWD);

		final SSLContext sslContext = sslConfig.createSSLContext();

		// create the client
		Client c = ClientBuilder.newBuilder().sslContext(sslContext)
				.register(JacksonFeature.class).build();

		target = c.target(Main.BASE_URI);

	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	private void getUserResourceAndAssertResponse(String username,
			String password, int responseCode, boolean credential) {
		try {
			userRepository.deleteAll();
			userRepository.save(dummyUser);
			if (credential)
				target.register(new HttpBasicAuthFilter("lunayo", password));
			Response response = target.path("users/" + username)
					.request(MediaType.APPLICATION_JSON).get(Response.class);

			assertThat(response.getStatus(), is(responseCode));

		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}

	private void getUserResourceAndAssertResponse(String username,
			int responseCode, boolean credential) {
		getUserResourceAndAssertResponse(username, "qwertyui", responseCode,
				credential);
	}

	private void addUserToResourceAndAssertResponse(User user, int responseCode) {
		try {
			Response response = target
					.path("users")
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(user, MediaType.APPLICATION_JSON),
							Response.class);

			assertThat(response.getStatus(), is(responseCode));

		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}

	private void updateUserInResourceAndAssertResponse(User user,
			String password, int responseCode, boolean credential) {
		try {
			userRepository.deleteAll();
			userRepository.save(dummyUser);
			if (credential)
				target.register(new HttpBasicAuthFilter("lunayo", password));
			Response response = target
					.path("users/" + user.getUsername())
					.request(MediaType.APPLICATION_JSON)
					.put(Entity.entity(user, MediaType.APPLICATION_JSON),
							Response.class);

			assertThat(response.getStatus(), is(responseCode));

		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}

	private void updateUserInResourceAndAssertResponse(User user,
			int responseCode, boolean credential) {
		updateUserInResourceAndAssertResponse(user, "qwertyui", responseCode,
				credential);
	}

	private void deleteUserFromResourceAndAssertResponse(String username,
			String password, int responseCode, boolean credential) {
		try {
			userRepository.deleteAll();
			userRepository.save(dummyUser);
			if (credential)
				target.register(new HttpBasicAuthFilter("lunayo", password));
			Response response = target.path("users/" + username)
					.request(MediaType.APPLICATION_JSON).delete();

			assertThat(response.getStatus(), is(responseCode));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteUserFromResourceAndAssertResponse(String username,
			int responseCode, boolean credential) {
		deleteUserFromResourceAndAssertResponse(username, "qwertyui",
				responseCode, credential);
	}

	@Test
	public void addUserToResourceAndCheckResponseCode() {
		userRepository.deleteAll();
		addUserToResourceAndAssertResponse(dummyUser, 200);
	}

	@Test
	public void addUserResourceWithExistedUser() {
		userRepository.deleteAll();
		userRepository.save(dummyUser);
		addUserToResourceAndAssertResponse(dummyUser, 409);
	}

	@Test
	public void addUserToResourceWithInvalidUsernameAndCheckResponseCode() {
		userRepository.deleteAll();
		userRepository.save(dummyUser);
		User user = new User("dfse", "qwertyui", "123", Role.ADMIN, "Iskandar",
				"Goh");
		addUserToResourceAndAssertResponse(user, 400);
	}

	@Test
	public void getUserResourceAndCheckResponseCode() {
		getUserResourceAndAssertResponse("lunayo", 200, true);
	}

	@Test
	public void getUserResourceWithNoCredentialAndCheckResponseCode() {
		getUserResourceAndAssertResponse("lunayo", 403, false);
	}

	@Test
	public void getUserResourceWithInvalidCredentialAndCheckResponseCode() {
		getUserResourceAndAssertResponse("lunayo", "random", 403, true);
	}

	@Test
	public void getUserResourceWithInvalidUsername() {
		getUserResourceAndAssertResponse("lun", 400, true);
	}

	@Test
	public void getUserResourceWithNonExistedUsername() {
		getUserResourceAndAssertResponse("lunanana", 404, true);
	}

	@Test
	public void updateUserInResourceAndCheckResponseCode() {
		updateUserInResourceAndAssertResponse(dummyUser, 200, true);
	}

	@Test
	public void updateUserInResourceWithInvalidUsernameAndCheckResponseCode() {
		User user = new User(" ", "as ", "luncodebadgecom", Role.ADMIN, "asd",
				"Goh");
		updateUserInResourceAndAssertResponse(user, 400, true);
	}

	@Test
	public void updateUserInResourceWithNonExistedUserAndCheckResponseCode() {
		User user = new User("lisanina", "asasdasasd", "lisa@codebadge.com",
				Role.ADMIN, "asdfffda", "Goh");
		updateUserInResourceAndAssertResponse(user, 404, true);
	}

	@Test
	public void updateUserInResourceWithInvalidEmailAndCheckResponseCode() {
		User user = new User("lunayo", "asasdasasd", "lisacodebadgecom",
				Role.ADMIN, "asdfffda", "Goh");
		updateUserInResourceAndAssertResponse(user, 400, true);
	}

	@Test
	public void updateUserInResourceWithInvalidCredentialAndCheckResponseCode() {
		updateUserInResourceAndAssertResponse(dummyUser, "random", 403, true);
	}

	@Test
	public void deleteUserFromResourceAndCheckResponseCode() {
		deleteUserFromResourceAndAssertResponse(dummyUser.getUsername(), 204,
				true);
	}

	@Test
	public void deleteUserFromResourceWithInvalidCredentialAndCheckResponseCode() {
		deleteUserFromResourceAndAssertResponse(dummyUser.getUsername(),
				"random", 403, true);
	}

	@Test
	public void deleteUserFromResourceWithInvalidUserAndCheckResponseCode() {
		deleteUserFromResourceAndAssertResponse("lu", 400, true);
	}

	@Test
	public void deleteUserFromResourceWithNonExistedUserAndCheckResponseCode() {
		deleteUserFromResourceAndAssertResponse("lunaluna", 404, true);
	}

}
