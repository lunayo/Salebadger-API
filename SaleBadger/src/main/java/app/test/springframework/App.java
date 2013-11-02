package app.test.springframework;


 
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import app.test.springframework.model.Product;

//import org.springframework.context.support.GenericXmlApplicationContext;
 
public class App {
 
    public static void main(String[] args) {
 
	// For XML
//	ApplicationContext ctx = new GenericXmlApplicationContext("SpringConfig.xml");
 
	// For Annotation
	ApplicationContext ctx = 
             new AnnotationConfigApplicationContext(SpringMongoConfig.class);
	MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
 
	Product product = new Product("nokia", "500");
 
	// save
	mongoOperation.save(product);
 
	// now user object got the created id.
	System.out.println("1. user : " + product);
 
	// query to search user
	Query searchUserQuery = new Query(Criteria.where("username").is("mkyong"));
 
	// find the saved user again.
	Product savedUser = mongoOperation.findOne(searchUserQuery, Product.class);
	System.out.println("2. find - savedUser : " + savedUser);
 
	// update password
	mongoOperation.updateFirst(searchUserQuery, 
                         Update.update("password", "new password"),Product.class);
 
	// find the updated user object
	Product updatedUser = mongoOperation.findOne(searchUserQuery, Product.class);
 
	System.out.println("3. updatedUser : " + updatedUser);
 
	// delete
	mongoOperation.remove(searchUserQuery, Product.class);
 
	// List, it should be empty now.
	List<Product> listUser = mongoOperation.findAll(Product.class);
	System.out.println("4. Number of user = " + listUser.size());
 
    }
 
}