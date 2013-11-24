package app.model.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.model.Product;

public interface ProductRepository extends CrudRepository<Product, String>, ProductRepositoryCustom{



	

}
