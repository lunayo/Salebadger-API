package app.saleBadger.model.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import app.saleBadger.model.Product;

public interface ProductRepository extends MongoRepository<Product, String>, ProductRepositoryCustom {

}
