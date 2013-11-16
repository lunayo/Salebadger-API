package app.model.dao2;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import app.model.User;

import com.mongodb.Mongo;

@Configuration
@EnableMongoRepositories
public class App {

	@Autowired
	UserRepositoryInterface userRepository;

	@SuppressWarnings("deprecation")
	@Bean
	Mongo mongo() throws UnknownHostException {
		return new Mongo("localhost");
	}

	@Bean
	MongoTemplate mongoTemplate(Mongo mongo) {
		return new MongoTemplate(mongo, "gs-accessing-data-mongo");
	}

	public static void main(String args[]) {
		 AbstractApplicationContext context = new AnnotationConfigApplicationContext(App.class);
	        UserRepositoryInterface repository = context.getBean(UserRepositoryInterface.class);
	        User user = new User("a", "b", "c", "d", "e");
	        
	        repository.save(user);
//	        repository.save(new User("Alice2", "Smith","a","b","c"));
	        
	        System.out.println("Customers found with findAll():");
	        System.out.println("-------------------------------");
	        for (User user1 : repository.findAll()) {
	            System.out.println(user1);
	        }
	        System.out.println();
	        context.close();
	        

	}

}
