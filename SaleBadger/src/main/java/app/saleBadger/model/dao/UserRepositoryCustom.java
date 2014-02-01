package app.saleBadger.model.dao;

import java.util.List;

import org.springframework.data.mongodb.core.geo.Point;

import app.saleBadger.model.User;

public interface UserRepositoryCustom {
	List<User> findByQuery(String keyword, Point location, int skip, int limit);
}
