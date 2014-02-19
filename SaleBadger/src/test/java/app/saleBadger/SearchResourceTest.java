package app.saleBadger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Currency;
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
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.geo.Point;

import app.saleBadger.model.Contact;
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

	private final Locale gb = new Locale("en", "GB");
	private final Currency gbp = Currency.getInstance(gb);
	private final Price iPhonePrice = new Price(499, gbp.getCurrencyCode());
	private final Product dummyProduct = new Product("iPhone", "Description",
			iPhonePrice, "lunayo", new Point(15.123212, 61.654321));
	private final Contact userContact = new Contact(gb.getCountry(), gb.getDisplayCountry(), "7446653997");
	private final User dummyUser = new User("lunayo", "qwertyui",
			"lun@codebadge.com", Role.ADMIN, "Iskandar", "Goh", userContact);
	private final ApplicationContext context = new AnnotationConfigApplicationContext(
			SpringMongoConfig.class);
	private final ProductRepository productRepository = context
			.getBean(ProductRepository.class);
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
				.register(new LoggingFilter()).register(JacksonFeature.class)
				.build();
		c.register(new LoggingFilter());

		target = c.target(Main.BASE_URI);

	}

	@AfterClass
	public static void tearDown() throws Exception {
		server.shutdown();
	}

	public void getProductResourceAndAssertResponseCode(String keyword,
			int responseCode) {
		Response response = getProductResource(keyword, null);
		assertThat(response.getStatus(), is(responseCode));
	}

	public void getProductResourceAndAssertResponseCode(Point location,
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

	public Response getProductResource(String keyword, Point location) {
		try {
			productRepository.deleteAll();
			productRepository.save(dummyProduct);
			Response response = null;
			Invocation.Builder invocationBuilder = null;
			WebTarget resourceWebTarget = target.path("v1/products");

			if (location != null) {
				WebTarget nearParam = resourceWebTarget.queryParam("near",
						location.getX() + "," + location.getY());
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
		Response response = getUserResource(keyword, true);
		assertThat(response.getStatus(), is(responseCode));
	}

	public void getUserResourceAndAssertUsersCount(String keyword, int count) {
		Response response = getUserResource(keyword, true);
		UserList users = response.readEntity(UserList.class);
		assertThat(users.getUsers().size(), is(count));
	}

	public Response getUserResource(String keyword,
			boolean credential) {
		try {
			userRepository.deleteAll();
			userRepository.save(dummyUser);
			Response response = null;
			Invocation.Builder invocationBuilder = null;
			WebTarget resourceWebTarget = target.path("v1/users");
			if (credential)
				resourceWebTarget.register(HttpAuthenticationFeature.basic("lunayo", "qwertyui"));

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
		getProductResourceAndAssertResponseCode(new Point(360.123212,
				360.654321), 400);
	}

	@Test
	public void getProductsFromResourceWithSearchKeywordAndCheckResponseCode() {
		getProductResourceAndAssertResponseCode("black", 200);
	}

	@Test
	public void getProductsFromResourceWithSearchKeywordAndCheckCount() {
		getProductResourceAndAssertProductsCount(dummyProduct.getName(), 1);
	}

	 @Test
	 public void getUsersFromResourceWithSearchKeywordAndCheckResponseCode() {
		 getUserResourceAndAssertResponseCode("black", 200);
	 }

	@Test
	public void getUsersFromResourceWithSearchKeywordAndCheckCount() {
		getUserResourceAndAssertUsersCount(dummyUser.getUsername(), 1);
	}
}
