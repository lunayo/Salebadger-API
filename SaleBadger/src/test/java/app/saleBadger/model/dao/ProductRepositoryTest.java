package app.saleBadger.model.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.geo.Point;

import app.saleBadger.model.Price;
import app.saleBadger.model.Product;
import app.saleBadger.model.User;
import app.saleBadger.model.dao.config.SpringMongoConfig;

public class ProductRepositoryTest {

	ApplicationContext context = new AnnotationConfigApplicationContext(
			SpringMongoConfig.class);
	ProductRepository productRepository = context
			.getBean(ProductRepository.class);
	Product product;
	Random random;
	Locale englandLocale = new Locale("en", "GB");
	Currency gbp = Currency.getInstance(englandLocale);
	Price iPhonePrice = new Price(499, gbp.getCurrencyCode());
	User mockUser;

	final double MIN_LAT = -90;
	final double MAX_LAT = 90;
	final double MIN_LONG = -180;
	final double MAX_LONG = 180;

	@Before
	public void setUp() {
		List<Double> location = Arrays.asList(15.123212, 61.654321);

		product = new Product("iPhone", "Description", iPhonePrice, "samatase",
				location);
		productRepository.deleteAll();

		mockUser = mock(User.class);
		when(mockUser.getUsername()).thenReturn("samatase");

	}

	@Test
	public void addOneProduct() {
		long emptySize = productRepository.count();
		productRepository.save(product);
		long actualSize = productRepository.count();

		assertThat(actualSize, is(emptySize + 1));
	}

	@Test
	public void retrieveNearestProductsReturnAListOfProducts() {
		Point point = new Point(10.323232, 21.123456);
		productRepository.save(product);
		int limit = 10;
		int skip = 0;
		List<Product> nearestProducts = productRepository.findNearby(point,
				skip, limit);
		assertThat(nearestProducts.isEmpty(), is(false));
	}

	@Test
	public void retrieveNearestProductsLimitTheResults() {
		// add 100 random products
		for (int i = 0; i < 100; i++) {
			Product productToAdd = new Product("dummy " + i, "Description",
					iPhonePrice, "samatase", getRandomLocation());
			productRepository.save(productToAdd);
		}
		int skip = 0;
		int limit = 10;
		Point point = new Point(10.323232, 21.123456);
		List<Product> nearestProducts = productRepository.findNearby(point,
				skip, limit);
		assertThat(nearestProducts.size(), is(limit));
	}

	@Test
	public void addManyProductsQueryByUserName() {
		for (int i = 0; i < 10; i++) {
			Product productToAdd = new Product("dummy " + i, "Description",
					iPhonePrice, mockUser.getUsername(), getRandomLocation());
			productRepository.save(productToAdd);
		}
		for (int i = 0; i < 20; i++) {
			Product productToAdd = new Product("other " + i, "Description",
					iPhonePrice, "thisIsCrap", getRandomLocation());
			productRepository.save(productToAdd);
		}

		List<Product> products = productRepository.findByUsername(mockUser
				.getUsername());
		assertThat(products.size(), is(10));

	}

	private List<Double> getRandomLocation() {
		Random random = new Random();

		double randomLat = MIN_LAT + (MAX_LAT - MIN_LAT) * random.nextDouble();
		double randomLong = MIN_LONG + (MAX_LONG - MIN_LONG)
				* random.nextDouble();
		List<Double> randomLocation = Arrays.asList(randomLong, randomLat);
		return randomLocation;
	}

}
