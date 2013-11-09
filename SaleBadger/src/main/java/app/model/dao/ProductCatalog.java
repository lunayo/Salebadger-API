package app.model.dao;

import java.util.List;

import app.model.Product;



public interface ProductCatalog {

	List<Product> getAllProducts();

	void addProduct(Product product);

	void clear();

}
