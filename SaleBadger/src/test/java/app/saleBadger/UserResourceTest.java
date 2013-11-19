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
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.api.json.JSONConfiguration;

public class UserResourceTest {

	private static HttpServer httpServer;
	private final String BASE_URL = "http://localhost:8181/api/v1";
	private static Client client;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// run the httpserver
		httpServer = Main.startServer();
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		client = Client.create(clientConfig);
		client.addFilter(new LoggingFilter(System.out));
	}

	protected void tearDown() throws Exception {

		httpServer.stop();
	}

	private void addUserToResourceAndAssertResponse(User user, int responseCode) {
		String requestURL = BASE_URL + "/users";
		try {
			WebResource webResource = client.resource(requestURL);
			ClientResponse response = webResource
					.type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, user);
			assertThat(response.getStatus(), is(responseCode));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void getUserResourceAndCheckResponseCode() {
		String requestURL = BASE_URL + "/users/lunayo";
		try {
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
		User user = new User(UUID.randomUUID().toString(), "qwertyui",
				"lun@codebadge.com", "Iskandar", "Goh");
		addUserToResourceAndAssertResponse(user, 200);
	}

	@Test
	public void addUserToResourceWithInvalidPropertiesAndCheckResponseCode() {
		User user = new User("df", "qwertyui", "123",
				"Iskandar", "Goh");
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
			WebResource webResource = client.resource(requestURL);
			ClientResponse response = webResource.accept(
					MediaType.APPLICATION_JSON).put(ClientResponse.class);

			assertThat(response.getStatus(), is(200));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
