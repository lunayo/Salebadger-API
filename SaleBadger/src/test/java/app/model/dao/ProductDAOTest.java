package app.model.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.hamcrest.CoreMatchers.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import app.test.springframework.model.Product;



public class ProductDAOTest {
	Product product;
	ProductDao productDao;
	
	@Before
	public void setUp(){
		product = new Product("Nokia","450");
		productDao = new ProductDaoImpl();
	}

	@Test
	public void emptyCollectiongetAllProductsReturnsEmptyList() {
		List<Product> allProducts = productDao.getAllProducts();
		assertThat(allProducts.size(),is(0));
	}
	
	@Test 
	public void collectionWithOneProductGetAllProductsListHasSizeOne(){
		int numberOfProducts = productDao.getAllProducts().size();
		productDao.addProduct(product);
		List<Product> updatedNumberOfProducts = productDao.getAllProducts();
		assertThat(updatedNumberOfProducts.size(),is(1));
		
	}

}
