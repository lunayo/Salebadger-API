package app.saleBadger.model.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import app.saleBadger.model.Contact;
import app.saleBadger.model.Role;
import app.saleBadger.model.User;
import app.saleBadger.model.dao.config.SpringMongoConfig;

public class UserRepositoryTest {
	
	private ApplicationContext context = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
	private UserRepository userRepository = context.getBean(UserRepository.class);
    private Locale gb;
    private Contact userContact;
    private User user;
	
    @Before
	public void setUp() {
    	gb = new Locale("en", "GB");
    	userContact = new Contact(gb.getCountry(), gb.getDisplayCountry(), "7446653997");
		user = new User("samatase", "123123123","Emmanouil",Role.ADMIN, "Samatas",
				"samatase@hotmail.com", userContact);
		userRepository.deleteAll();

	}

    
	@Test
	public void addOneUser() {
		long emptyDb = userRepository.count();
		userRepository.save(user);
		long newSize = userRepository.count();
		assertThat(newSize, is(emptyDb + 1));
		
	}
	
	

}
