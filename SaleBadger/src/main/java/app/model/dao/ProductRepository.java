package app.model.dao;

import org.springframework.data.repository.CrudRepository;

import app.model.Product;

public interface ProductRepository extends CrudRepository<Product, String>, ProductRepositoryCustom{

	

}
