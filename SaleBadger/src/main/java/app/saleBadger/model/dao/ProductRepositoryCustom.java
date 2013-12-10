package app.saleBadger.model.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.mongodb.core.geo.Point;

import app.saleBadger.model.Product;

public interface ProductRepositoryCustom {
	List<Product> findNearby(Point point, int skip, int limit);
	List<Product> findByUsername(String username);
	List<Product> findByQuery(HashMap<String, Object> params);

}
