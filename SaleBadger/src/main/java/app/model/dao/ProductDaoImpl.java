package app.model.dao;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

//import app.model.Product;
import app.test.springframework.SpringMongoConfig;
import app.test.springframework.model.Product;

import com.mongodb.MongoClient;

public class ProductDaoImpl implements ProductDao {

	// DataSource dataSource = new MongoDataSource();
	MongoClient mongoClient;

	ApplicationContext ctx = new AnnotationConfigApplicationContext(
			SpringMongoConfig.class);
	MongoOperations mongoOperation = (MongoOperations) ctx
			.getBean("mongoTemplate");

	@Override
	public List<Product> getAllProducts() {
		// List<Product> products = new ArrayList<Product>();
		// try {
		// mongoClient = new MongoClient("localhost", 27017);
		// } catch (UnknownHostException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// DB database = mongoClient.getDB("saleBadger");
		// DBCollection productsCollection = database.getCollection("products");
		//
		//
		// return products;

		List<Product> productList = mongoOperation.findAll(Product.class);
		return productList;
	}

	@Override
	public void addProduct(Product product) {
		// try {
		// mongoClient = new MongoClient("localhost", 27017);
		// } catch (UnknownHostException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// DB database = mongoClient.getDB("saleBadger");
		// DBCollection productsCollection = database.getCollection("products");
		//
		// BasicDBObject productToInsert = new BasicDBObject("name",
		// product.getName()).append("price", product.getPrice()).append(
		// "description", product.getDescription());
		//
		// productsCollection.insert(productToInsert);
		
		mongoOperation.save(product);
	}

}
