package app.model.dao;

import org.springframework.context.annotation.Bean;

import app.model.User;


public class UserRepositoryImpl implements UserRepositoryCustom{

	public void print(User user) {

		System.out.println("LoL");
		
	}

}
