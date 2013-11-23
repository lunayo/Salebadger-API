package app.model;

import java.util.Arrays;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class Product {

	@Id
	private String id;

	private String description;
	
	private String owner;

	private Price price;

	private double[] location;

	public Product(String description, Price price, double[] location) {
		this.description = description;
		this.price = price;
		this.location = location;
		
	}
	
	
//
//	public Product(String description, String owner, String price,
//			double[] location) {
//		super();
//		this.description = description;
//		this.owner = owner;
//		this.price = price;
//		this.location = location;
//	}




	public Price getPrice() {
		return this.price;
	}

	public String getName() {
		return "test name";
	}

	public String getDescription() {
		return this.description;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", description=" + description
				+ ", price=" + price + ", location="
				+ Arrays.toString(location) + "]";
	}

	

}
