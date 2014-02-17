package app.saleBadger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import javax.net.ssl.SSLContext;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
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

public class UserResourceTest {

	private final Locale gb = new Locale("en", "GB");
	private final Contact userContact = new Contact(gb.getCountry(),
			gb.getDisplayCountry(), "7446653997");
	private final User dummyUser = new User("lunayo", "qwertyui",
			"lun@codebadge.com", Role.ADMIN, "Iskandar", "Goh", userContact);
	private final ApplicationContext context = new AnnotationConfigApplicationContext(
			SpringMongoConfig.class);
	private final UserRepository userRepository = context
			.getBean(UserRepository.class);

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

	private void getUserResourceAndAssertResponse(String username,
			String password, int responseCode, boolean credential) {
		try {
			userRepository.deleteAll();
			userRepository.save(dummyUser);
			if (credential)
				target.register(HttpAuthenticationFeature.basic("lunayo", password));
			Response response = target.path("user/" + username)
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
					.path("user")
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
				target.register(HttpAuthenticationFeature.basic("lunayo", password));
			Response response = target
					.path("user/" + user.getUsername())
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
				target.register(HttpAuthenticationFeature.basic("lunayo", password));
			Response response = target.path("user/" + username)
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
	public void addUserResourceWithInvalidContactPhoneNumber() {
		userRepository.deleteAll();
		Contact invalidContact = new Contact("GB", "United Kingdom", "11");
		User invalidUser = new User("lunayo", "qwertyui", "lun@codebadge.com",
				Role.ADMIN, "Iskandar", "Goh", invalidContact);
		addUserToResourceAndAssertResponse(invalidUser, 400);
	}

	@Test
	public void addUserResourceWithInvalidContactCountryCode() {
		userRepository.deleteAll();
		Contact invalidContact = new Contact("ASD", "Indonesia", "74466532");
		User invalidUser = new User("lunayo", "qwertyui", "lun@codebadge.com",
				Role.ADMIN, "Iskandar", "Goh", invalidContact);
		addUserToResourceAndAssertResponse(invalidUser, 400);
	}

	@Test
	public void addUserResourceWithInvalidContactCountryName() {
		userRepository.deleteAll();
		Contact invalidContact = new Contact("GB", "asdfea", "74466532");
		User invalidUser = new User("lunayo", "qwertyui", "lun@codebadge.com",
				Role.ADMIN, "Iskandar", "Goh", invalidContact);
		addUserToResourceAndAssertResponse(invalidUser, 400);
	}

	@Test
	public void addUserToResourceWithInvalidUsernameAndCheckResponseCode() {
		userRepository.deleteAll();
		userRepository.save(dummyUser);
		User user = new User("dfse", "qwertyui", "123", Role.ADMIN, "Iskandar",
				"Goh", userContact);
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
				"Goh", userContact);
		updateUserInResourceAndAssertResponse(user, 400, true);
	}

	@Test
	public void updateUserInResourceWithNonExistedUserAndCheckResponseCode() {
		User user = new User("lisanina", "asasdasasd", "lisa@codebadge.com",
				Role.ADMIN, "asdfffda", "Goh", userContact);
		updateUserInResourceAndAssertResponse(user, 404, true);
	}

	@Test
	public void updateUserInResourceWithInvalidEmailAndCheckResponseCode() {
		User user = new User("lunayo", "asasdasasd", "lisacodebadgecom",
				Role.ADMIN, "asdfffda", "Goh", userContact);
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
