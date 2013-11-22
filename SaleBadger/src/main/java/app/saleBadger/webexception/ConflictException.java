package app.saleBadger.webexception;

import java.net.URI;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import app.saleBadger.validator.ErrorValidationMapper;

public class ConflictException extends WebApplicationException {

	private static final long serialVersionUID = 1L;

	public ConflictException(List<String> messages, URI location) {
		super(Response.status(Response.Status.CONFLICT)
				.entity(getValidationList(messages)).location(location)
				.type(MediaType.APPLICATION_JSON).build());
	}

	public static ErrorValidationMapper getValidationList(List<String> errorMessages) {
		ErrorValidationMapper validationError = new ErrorValidationMapper();
		for (String message : errorMessages) {
			validationError.addError(message,
					Response.Status.CONFLICT.getStatusCode());
		}

		return validationError;
	}

}
