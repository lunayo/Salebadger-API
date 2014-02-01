package app.saleBadger.model;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import app.saleBadger.model.Role;
import app.saleBadger.model.User;

public class UserTest {
	
	private Locale gb;
	private Contact userContact;
	private User user;
	
	@Before
	public void setUp(){
		gb = new Locale("en", "GB");
		userContact = new Contact(gb.getCountry(), gb.getDisplayCountry(), "7446623997");
		user = new User("userName","password","samatase@hotmail.com",Role.USER,"Manos","Samatas", userContact);
	}
	@Test
	public void newUserHasADateCreatedField() {
		
		assertTrue(user.getDateCreated() instanceof Date);
	}
	
	@Test
	public void newUserHasADateModifiedField(){
		assertTrue(user.getDateModified() instanceof Date);
	}
	
	

}
