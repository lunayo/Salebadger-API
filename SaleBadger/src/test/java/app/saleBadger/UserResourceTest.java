package app.saleBadger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientResponse;
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
	public void addUserToResourceAndCheckResponseCode() {
		User user = new User(UUID.randomUUID().toString(), "qwertyui",
				"lun@codebadge.com", "Iskandar", "Goh");
		addUserToResourceAndAssertResponse(user, 200);
	}

	@Test
	public void addUserToResourceWithInvalidPropertiesAndCheckResponseCode() {
		User user = new User("dfse", "qwertyui", "123", "Iskandar", "Goh");
		addUserToResourceAndAssertResponse(user, 400);
	}

	@Test
	public void addUserResourceWithExistedUser() {
		User user = new User("lunayo", "qwertyui", "lun@codebadge.com",
				"Iskandar", "Goh");
		addUserToResourceAndAssertResponse(user, 409);
	}

	// @Test
	// public void deleteUserFromResourceAndCheckResponseCode() {
	// try {
	// Response response = target
	// .path("v1/users/lunayo")
	// .request(MediaType.APPLICATION_JSON)
	// .delete();
	//
	// assertThat(response.getStatus(), is(204));
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	@Test
	public void updateUserInResourceAndCheckResponseCode() {
		try {
			ClientResponse response = target.path("/users/lunayo")
					.request(MediaType.APPLICATION_JSON)
					.get(ClientResponse.class);

			assertThat(response.getStatus(), is(200));

		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}

}
