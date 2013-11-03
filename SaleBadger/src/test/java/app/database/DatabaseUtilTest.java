package app.database;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import static org.mockito.Mockito.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import app.model.Product;

public class DatabaseUtilTest {
	String dbURL = "localhost";
	int port = 27017;
	static Product product;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		// LUN
		// Product is a mock object
		// I can give the desired behaviour to the methods
		// See the lines below, there really are self-explanatory
		product = mock(Product.class);
		when(product.getPrice()).thenReturn("500");
		when(product.getName()).thenReturn("iPhone 5");
		when(product.getDescription()).thenReturn(
				"I phone 5 is better than WP 7!");
	}

	@Test
	public void insertProductAddsThisProduct() {
		DatabaseUtil du = new MongoDBUtil();
		du.connect(dbURL, port);
		int numberOfProducts = (int) du.countProducts();
		int expectedNumberOfProducts = numberOfProducts + 1;
		du.insertProduct(product);

		assertThat((int) du.countProducts(), is(expectedNumberOfProducts));

	}

}
