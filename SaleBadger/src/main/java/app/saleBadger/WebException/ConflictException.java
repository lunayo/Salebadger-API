package app.saleBadger.WebException;

import java.net.URI;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import app.model.Validator.ValidationError;

public class ConflictException extends WebApplicationException {

	private static final long serialVersionUID = 1L;

	public ConflictException(List<String> messages, URI location) {
		super(Response.status(Response.Status.CONFLICT)
				.entity(getValidationList(messages)).location(location)
				.type(MediaType.APPLICATION_JSON).build());
	}

	public static ValidationError getValidationList(List<String> errorMessages) {
		ValidationError validationError = new ValidationError();
		for (String message : errorMessages) {
			validationError.addError(message,
					Response.Status.CONFLICT.getStatusCode());
		}

		return validationError;
	}

}
