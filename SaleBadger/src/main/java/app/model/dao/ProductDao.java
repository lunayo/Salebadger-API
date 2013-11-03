package app.model.dao;

import java.util.List;

import app.test.springframework.model.Product;



public interface ProductDao {

	List<Product> getAllProducts();

	void addProduct(Product product);

}
