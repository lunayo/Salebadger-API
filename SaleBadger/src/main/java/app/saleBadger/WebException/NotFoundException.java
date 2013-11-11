package app.saleBadger.WebException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import app.model.Validator.ValidationError;

public class NotFoundException extends WebApplicationException {

	private static final long serialVersionUID = 1L;

	public NotFoundException(String objectName) {
		this(objectName, "not found");
	}

	public NotFoundException(String objectName, String message) {
		super(Response
				.status(Response.Status.NOT_FOUND)
				.entity(new ValidationError(objectName + " " + message,
						Response.Status.NOT_FOUND.getStatusCode()))
				.type(MediaType.APPLICATION_JSON).build());
	}

}