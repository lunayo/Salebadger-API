package app.model.dao;

import java.util.List;

import org.springframework.data.mongodb.core.geo.Point;

import app.model.Product;

public interface ProductRepositoryCustom {
	List<Product> findNearby(Point point, int skip, int limit);

}
