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
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.client.filter.HttpBasicAuthFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import app.model.Price;
import app.model.Product;
import app.model.Role;
import app.model.User;
import app.model.dao.ProductRepository;
import app.model.dao.UserRepository;
import app.model.dao.config.SpringMongoConfig;

public class ProductResourceTest {

	private final Currency gbp = Currency.getInstance(new Locale("en", "GB"));
	private final Price iPhonePrice = new Price(499, gbp.getCurrencyCode());
	private final Product dummyProduct = new Product("iPhone", "Description",
			iPhonePrice, "lunayo", new double[] { 15.123212, 61.654321 });
	private final User dummyUser = new User("lunayo", "qwertyui",
			"lun@codebadge.com", Role.ADMIN, "Iskandar", "Goh");
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

		// add at least one user
		userRepository.deleteAll();
		userRepository.save(dummyUser);
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	public void addProductToResourceAndAssertResponseCode(Product product,
			int responseCode) {
		try {
			target.register(new HttpBasicAuthFilter("lunayo", "qwertyui"));
			Response response = target
					.path("products")
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(product, MediaType.APPLICATION_JSON),
							Response.class);
			assertThat(response.getStatus(), is(responseCode));
		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}

	public void deleteProductFromResourceAndAssertResponseCode(
			ObjectId productId, int responseCode) {
		try {
			productRepository.deleteAll();
			productRepository.save(dummyProduct);
			target.register(new HttpBasicAuthFilter("lunayo", "qwertyui"));
			Response response = target.path("products/" + productId)
					.request(MediaType.APPLICATION_JSON).delete();
			assertThat(response.getStatus(), is(responseCode));
		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}

	public void updateProductInResourceAndAssertResponseCode(Product product,
			int responseCode) {
		try {
			productRepository.deleteAll();
			productRepository.save(dummyProduct);
			target.register(new HttpBasicAuthFilter("lunayo", "qwertyui"));
			Response response = target
					.path("products/" + product.getId())
					.request(MediaType.APPLICATION_JSON)
					.put(Entity.entity(product, MediaType.APPLICATION_JSON),
							Response.class);
			assertThat(response.getStatus(), is(responseCode));
		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}

	@Test
	public void addProductToResourceAndCheckResponseCode() {
		productRepository.deleteAll();
		addProductToResourceAndAssertResponseCode(dummyProduct, 200);
	}
	
	@Test
	public void updateProductInResourceAndCheckResponseCode() {
		updateProductInResourceAndAssertResponseCode(dummyProduct, 200);
	}
	
	@Test
	public void deleteProductFromResourceAndCheckResponseCode() {
		deleteProductFromResourceAndAssertResponseCode(dummyProduct.getId(), 204);
	}

}
