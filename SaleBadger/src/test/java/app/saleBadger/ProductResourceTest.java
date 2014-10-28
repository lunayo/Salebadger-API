package app.saleBadger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collections;
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
import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
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
			iPhonePrice, "lunayo", new Point(15.123212, 61.654321),
			Collections.<String> emptyList());
	private final Contact userContact = new Contact(gb.getCountry(),
			gb.getDisplayCountry(), "7446653997");
	private final User dummyAdmin = new User("lunayo", "qwertyui",
			"lun@codebadge.com", Role.ADMIN, "Iskandar", "Goh", userContact);
	private final User dummyUser = new User("lunayo", "qwertyui",
			"lun@codebadge.com", Role.USER, "Iskandar", "Goh", userContact);
	private final ApplicationContext context = new AnnotationConfigApplicationContext(
			SpringMongoConfig.class);
	private final UserRepository userRepository = context
			.getBean(UserRepository.class);
	private final ProductRepository productRepository = context
			.getBean(ProductRepository.class);
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
		c.register(MultiPartFeature.class);
		c.register(new LoggingFilter());

		target = c.target(Main.BASE_URI);

	}

	@AfterClass
	public static void tearDown() throws Exception {
		server.shutdown();
	}

	public void getProductResourceAndAssertResponseCode(String username,
			ObjectId productId, int responseCode) {
		try {
			productRepository.deleteAll();
			productRepository.save(dummyProduct);
			Response response = null;
			target.register(HttpAuthenticationFeature.basic("lunayo",
					"qwertyui"));
			if (productId == null) {
				// get list of products
				response = target.path("v1/user/" + username + "/product")
						.request(MediaType.APPLICATION_JSON)
						.get(Response.class);
			} else {
				// get specific product
				response = target
						.path("v1/user/" + username + "/product/" + productId)
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
		getProductResourceAndAssertResponseCode(dummyAdmin.getUsername(), null,
				responseCode);
	}

	public void getProductResourceAndAssertResponseCode(ObjectId productId,
			int responseCode) {
		getProductResourceAndAssertResponseCode(dummyAdmin.getUsername(),
				productId, responseCode);
	}

	public void addProductToResourceAndAssertResponseCode(String username,
			File[] images, Product product, int responseCode) {
		try {
			target.register(HttpAuthenticationFeature.basic("lunayo",
					"qwertyui"));
			final FormDataMultiPart mp = new FormDataMultiPart();
			// Construct multiple part object
			ObjectMapper mapper = new ObjectMapper();
			FormDataBodyPart p = new FormDataBodyPart("product",
					mapper.writeValueAsString(product));
			mp.bodyPart(p);
			if (images != null) {
				// append images to the form
				for (File image : images) {
					FormDataBodyPart fdp = new FormDataBodyPart("image", image,
							MediaType.APPLICATION_OCTET_STREAM_TYPE);
					mp.bodyPart(fdp);
				}
			}
			Response response = target
					.path("v1/user/" + username + "/product")
					.request(MediaType.APPLICATION_JSON)
					.post(Entity.entity(mp, MediaType.MULTIPART_FORM_DATA),
							Response.class);
			assertThat(response.getStatus(), is(responseCode));
		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}

	public void addProductToResourceAndAssertResponseCode(String username,
			Product product, int responseCode) {
		addProductToResourceAndAssertResponseCode(username, null, product,
				responseCode);
	}

	public void addProductToResourceAndAssertResponseCode(Product product,
			File[] files, int responseCode) {
		addProductToResourceAndAssertResponseCode(dummyAdmin.getUsername(),
				files, product, responseCode);
	}

	public void addProductToResourceAndAssertResponseCode(Product product,
			int responseCode) {
		addProductToResourceAndAssertResponseCode(dummyAdmin.getUsername(),
				product, responseCode);
	}

	public void deleteProductFromResourceAndAssertResponseCode(String username,
			ObjectId productId, int responseCode) {
		try {
			productRepository.deleteAll();
			productRepository.save(dummyProduct);
			target.register(HttpAuthenticationFeature.basic("lunayo",
					"qwertyui"));
			Response response = target
					.path("v1/user/" + username + "/product/" + productId)
					.request(MediaType.APPLICATION_JSON).delete();
			assertThat(response.getStatus(), is(responseCode));
		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException();
		}
	}

	public void deleteProductFromResourceAndAssertResponseCode(
			ObjectId productId, int responseCode) {
		deleteProductFromResourceAndAssertResponseCode(
				dummyAdmin.getUsername(), productId, responseCode);
	}

	public void updateProductInResourceAndAssertResponseCode(String username,
			Product product, int responseCode) {
		try {
			productRepository.deleteAll();
			productRepository.save(dummyProduct);
			target.register(HttpAuthenticationFeature.basic("lunayo",
					"qwertyui"));
			Response response = target
					.path("v1/user/" + username + "/product/" + product.getId())
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
		updateProductInResourceAndAssertResponseCode(dummyAdmin.getUsername(),
				product, responseCode);
	}

	@Test
	public void getProductsFromResourceAndCheckResponseCode() {
		userRepository.deleteAll();
		userRepository.save(dummyAdmin);
		getProductResourceAndAssertResponseCode(200);
	}

	@Test
	public void getProductFromResourceAndCheckResponseCode() {
		userRepository.deleteAll();
		userRepository.save(dummyAdmin);
		getProductResourceAndAssertResponseCode(dummyProduct.getId(), 200);
	}

	@Test
	public void addProductToResourceAndCheckResponseCode() {
		userRepository.deleteAll();
		userRepository.save(dummyAdmin);
		productRepository.deleteAll();
		addProductToResourceAndAssertResponseCode(dummyProduct, 200);
	}

	@Test
	public void addProductToResourceWithInvalidUserAndCheckResponseCode() {
		userRepository.deleteAll();
		userRepository.save(dummyAdmin);
		productRepository.deleteAll();
		addProductToResourceAndAssertResponseCode("lun", dummyProduct, 400);
	}

	@Test
	public void addProductToResourceWithNonExistedUserAndCheckResponseCode() {
		userRepository.deleteAll();
		userRepository.save(dummyAdmin);
		productRepository.deleteAll();
		addProductToResourceAndAssertResponseCode("lunaluna", dummyProduct, 404);
	}

	@Test
	public void addProductWithImageToResourceAndAssertResponseCode() throws URISyntaxException {
		userRepository.deleteAll();
		userRepository.save(dummyAdmin);
		productRepository.deleteAll();
		// Get test images from resources folder
		File[] images = new File[2];
		images[0] = new File(getClass().getResource("/images/IMG_0300.JPG").toURI());
		images[1] = new File(getClass().getResource("/images/IMG_0303.JPG").toURI());
		addProductToResourceAndAssertResponseCode(dummyProduct, images, 200);
	}

	@Test
	public void addProductToResourceWithExistedProductAndCheckResponseCode() {
		userRepository.deleteAll();
		userRepository.save(dummyAdmin);
		productRepository.deleteAll();
		productRepository.save(dummyProduct);
		addProductToResourceAndAssertResponseCode(dummyProduct, 409);
	}

	@Test
	public void updateProductInResourceAndCheckResponseCode() {
		userRepository.deleteAll();
		userRepository.save(dummyAdmin);
		updateProductInResourceAndAssertResponseCode(dummyProduct, 200);
	}

	@Test
	public void updateProductInResourceWithNonExistedProductAndCheckResponseCode() {
		userRepository.deleteAll();
		userRepository.save(dummyAdmin);
		Product product = new Product("iPhone 4", "Description", iPhonePrice,
				"lunayo", new Point(15.123212, 61.654321),
				Collections.<String> emptyList());
		updateProductInResourceAndAssertResponseCode(product, 404);
	}

	@Test
	public void deleteProductFromResourceAndCheckResponseCode() {
		userRepository.deleteAll();
		userRepository.save(dummyAdmin);
		deleteProductFromResourceAndAssertResponseCode(dummyProduct.getId(),
				204);
	}

	@Test
	public void deleteProductFromResourceWithNonexistedProductAndCheckResponseCode() {
		userRepository.deleteAll();
		userRepository.save(dummyAdmin);
		deleteProductFromResourceAndAssertResponseCode(new ObjectId(), 404);
	}

	@Test
	public void addProductToResourceWithInvalidPermissionAndCheckResponseCode() {
		// add at least one user
		userRepository.deleteAll();
		userRepository.save(dummyUser);
		productRepository.deleteAll();
		addProductToResourceAndAssertResponseCode("lun", dummyProduct, 403);
	}

	@Test
	public void updateProductToResourceWithInvalidPermissionAndCheckResponseCode() {
		// add at least one user
		userRepository.deleteAll();
		userRepository.save(dummyUser);
		productRepository.deleteAll();
		updateProductInResourceAndAssertResponseCode("lun", dummyProduct, 403);
	}

	@Test
	public void deleteProductToResourceWithInvalidPermissionAndCheckResponseCode() {
		// add at least one user
		userRepository.deleteAll();
		userRepository.save(dummyUser);
		productRepository.deleteAll();
		deleteProductFromResourceAndAssertResponseCode("lun",
				dummyProduct.getId(), 403);
	}

}
