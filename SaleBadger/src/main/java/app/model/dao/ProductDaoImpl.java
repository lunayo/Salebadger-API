package app.model.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import app.model.Product;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class ProductDaoImpl implements ProductDao {

//	DataSource dataSource = new MongoDataSource();
	MongoClient mongoClient;
	
	@Override
	public List<Product> getAllProducts() {
		List<Product> products = new ArrayList<Product>();
		try {
			mongoClient = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DB database = mongoClient.getDB("saleBadger");
		DBCollection productsCollection = database.getCollection("products");


		return products;
	}

	@Override
	public void addProduct(Product product) {
		try {
			mongoClient = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DB database = mongoClient.getDB("saleBadger");
		DBCollection productsCollection = database.getCollection("products");

		BasicDBObject productToInsert = new BasicDBObject("name",
				product.getName()).append("price", product.getPrice()).append(
				"description", product.getDescription());

		productsCollection.insert(productToInsert);

		
	}

}
