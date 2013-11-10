package app.model.dao;

import app.model.User;

public interface UserCatalog {

	void addUser(User user);

	long count();

}
