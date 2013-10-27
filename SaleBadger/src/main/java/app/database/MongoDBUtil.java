package app.database;

import java.net.UnknownHostException;

import org.bson.BasicBSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import app.model.Product;

public class MongoDBUtil implements DatabaseUtil {

	MongoClient mongoClient;

	@Override
	public void connect(String connectionString, int port) {

		try {
			mongoClient = new MongoClient(connectionString, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void insertProduct(Product product) {
		// TODO Auto-generated method stub
		DB database = mongoClient.getDB("saleBadger");
		DBCollection productsCollection = database.getCollection("products");

		BasicDBObject productToInsert = new BasicDBObject("name",
				product.getName()).append("price", product.getPrice()).append(
				"description", product.getDescription());

		productsCollection.insert(productToInsert);

	}

	@Override
	public long countProducts() {
		DB database = mongoClient.getDB("saleBadger");
		DBCollection productsCollection = database.getCollection("products");
		return productsCollection.count();
		
	}

}
