package app.saleBadger.WebException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class NotFoundException extends WebApplicationException {
	
	private static final long serialVersionUID = 1L;

	public NotFoundException(String objectName) {
		this(objectName, "not found");
	}

	public NotFoundException(String objectName, String message) {
		super(Response.status(Response.Status.NOT_FOUND)
				.entity(objectName + " " + message).type(MediaType.TEXT_PLAIN)
				.build());
	}

}
