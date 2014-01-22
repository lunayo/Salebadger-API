package app.saleBadger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.SSLContext;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import app.saleBadger.model.Price;
import app.saleBadger.model.Product;
import app.saleBadger.model.ProductList;
import app.saleBadger.model.Role;
import app.saleBadger.model.User;
import app.saleBadger.model.UserList;
import app.saleBadger.model.dao.ProductRepository;
import app.saleBadger.model.dao.UserRepository;
import app.saleBadger.model.dao.config.SpringMongoConfig;

public class SearchResourceTest {

	private final Currency gbp = Currency.getInstance(new Locale("en", "GB"));
	private final Price iPhonePrice = new Price(499, gbp.getCurrencyCode());
	private final Product dummyProduct = new Product("iPhone", "Description",
			iPhonePrice, "lunayo", Arrays.asList(15.123212, 61.654321));
	private final User dummyUser = new User("lunayo", "qwertyui",
			"lun@codebadge.com", Role.ADMIN, "Iskandar", "Goh");
	private final ApplicationContext context = new AnnotationConfigApplicationContext(
			SpringMongoConfig.class);
	private final ProductRepository productRepository = context
			.getBean(ProductRepository.class);
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
				.register(new LoggingFilter()).register(JacksonFeature.class)
				.build();

		target = c.target(Main.BASE_URI);

	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	public void getProductResourceAndAssertResponseCode(String keyword,
			int responseCode) {
		Response response = getProductResource(keyword, null);
		assertThat(response.getStatus(), is(responseCode));
	}

	public void getProductResourceAndAssertResponseCode(List<Double> location,
			int responseCode) {
		Response response = getProductResource(null, location);
		assertThat(response.getStatus(), is(responseCode));
	}

	public void getProductResourceAndAssertProductsCount(String keyword,
			int count) {
		Response response = getProductResource(keyword, null);
		ProductList products = response.readEntity(ProductList.class);
		assertThat(products.getProducts().size(), is(count));
	}

	public Response getProductResource(String keyword, List<Double> location) {
		try {
			productRepository.deleteAll();
			productRepository.save(dummyProduct);
			Response response = null;
			Invocation.Builder invocationBuilder = null;
			WebTarget resourceWebTarget = target.path("products");

			if (location != null) {
				WebTarget nearParam = resourceWebTarget.queryParam("near",
						location.get(0) + ";" + location.get(1));
				invocationBuilder = nearParam
						.request(MediaType.APPLICATION_JSON);
			}
			if (keyword != null) {
				WebTarget qTarget = resourceWebTarget.queryParam("q", keyword);
				invocationBuilder = qTarget.request(MediaType.APPLICATION_JSON);
			}

			response = invocationBuilder.get(Response.class);

			return response;

		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}

	public void getUserResourceAndAssertResponseCode(String keyword,
			int responseCode) {
		Response response = getUserResource(keyword, null);
		assertThat(response.getStatus(), is(responseCode));
	}

	public void getUserResourceAndAssertUsersCount(String keyword, int count) {
		Response response = getUserResource(keyword, null);
		UserList users = response.readEntity(UserList.class);
		assertThat(users.getUsers().size(), is(count));
	}

	public Response getUserResource(String keyword, List<Double> location) {
		try {
			userRepository.deleteAll();
			userRepository.save(dummyUser);
			Response response = null;
			Invocation.Builder invocationBuilder = null;
			WebTarget resourceWebTarget = target.path("users");

			if (location != null) {
				WebTarget nearParam = resourceWebTarget.queryParam("near",
						location.get(0) + ";" + location.get(1));
				invocationBuilder = nearParam
						.request(MediaType.APPLICATION_JSON);
			}
			if (keyword != null) {
				WebTarget qTarget = resourceWebTarget.queryParam("q", keyword);
				invocationBuilder = qTarget.request(MediaType.APPLICATION_JSON);
			}

			response = invocationBuilder.get(Response.class);

			return response;

		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}

	@Test
	public void getNearbyProductsFromResourceAndCheckResponseCode() {
		getProductResourceAndAssertResponseCode(dummyProduct.getLocation(), 200);
	}

	@Test
	public void getNearbyProductsFromResourceWithInvalidLocationAndCheckResponseCode() {
		getProductResourceAndAssertResponseCode(
				Arrays.asList(360.123212, 360.654321), 400);
	}

	@Test
	public void getProductsFromResourceWithSearchKeywordAndCheckResponseCode() {
		getProductResourceAndAssertResponseCode("black", 200);
	}

	@Test
	public void getProductsFromResourceWithSearchKeywordAndCheckCount() {
		getProductResourceAndAssertProductsCount(dummyProduct.getName(), 1);
	}

	// @Test
	// public void getUsersFromResourceWithSearchKeywordAndCheckResponseCode() {
	// getUserResourceAndAssertResponseCode("black", 200);
	// }

	@Test
	public void getUsersFromResourceWithSearchKeywordAndCheckCount() {
		getUserResourceAndAssertUsersCount(dummyUser.getUsername(), 1);
	}
}
