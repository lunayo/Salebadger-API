package app.model.dao;

import java.util.List;

import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.repository.CrudRepository;

import app.model.Product;
import app.model.User;
import app.model.dao.config.SpringMongoConfig;

public interface ProductRepository extends CrudRepository<Product, String>, ProductRepositoryCustom{

	

}
