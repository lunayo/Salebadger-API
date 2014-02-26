package app.saleBadger.model;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.bson.types.ObjectId;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.mapping.Document;

import app.saleBadger.model.constraints.LocationIsValid;
import app.saleBadger.model.serializer.LocationDeserializer;
import app.saleBadger.model.serializer.LocationSerializer;
import app.saleBadger.model.serializer.ObjectIdSerializer;

@Document(collection = "products")
public class Product {

	@Id
	@JsonSerialize(using = ObjectIdSerializer.class)
	private ObjectId id;
	@Size(min = 5)
	private String name;
	@NotEmpty
	private String description;
	private String ownerId;
	@NotNull
	@Valid
	private Price price;
	@NotNull
	@LocationIsValid
	@JsonSerialize(using = LocationSerializer.class)
	@JsonDeserialize(using = LocationDeserializer.class)
	private Point location;
	private Date dateCreated;
	private Date dateModified;

	public Product() {
	}

	public Product(String name, String description, Price price,
			String ownerId, Point location) {
		this.id = new ObjectId();
		this.name = name;
		this.description = description;
		this.price = price;
		this.location = location;
		this.ownerId = ownerId;
		this.dateCreated = new Date();
		this.dateModified = new Date();
	}

	public static Point getLocation(String location) {
		String[] locationString = location.split(",");

		if (locationString.length != 2) {
			return null;
		}

		Point locations = new Point(Double.parseDouble(locationString[0]),
				Double.parseDouble(locationString[1]));

		return locations;
	}

	public void setId(ObjectId id) {
		this.id = id;
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

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setDateCreated() {
		this.dateCreated = new Date();
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void updateDateModified() {
		this.dateModified = new Date();
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", description="
				+ description + ", ownerId=" + ownerId + ", price=" + price
				+ ", location=" + location + ", dateCreated=" + dateCreated
				+ ", dateModified=" + dateModified + "]";
	}

}
