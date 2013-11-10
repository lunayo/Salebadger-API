package app.model.dao;

import app.model.User;

public interface UserCatalog {

	void addUser(User user);

	long count();

	void clear();

	User find(String username);

}
