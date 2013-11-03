package app.test.springframework.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
 
@Document(collection = "products")
public class Product {
 
	public Product(String description, String price) {
		this.description = description;
		this.price = price;
	}

	@Id
	private String id;
 
	String description;
 
	String price;
 
	//getter, setter, toString, Constructors
 
}
