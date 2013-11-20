package app.saleBadger.WebException;

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import app.saleBadger.Validator.ErrorValidationMapper;

public class BadRequestException extends WebApplicationException {

	private static final long serialVersionUID = 1L;

	public BadRequestException(List<String> messages) {
		super(Response.status(Response.Status.BAD_REQUEST)
				.entity(getValidationList(messages))
				.type(MediaType.APPLICATION_JSON).build());
	}

	public static ErrorValidationMapper getValidationList(List<String> errorMessages) {
		ErrorValidationMapper validationError = new ErrorValidationMapper();
		for (String message : errorMessages) {
			validationError.addError(message,
					Response.Status.BAD_REQUEST.getStatusCode());
		}

		return validationError;
	}
}
