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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import app.saleBadger.model.User;
import app.saleBadger.model.dao.UserRepository;
import app.saleBadger.model.dao.config.SpringMongoConfig;
import app.saleBadger.validator.ErrorMessagesMapper;
import app.saleBadger.webexception.ForbiddenException;
import app.saleBadger.webexception.NotFoundException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationRequestFilter implements ContainerRequestFilter {

	private final ApplicationContext context = new AnnotationConfigApplicationContext(
			SpringMongoConfig.class);
	private final UserRepository userRepository = context
			.getBean(UserRepository.class);
	public static final String AUTHORIZATION_PROPERTY = "Authorization";

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

		String username = loginInformation[0];
		String password = loginInformation[1];
		User user = userRepository.findOne(username);
		
		if (user == null) {
			errors.add(ErrorMessagesMapper
					.getString("user.does.not.exist"));
			throw new NotFoundException(errors);
		}
		String storedPassword = user.getPassword();

		// authenticate username and password
		if (!UserAuthentication.check(password, storedPassword)) {
			errors.add(ErrorMessagesMapper
					.getString("authentication.wrong.user"));
			throw new ForbiddenException(errors);
		}

		requestContext.setSecurityContext(new AuthenticationSecurityContext(
				routingContext, user));
	}
}
