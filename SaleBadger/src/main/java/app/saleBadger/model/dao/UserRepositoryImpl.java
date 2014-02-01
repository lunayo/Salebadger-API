package app.saleBadger.model.dao;

import java.util.List;

import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import app.saleBadger.model.User;

@Provider
public class UserRepositoryImpl implements UserRepositoryCustom {

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public List<User> findByQuery(String keyword, Point location, int skip,
			int limit) {
		Query query = new Query();
		if (keyword != null && !keyword.isEmpty()) {
			query.addCriteria(new Criteria().orOperator(
					Criteria.where("username").regex(keyword, "i"), Criteria
							.where("firstName").regex(keyword, "i"), Criteria
							.where("lastName").regex(keyword, "i"), Criteria
							.where("email").regex(keyword, "i")));
		}
		if (location != null) {
			query.addCriteria(Criteria.where("location").nearSphere(location));
		}
		query.skip(skip).limit(limit);
		return mongoTemplate.find(query, User.class);
	}

}
