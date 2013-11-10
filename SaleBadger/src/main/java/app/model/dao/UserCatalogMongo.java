package app.model.dao;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

import app.model.User;
import app.model.dao.config.SpringMongoConfig;

public class UserCatalogMongo implements UserCatalog {

	private static UserCatalogMongo instance = new UserCatalogMongo();

	private ApplicationContext ctx = new AnnotationConfigApplicationContext(
			SpringMongoConfig.class);
	private MongoOperations mongoOperation = (MongoOperations) ctx
			.getBean("mongoTemplate");

	public UserCatalogMongo() {

	}

	public static UserCatalogMongo getInstance() {
		return instance;
	}

	@Override
	public void add(User user) {
		mongoOperation.save(user);
	}

	@Override
	public long count() {
		Query query = new Query();
		return mongoOperation.count(query, User.class);
	}

	@Override
	public void clear() {
		mongoOperation.dropCollection(User.class);
		
	}

	@Override
	public User find(String username) {
		Query findByName = query(where("username").is(username));
		User user = mongoOperation.findOne(findByName, User.class);
		return user;
	}

}
