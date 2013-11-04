package app.model.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.hamcrest.CoreMatchers.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import app.model.Product;

public class ProductCatalogueMongoTest {
	Product product;
	ProductCatalog productDao;

	@Before
	public void setUp() {
		product = new Product("Nokia", "450");
		productDao = new ProductCatalogMongo();
		
		productDao.clear();
	}

	@Test
	public void emptyCollectiongetAllProductsReturnsEmptyList() {
		List<Product> allProducts = productDao.getAllProducts();
		assertThat(allProducts.size(), is(0));
	}

	@Test
	public void collectionWithOneProductGetAllProductsListHasSizeOne() {
		int numberOfProducts = productDao.getAllProducts().size();
		
		productDao.addProduct(product);
		
		List<Product> updatedNumberOfProducts = productDao.getAllProducts();
		
		int expectedSize = numberOfProducts + 1;
		int size = updatedNumberOfProducts.size();
		assertThat(size, is(expectedSize));

	}

}
