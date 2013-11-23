package app.model;

import java.util.Arrays;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class Product {

	@Id
	private String id;

	private String name;

	private String ownerId;

	private Price price;

	private double[] location;

	private Date dateCreated;

	private Date dateModified;

	public Product(String name, Price price, String ownerId, double[] location) {
		this.name = name;
		this.price = price;
		this.location = location;
		this.ownerId = ownerId;
		dateCreated = new Date();

	}

	public Price getPrice() {
		return this.price;
	}

	public String getName() {
		return name;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", ownerId=" + ownerId
				+ ", price=" + price + ", location="
				+ Arrays.toString(location) + ", dateCreated=" + dateCreated
				+ ", dateModified=" + dateModified + "]";
	}

}
