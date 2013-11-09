package app.model;

import java.sql.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User")
public class User {
	@Id
	private String id;

	String username;
	String password;
	String firstName;
	String lastName;
	Date createdAt;
	Date modifiedAt;
	
}
