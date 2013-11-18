package app.saleBadger.WebException;

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import app.model.Validator.ValidationError;

public class NotFoundException extends WebApplicationException {

	private static final long serialVersionUID = 1L;

	public NotFoundException(List<String> messages) {
		super(Response.status(Response.Status.NOT_FOUND)
				.entity(getValidationList(messages))
				.type(MediaType.APPLICATION_JSON).build());
	}

	public static ValidationError getValidationList(List<String> errorMessages) {
		ValidationError validationError = new ValidationError();
		for (String message : errorMessages) {
			validationError.addError(message,
					Response.Status.NOT_FOUND.getStatusCode());
		}

		return validationError;
	}

}