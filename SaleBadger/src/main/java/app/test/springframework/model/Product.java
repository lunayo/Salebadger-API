package app.test.springframework.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
 
@Document(collection = "users")
public class Product {
 
	public Product(String description, String price) {
		// TODO Auto-generated constructor stub
	}

	@Id
	private String id;
 
	String description;
 
	String price;
 
	//getter, setter, toString, Constructors
 
}
