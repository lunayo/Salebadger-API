package app.saleBadger.model.dao;

import javax.ws.rs.ext.Provider;

import app.saleBadger.model.User;

@Provider
public class UserRepositoryImpl implements UserRepositoryCustom{

	public void print(User user) {

		System.out.println("LoL");
		
	}

}
