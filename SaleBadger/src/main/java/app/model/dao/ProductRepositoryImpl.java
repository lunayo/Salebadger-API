package app.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import app.model.Product;
import app.model.dao.config.SpringMongoConfig;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

	@Autowired MongoTemplate mongoTemplate;
	@Override
	public List<Product> findNearby(Point point, int skip, int limit) {

		List<Product> nearestProducts = 
			    mongoTemplate.find(new Query(Criteria.where("location").nearSphere(point)).skip(skip).limit(limit), Product.class);
		return nearestProducts;
		
	}

}
