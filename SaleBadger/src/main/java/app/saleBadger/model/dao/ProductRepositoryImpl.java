package app.saleBadger.model.dao;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import app.saleBadger.model.Product;

@Provider
public class ProductRepositoryImpl implements ProductRepositoryCustom {

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public List<Product> findNearby(Point point, int skip, int limit) {

		List<Product> nearestProducts = mongoTemplate.find(new Query(Criteria
				.where("location").nearSphere(point)).skip(skip).limit(limit),
				Product.class);
		return nearestProducts;

	}

	@Override
	public List<Product> findByUsername(String username) {
		return mongoTemplate.find(
				new Query(Criteria.where("ownerId").is(username)),
				Product.class);

	}

	@Override
	public List<Product> findByQuery(HashMap<String, Object> params) {
		Query query = new Query();
		for (String key : params.keySet()) {
			query.addCriteria(Criteria.where(key).is(params.get(key)));
		}
		return mongoTemplate.find(query, Product.class);
	}

	@Override
	public List<Product> findByQuery(String keyword, Point location, int skip,
			int limit) {
		Query query = new Query();
		if (keyword != null && !keyword.isEmpty()) {
			query.addCriteria(new Criteria().orOperator(
					Criteria.where("description").regex(keyword, "i"), Criteria
							.where("name").regex(keyword, "i")));
		}
		if (location != null) {
			query.addCriteria(Criteria.where("location").nearSphere(location));
		}
		query.skip(skip).limit(limit);
		return mongoTemplate.find(query, Product.class);
	}
}
