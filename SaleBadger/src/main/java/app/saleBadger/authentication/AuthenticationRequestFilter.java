package app.saleBadger.authentication;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Priority;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.internal.routing.UriRoutingContext;
import org.glassfish.jersey.server.model.ResourceMethodInvoker;

import app.model.User;
import app.saleBadger.validator.ErrorMessagesMapper;
import app.saleBadger.webexception.ForbiddenException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationRequestFilter implements ContainerRequestFilter {

	private static final String AUTHORIZATION_PROPERTY = "Authorization";

	@Override
	public void filter(ContainerRequestContext requestContext)
			throws WebApplicationException {
		// TODO Auto-generated method stub
		UriRoutingContext routingContext = (UriRoutingContext) requestContext
				.getUriInfo();
		ResourceMethodInvoker invoker = (ResourceMethodInvoker) routingContext
				.getInflector();
		Method method = invoker.getResourceMethod();

		// check affected method
		if (method.isAnnotationPresent(PermitAll.class)) {
			return;
		} else if (method.isAnnotationPresent(DenyAll.class)) {
			return;
		}

		// get headers value
		final MultivaluedMap<String, String> headers = requestContext
				.getHeaders();
		final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
		List<String> errors = new ArrayList<String>();

		if (authorization == null || authorization.isEmpty()) {
			errors.add(ErrorMessagesMapper
					.getString("authentication.missing.header"));
			throw new ForbiddenException(errors);
		}

		String[] loginInformation = BasicAuthentication.decode(authorization
				.get(0));

		if (loginInformation == null || loginInformation.length != 2) {
			errors.add(ErrorMessagesMapper
					.getString("authentication.invalid.user"));
			throw new ForbiddenException(errors);
		}

		// authenticate username and password
		if (!loginInformation[0].equals("lunayo")
				|| !loginInformation[1].equals("qwertyui")) {
			errors.add(ErrorMessagesMapper
					.getString("authentication.wrong.user"));
			throw new ForbiddenException(errors);
		}

		// Dummy user
		User user = new User("lunayo", "qewrtyui", "adsfdsa", "adsfas",
				"asdfas");
		requestContext.setSecurityContext(new AuthenticationSecurityContext(
				user));
	}

}
