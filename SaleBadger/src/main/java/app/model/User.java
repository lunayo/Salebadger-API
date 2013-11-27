package app.model;

import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import app.model.constraints.EmailIsValid;
import app.saleBadger.authentication.UserAuthentication;

@Document(collection = "users")
public class User {

	@Id
	@Size(min = 6, max = 20)
	private String username;
	@Size(min = 6)
	private String password;
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	@NotBlank
	@EmailIsValid
	private String email;
	private Date dateCreated;
	private Date dateModified;
	private String role;
	
	public User() {
	}

	public User(String username, String password, String email, String role,
			String firstName, String lastName) {
		super();
		this.username = username;
		this.setPassword(password);
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateCreated = new Date();
		this.dateModified = new Date();
		this.role = role;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setUsername(String username) {
		this.username = username;
		this.updateDateModified();
	}

	public void setPassword(String password) {
		this.password = UserAuthentication
				.getSaltedHashPassword(password);
		this.updateDateModified();
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
		this.updateDateModified();
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
		this.updateDateModified();
	}

	public void setEmail(String email) {
		this.email = email;
		this.updateDateModified();
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}
	
	public String getRole(){
		return role;
	}
	

	private void updateDateModified() {
		this.dateModified = new Date();
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
