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
import org.glassfish.jersey.client.filter.HttpBasicAuthFilter;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import app.saleBadger.model.Price;
import app.saleBadger.model.Product;
import app.saleBadger.model.dao.ProductRepository;
import app.saleBadger.model.dao.config.SpringMongoConfig;

public class SearchResourceTest {

	private final Currency gbp = Currency.getInstance(new Locale("en", "GB"));
	private final Price iPhonePrice = new Price(499, gbp.getCurrencyCode());
	private final Product dummyProduct = new Product("iPhone", "Description",
			iPhonePrice, "lunayo", Arrays.asList(15.123212, 61.654321));
	private final ApplicationContext context = new AnnotationConfigApplicationContext(
			SpringMongoConfig.class);
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
		getProductResourceAndAssertResponseCode(keyword, null, responseCode);
	}

	public void getProductResourceAndAssertResponseCode(List<Double> location,
			int responseCode) {
		getProductResourceAndAssertResponseCode(null, location, responseCode);
	}

	public void getProductResourceAndAssertResponseCode(String keyword,
			List<Double> location, int responseCode) {
		try {
			productRepository.deleteAll();
			productRepository.save(dummyProduct);
			Response response = null;
			Invocation.Builder invocationBuilder = null;
			target.register(new HttpBasicAuthFilter("lunayo", "qwertyui"));
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

			assertThat(response.getStatus(), is(responseCode));
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
}
