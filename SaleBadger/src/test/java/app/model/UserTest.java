package app.model;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class UserTest {
	
	
	User user;
	
	@Before
	public void setUp(){
		user = new User("userName","password","samatase@hotmail.com","Manos","Samatas");
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
