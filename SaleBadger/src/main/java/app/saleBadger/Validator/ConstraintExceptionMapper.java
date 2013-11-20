package app.saleBadger.Validator;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import app.saleBadger.WebException.BadRequestException;

@Provider
public class ConstraintExceptionMapper implements
		ExceptionMapper<ConstraintViolationException> {

	@Override
	public Response toResponse(ConstraintViolationException e) {
		// TODO Auto-generated method stub
		List<String> errors = new ArrayList<String>();
		for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
			errors.add(violation.getMessage());
		}
		return new BadRequestException(errors).getResponse();
	}

}
