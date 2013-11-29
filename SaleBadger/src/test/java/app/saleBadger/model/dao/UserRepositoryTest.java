package app.saleBadger.model.dao;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import app.saleBadger.model.Role;
import app.saleBadger.model.User;
import app.saleBadger.model.dao.UserRepository;
import app.saleBadger.model.dao.config.SpringMongoConfig;

public class UserRepositoryTest {
	
	ApplicationContext context = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
    UserRepository userRepository = context.getBean(UserRepository.class);
	User user;
	
    @Before
	public void setUp() {
		user = new User("samatase", "123123123","Emmanouil",Role.ADMIN, "Samatas",
				"samatase@hotmail.com");
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
