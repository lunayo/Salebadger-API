package app.model.dao;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import app.model.Product;
import app.model.dao.config.SpringMongoConfig;

import com.mongodb.MongoClient;

public class ProductCatalogMongo implements ProductCatalog {

	// DataSource dataSource = new MongoDataSource();
	MongoClient mongoClient;

	ApplicationContext ctx = new AnnotationConfigApplicationContext(
			SpringMongoConfig.class);
	MongoOperations mongoOperation = (MongoOperations) ctx
			.getBean("mongoTemplate");

	@Override
	public List<Product> getAllProducts() {
		List<Product> productList = mongoOperation.findAll(Product.class);
		return productList;
	}

	@Override
	public void addProduct(Product product) {
		mongoOperation.save(product);
	}

	@Override
	public void clear() {
		mongoOperation.dropCollection(Product.class);

	}

}
