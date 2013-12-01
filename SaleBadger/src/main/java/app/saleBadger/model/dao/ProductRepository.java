package app.saleBadger.model.dao;

import org.springframework.data.repository.CrudRepository;

import app.saleBadger.model.Product;

public interface ProductRepository extends CrudRepository<Product, String>, ProductRepositoryCustom {

}
