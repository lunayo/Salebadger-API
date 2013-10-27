package app.database;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

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
		product = new Product("iPhone 5", 550, "Apple smartphone");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
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
