package app.model.dao2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import app.model.User;
import app.model.dao.config.SpringMongoConfig;

public class App {

	// @SuppressWarnings("deprecation")
	// @Bean
	// Mongo mongo() throws UnknownHostException {
	// return new Mongo("localhost");
	// }
	//
	// @Bean
	// MongoTemplate mongoTemplate(Mongo mongo) {
	// return new MongoTemplate(mongo, "gs-accessing-data-mongo");
	// }

	public static void main(String args[]) {
		ApplicationContext context = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
        UserRepositoryInterface userRepository = context.getBean(UserRepositoryInterface.class);
//	        User user = new User("a", "b", "c", "d", "e");
//	        
//	        repository.save(user);
////	        repository.save(new User("Alice2", "Smith","a","b","c"));
//	        
//	        System.out.println("Customers found with findAll():");
//	        System.out.println("-------------------------------");
//	        for (User user1 : repository.findAll()) {
//	            System.out.println(user1);
//	        }
//	        System.out.println();
//	        context.close();
	    
        
        User userToAdd = new User("sa__1m" ,"a2", "a3", "a4", "a5"); 
        
	    System.out.println(userRepository.count());
	    System.out.println(userRepository.save(userToAdd));
	    System.out.println(userRepository.count());
		
//		if(user != null) {
//			System.out.print("It works! :)");
//		}
	}
}
