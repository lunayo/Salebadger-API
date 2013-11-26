package app.model;

import java.util.Arrays;
import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class Product {

	@Id 
	private ObjectId id;

	private String name;
	
	private String description;

	private String ownerId;

	private Price price;

	private double[] location;

	private Date dateCreated;

	private Date dateModified;
	
	public Product() {
		
	}

	public Product(String name, String description, Price price, String ownerId, double[] location) {
		this.id = new ObjectId();
		this.name = name;
		this.description = description;
		this.price = price;
		this.location = location;
		this.ownerId = ownerId;
		dateCreated = new Date();

	}

	public ObjectId getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public double[] getLocation() {
		return location;
	}

	public void setLocation(double[] location) {
		this.location = location;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", description="
				+ description + ", ownerId=" + ownerId + ", price=" + price
				+ ", location=" + Arrays.toString(location) + ", dateCreated="
				+ dateCreated + ", dateModified=" + dateModified + "]";
	}

}
