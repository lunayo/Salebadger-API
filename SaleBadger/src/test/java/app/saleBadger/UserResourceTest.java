package app.saleBadger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import javax.ws.rs.core.MediaType;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.BeforeClass;
import org.junit.Test;

import app.model.User;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class UserResourceTest {

	private static HttpServer httpServer;
	private final String BASE_URL = "http://localhost:8181/api/v1";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// run the httpserver
		httpServer = Main.startServer();

	}

	protected void tearDown() throws Exception {

		httpServer.stop();
	}

	@Test
	public void getUserResourceAndCheckResponseCode() {
		String requestURL = BASE_URL + "/users/lunayo";
		try {

			Client client = Client.create();
			WebResource webResource = client.resource(requestURL);
			ClientResponse response = webResource.accept(
					MediaType.APPLICATION_JSON).get(ClientResponse.class);

			assertThat(response.getStatus(), is(200));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void getUserResourceWithInvalidUsername() {
		String requestURL = BASE_URL + "/users/lun";
		try {

			Client client = Client.create();
			WebResource webResource = client.resource(requestURL);
			ClientResponse response = webResource.accept(
					MediaType.APPLICATION_JSON).get(ClientResponse.class);

			assertThat(response.getStatus(), is(404));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addUserToResourceAndCheckResponseCode() {
		String requestURL = BASE_URL + "/users";
		try {
			User user = new User(UUID.randomUUID().toString(), "qwertyui",
					"lun@codebadge.com", "Iskandar", "Goh");
			Client client = Client.create();
			WebResource webResource = client.resource(requestURL);
			ClientResponse response = webResource.accept(
					MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, user);
			assertThat(response.getStatus(), is(200));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addUserResourceWithExistedUser() {
		String requestURL = BASE_URL + "/users";
		try {
			User user = new User("lunayo", "qwertyui", "lun@codebadge.com",
					"Iskandar", "Goh");
			Client client = Client.create();
			WebResource webResource = client.resource(requestURL);
			ClientResponse response = webResource.accept(
					MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, user);
			assertThat(response.getStatus(), is(409));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	// public void deleteUserFromResourceAndCheckResponseCode() {
	// String requestURL = BASE_URL + "/users/lunayo";
	// try {
	//
	// Client client = Client.create();
	// WebResource webResource = client.resource(requestURL);
	// ClientResponse response = webResource.accept(
	// MediaType.APPLICATION_JSON).delete(ClientResponse.class);
	//
	// assertThat(response.getStatus(), is(204));
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	@Test
	public void updateUserInResourceAndCheckResponseCode() {
		String requestURL = BASE_URL + "/users/lunayo";
		try {

			Client client = Client.create();
			WebResource webResource = client.resource(requestURL);
			ClientResponse response = webResource.accept(
					MediaType.APPLICATION_JSON).put(ClientResponse.class);

			assertThat(response.getStatus(), is(200));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
