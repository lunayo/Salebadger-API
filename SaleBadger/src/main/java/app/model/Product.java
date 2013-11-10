package app.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
 
@Document(collection = "products")
public class Product {
	
	@Id
	private String id;
 
	private String description;
 
	private String price;
 
	public Product(String description, String price) {
		this.description = description;
		this.price = price;
	}

	public String getPrice() {
		// TODO Auto-generated method stub
		return this.price;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return "test name";
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return this.description;
	}


 
	//getter, setter, toString, Constructors
 
}
