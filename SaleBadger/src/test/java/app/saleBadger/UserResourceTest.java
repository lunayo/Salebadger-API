package app.saleBadger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.filter.HttpBasicAuthFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import app.model.User;

public class UserResourceTest {

	private HttpServer server;
	private WebTarget target;

	@Before
	public void setUp() throws Exception {
		// start the server
		server = Main.startServer();
		// create the client
		Client c = ClientBuilder.newClient().register(JacksonFeature.class);

		target = c.target(Main.BASE_URI);
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	private void getUserResourceAndAssertResponse(String username,
			int responseCode) {
		try {
			Response response = target.path("v1/users/" + username)
					.request(MediaType.APPLICATION_JSON).get(Response.class);

			assertThat(response.getStatus(), is(responseCode));

		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}

	private void addUserToResourceAndAssertResponse(User user, int responseCode) {
		try {
			target.register(new HttpBasicAuthFilter(user.getUsername(), user
					.getPassword()));
			Response response = target
					.path("v1/users")
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
			int responseCode) {
		try {
			target.register(new HttpBasicAuthFilter(user.getUsername(), user
					.getPassword()));
			Response response = target
					.path("v1/users/" + user.getUsername())
					.request(MediaType.APPLICATION_JSON)
					.put(Entity.entity(user, MediaType.APPLICATION_JSON),
							Response.class);

			assertThat(response.getStatus(), is(responseCode));

		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}

	private void deleteUserFromResourceAndAssertResponse(String username,
			String password, int responseCode) {
		try {
			target.register(new HttpBasicAuthFilter(username, password));
			Response response = target.path("v1/users/" + username)
					.request(MediaType.APPLICATION_JSON).delete();

			assertThat(response.getStatus(), is(responseCode));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addUserToResourceAndCheckResponseCode() {
		User user = new User("lunayo", "qwertyui", "lun@codebadge.com",
				"Iskandar", "Goh");
		addUserToResourceAndAssertResponse(user, 200);
	}

	@Test
	public void addUserResourceWithExistedUser() {
		User user = new User("lunayo", "qwertyui", "lun@codebadge.com",
				"Iskandar", "Goh");
		addUserToResourceAndAssertResponse(user, 409);
	}

	@Test
	public void addUserToResourceWithInvalidUsernameAndCheckResponseCode() {
		User user = new User("dfse", "qwertyui", "123", "Iskandar", "Goh");
		addUserToResourceAndAssertResponse(user, 400);
	}

	@Test
	public void getUserResourceAndCheckResponseCode() {
		getUserResourceAndAssertResponse("lunayo", 200);
	}

	@Test
	public void getUserResourceWithInvalidUsername() {
		getUserResourceAndAssertResponse("lun", 400);
	}

	@Test
	public void getUserResourceWithNonExistedUsername() {
		getUserResourceAndAssertResponse("lunanana", 404);
	}

	@Test
	public void updateUserInResourceAndCheckResponseCode() {
		User user = new User("lunayo", "qwertyuiasdf", "lun@codebadge.com",
				"Iskandar", "Goh");
		updateUserInResourceAndAssertResponse(user, 200);
	}

	@Test
	public void updateUserInResourceWithInvalidUsernameAndCheckResponseCode() {
		User user = new User(" ", "as ", "luncodebadgecom", "asd", "Goh");
		updateUserInResourceAndAssertResponse(user, 400);
	}

	@Test
	public void updateUserInResourceWithNonExistedUserAndCheckResponseCode() {
		User user = new User("lisanina", "asasdasasd", "lisa@codebadge.com",
				"asdfffda", "Goh");
		updateUserInResourceAndAssertResponse(user, 404);
	}

	@Test
	public void updateUserInResourceWithInvalidEmailAndCheckResponseCode() {
		User user = new User("lunayo", "asasdasasd", "lisacodebadgecom",
				"asdfffda", "Goh");
		updateUserInResourceAndAssertResponse(user, 400);
	}

	@Test
	public void deleteUserFromResourceAndCheckResponseCode() {
		deleteUserFromResourceAndAssertResponse("lunayo", "qwertyui", 204);
	}

	@Test
	public void deleteUserFromResourceWithInvalidUserAndCheckResponseCode() {
		deleteUserFromResourceAndAssertResponse("lu", "qwertyui", 400);
	}

	@Test
	public void deleteUserFromResourceWithNonExistedUserAndCheckResponseCode() {
		deleteUserFromResourceAndAssertResponse("lunaluna", "qwertyui", 404);
	}
}
