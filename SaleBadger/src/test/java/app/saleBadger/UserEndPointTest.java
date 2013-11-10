package app.saleBadger;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class UserEndPointTest {

	private static HttpServer httpServer;
	private static WebResource webResource;
	private static HttpURLConnection urlConnection;
	private final String BASE_URL = "http://localhost:8181/api/v1/";
	private final String USER_AGENT = "Mozilla/5.0";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// run the httpserver
		httpServer = Main.startServer();
		
		Client client = Client.create();
		webResource = client.resource(Main.BASE_URI);

	}
	
	protected void tearDown() throws Exception {

        httpServer.stop();
    }

	@Test
	public void getUser() {
		String requestURL = BASE_URL + "/users/lunayo";
		try {
			URL url = new URL(requestURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			connection.setRequestProperty("User-Agent", USER_AGENT);
			
			int responseCode = connection.getResponseCode();
			
			assertThat(responseCode, is(200));
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void addUser() {
		fail("Not yet implemented");
	}
	
	@Test
	public void deleteUser() {
		String requestURL = BASE_URL + "/users/lunayo";
		try {
			URL url = new URL(requestURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("DELETE");
			
			connection.setRequestProperty("User-Agent", USER_AGENT);
			
			int responseCode = connection.getResponseCode();
			
			assertThat(responseCode, is(204));
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void updateUser() {
		String requestURL = BASE_URL + "/users/lunayo";
		try {
			URL url = new URL(requestURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("PUT");
			
			connection.setRequestProperty("User-Agent", USER_AGENT);
			
			int responseCode = connection.getResponseCode();
			
			assertThat(responseCode, is(200));
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
