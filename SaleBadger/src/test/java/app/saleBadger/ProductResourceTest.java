package app.saleBadger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Currency;
import java.util.Locale;

import javax.net.ssl.SSLContext;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.filter.HttpBasicAuthFilter;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.geo.Point;

import app.saleBadger.model.Contact;
import app.saleBadger.model.Price;
import app.saleBadger.model.Product;
import app.saleBadger.model.Role;
import app.saleBadger.model.User;
import app.saleBadger.model.dao.ProductRepository;
import app.saleBadger.model.dao.UserRepository;
import app.saleBadger.model.dao.config.SpringMongoConfig;

public class ProductResourceTest {

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
	private final UserRepository userRepository = context
			.getBean(UserRepository.class);
	private final ProductRepository productRepository = context
			.getBean(ProductRepository.class);
	private HttpServer server;
	private WebTarget target;

	@Before
	public void setUp() throws Exception {
		// start the server
		server = Main.startServer();

		final SSLContext sslContext = Main.createSSLContext(false);

		// create the client
		Client c = ClientBuilder.newBuilder().sslContext(sslContext)
				.register(new LoggingFilter()).register(JacksonFeature.class)
				.build();

		target = c.target(Main.BASE_URI);

		// add at least one user
		userRepository.deleteAll();
		userRepository.save(dummyUser);
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	public void getProductResourceAndAssertResponseCode(String username,
			ObjectId productId, int responseCode) {
		try {
			productRepository.deleteAll();
			productRepository.save(dummyProduct);
			Response response = null;
			target.register(new HttpBasicAuthFilter("lunayo", "qwertyui"));
			if (productId == null) {
				// get list of products
				response = target.path("users/" + username + "/products")
						.request(MediaType.APPLICATION_JSON)
						.get(Response.class);
			} else {
				// get specific product
				response = target
						.path("users/" + username + "/product/" + productId)
						.request(MediaType.APPLICATION_JSON)
						.get(Response.class);
			}

			assertThat(response.getStatus(), is(responseCode));
		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}

	public void getProductResourceAndAssertResponseCode(int responseCode) {
		getProductResourceAndAssertResponseCode(dummyUser.getUsername(), null,
				responseCode);
	}

	public void getProductResourceAndAssertResponseCode(ObjectId productId,
			int responseCode) {
		getProductResourceAndAssertResponseCode(dummyUser.getUsername(),
				productId, responseCode);
	}

	public void addProductToResourceAndAssertResponseCode(String username,
			Product product, int responseCode) {
		try {
			target.register(new HttpBasicAuthFilter("lunayo", "qwertyui"));
			Response response = target
					.path("users/" + username + "/product")
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(product, MediaType.APPLICATION_JSON),
							Response.class);
			assertThat(response.getStatus(), is(responseCode));
		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}

	public void addProductToResourceAndAssertResponseCode(Product product,
			int responseCode) {
		addProductToResourceAndAssertResponseCode(dummyUser.getUsername(),
				product, responseCode);
	}

	public void deleteProductFromResourceAndAssertResponseCode(String username,
			ObjectId productId, int responseCode) {
		try {
			productRepository.deleteAll();
			productRepository.save(dummyProduct);
			target.register(new HttpBasicAuthFilter("lunayo", "qwertyui"));
			Response response = target
					.path("users/" + username + "/product/" + productId)
					.request(MediaType.APPLICATION_JSON).delete();
			assertThat(response.getStatus(), is(responseCode));
		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}

	public void deleteProductFromResourceAndAssertResponseCode(
			ObjectId productId, int responseCode) {
		deleteProductFromResourceAndAssertResponseCode(dummyUser.getUsername(),
				productId, responseCode);
	}

	public void updateProductInResourceAndAssertResponseCode(String username,
			Product product, int responseCode) {
		try {
			productRepository.deleteAll();
			productRepository.save(dummyProduct);
			target.register(new HttpBasicAuthFilter("lunayo", "qwertyui"));
			Response response = target
					.path("users/" + username + "/product/" + product.getId())
					.request(MediaType.APPLICATION_JSON)
					.put(Entity.entity(product, MediaType.APPLICATION_JSON),
							Response.class);
			assertThat(response.getStatus(), is(responseCode));
		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}

	public void updateProductInResourceAndAssertResponseCode(Product product,
			int responseCode) {
		updateProductInResourceAndAssertResponseCode(dummyUser.getUsername(),
				product, responseCode);
	}

	@Test
	public void getProductsFromResourceAndCheckResponseCode() {
		getProductResourceAndAssertResponseCode(200);
	}

	@Test
	public void getProductFromResourceAndCheckResponseCode() {
		getProductResourceAndAssertResponseCode(dummyProduct.getId(), 200);
	}

	@Test
	public void addProductToResourceAndCheckResponseCode() {
		productRepository.deleteAll();
		addProductToResourceAndAssertResponseCode(dummyProduct, 200);
	}

	@Test
	public void addProductToResourceWithInvalidUserAndCheckResponseCode() {
		productRepository.deleteAll();
		addProductToResourceAndAssertResponseCode("lun", dummyProduct, 400);
	}

	@Test
	public void addProductToResourceWithNonExistedUserAndCheckResponseCode() {
		productRepository.deleteAll();
		addProductToResourceAndAssertResponseCode("lunaluna", dummyProduct, 404);
	}

	@Test
	public void addProductToResourceWithExistedProductAndCheckResponseCode() {
		productRepository.deleteAll();
		productRepository.save(dummyProduct);
		addProductToResourceAndAssertResponseCode(dummyProduct, 409);
	}

	@Test
	public void updateProductInResourceAndCheckResponseCode() {
		updateProductInResourceAndAssertResponseCode(dummyProduct, 200);
	}

	@Test
	public void updateProductInResourceWithNonExistedProductAndCheckResponseCode() {
		Product product = new Product("iPhone 4", "Description", iPhonePrice,
				"lunayo", new Point(15.123212, 61.654321));
		updateProductInResourceAndAssertResponseCode(product, 404);
	}

	@Test
	public void deleteProductFromResourceAndCheckResponseCode() {
		deleteProductFromResourceAndAssertResponseCode(dummyProduct.getId(),
				204);
	}

	@Test
	public void deleteProductFromResourceWithNonexistedProductAndCheckResponseCode() {
		deleteProductFromResourceAndAssertResponseCode(new ObjectId(), 404);
	}

}
